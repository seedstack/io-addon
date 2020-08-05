/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.internal;

import com.google.inject.Injector;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.context.Context;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequestBuilder;
import org.seedstack.io.Parser;
import org.seedstack.io.Renderer;
import org.seedstack.io.spi.StaticTemplateLoader;
import org.seedstack.io.spi.TemplateLoader;
import org.seedstack.seed.core.internal.AbstractSeedPlugin;
import org.seedstack.shed.reflect.ClassPredicates;
import org.seedstack.shed.reflect.Classes;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This plugin is responsible for detecting templates, renderers and parsers.
 */
public class IOPlugin extends AbstractSeedPlugin {
    private static final Predicate<Class<?>> templateLoaderSpec = ClassPredicates.classIsDescendantOf(TemplateLoader.class)
            .and(ClassPredicates.classIsInterface().negate())
            .and(ClassPredicates.classModifierIs(Modifier.ABSTRACT).negate());
    private static final Predicate<Class<?>> rendererSpec = ClassPredicates.classIsDescendantOf(Renderer.class)
            .and(ClassPredicates.classIsInterface().negate())
            .and(ClassPredicates.classModifierIs(Modifier.ABSTRACT).negate());
    private static final Predicate<Class<?>> parserSpec = ClassPredicates.classIsDescendantOf(Parser.class)
            .and(ClassPredicates.classIsInterface().negate())
            .and(ClassPredicates.classModifierIs(Modifier.ABSTRACT).negate());
    private static final String META_INF_TEMPLATES = "META-INF/templates/";
    private final List<StaticTemplateLoader<?>> templateLoaderRegexs = new ArrayList<>();
    private final List<TemplateLoader<?>> templateLoaders = new ArrayList<>();
    private final Map<String, Class<Renderer>> renderers = new HashMap<>();
    private final Map<String, Class<Parser>> parsers = new HashMap<>();

    @Inject
    private Injector injector;

    @Override
    public String name() {
        return "seed-io-function";
    }

    @Override
    public String pluginPackageRoot() {
        return "META-INF.templates";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests() {
        // Scan templateLoaders
        if (round.isFirst()) {
            return classpathScanRequestBuilder()
                    .predicate(templateLoaderSpec)
                    .predicate(rendererSpec)
                    .predicate(parserSpec)
                    .build();
        } else {
            // for each templateLoader with regex, parse "META-INF/templates/" directory to find corresponding templates
            ClasspathScanRequestBuilder classpathScanRequestBuilder = classpathScanRequestBuilder();
            for (StaticTemplateLoader<?> templateLoader : templateLoaderRegexs) {
                classpathScanRequestBuilder.resourcesRegex(templateLoader.templatePathRegex());
            }
            return classpathScanRequestBuilder.build();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public InitState initialize(InitContext initContext) {
        if (round.isFirst()) {
            Map<Predicate<Class<?>>, Collection<Class<?>>> scannedClassesByAnnotationClass = initContext.scannedTypesByPredicate();

            // Gets all implementation of TemplateLoader
            Collection<Class<?>> templateLoaderClasses = scannedClassesByAnnotationClass.get(templateLoaderSpec);

            if (templateLoaderClasses != null) {
                for (Class<?> templateLoaderClass : templateLoaderClasses) {
                    if (TemplateLoader.class.isAssignableFrom(templateLoaderClass)) {

                        if (StaticTemplateLoader.class.isAssignableFrom(templateLoaderClass)) {
                            Class<StaticTemplateLoader<?>> staticTemplateLoaderClass = (Class<StaticTemplateLoader<?>>) templateLoaderClass;
                            templateLoaderRegexs.add(Classes.instantiateDefault(staticTemplateLoaderClass));
                        } else {
                            Class<TemplateLoader<?>> dynamicTemplateLoaderClass = (Class<TemplateLoader<?>>) templateLoaderClass;
                            TemplateLoader<?> dynamicTemplateLoader;
                            try {
                                dynamicTemplateLoader = dynamicTemplateLoaderClass.newInstance();
                            } catch (Exception e) {
                                throw new RuntimeException("Unable to instantiate TemplateLoader " + dynamicTemplateLoaderClass.getCanonicalName(), e);
                            }
                            templateLoaders.add(dynamicTemplateLoader);
                        }
                    }
                }
            }

            // Gets all implementation of Renderer
            Collection<Class<?>> rendererClasses = scannedClassesByAnnotationClass.get(rendererSpec);
            if (rendererClasses != null) {
                for (Class<?> rendererClass : rendererClasses) {
                    if (Renderer.class.isAssignableFrom(rendererClass) && rendererClass.isAnnotationPresent(Named.class)) {
                        renderers.put(rendererClass.getAnnotation(Named.class).value(), (Class<Renderer>) rendererClass);
                    }
                }
            }

            // Gets all implementation of Parser
            Collection<Class<?>> parserClasses = scannedClassesByAnnotationClass.get(parserSpec);
            if (parserClasses != null) {
                for (Class<?> parserClass : parserClasses) {
                    if (Parser.class.isAssignableFrom(parserClass) && parserClass.isAnnotationPresent(Named.class)) {
                        parsers.put(parserClass.getAnnotation(Named.class).value(), (Class<Parser>) parserClass);
                    }
                }
            }

            return InitState.NON_INITIALIZED;
        } else {
            Map<String, Collection<String>> mapResourcesByRegex = initContext.mapResourcesByRegex();


            for (StaticTemplateLoader<?> staticTemplateLoader : templateLoaderRegexs) {
                Map<String, URL> templateURLs = new HashMap<>();

                Collection<String> templateUrls = mapResourcesByRegex.get(staticTemplateLoader.templatePathRegex());
                for (String templateUrl : templateUrls) {
                    if (templateUrl.startsWith(META_INF_TEMPLATES)) {
                        String templateName = substringAfter(templateUrl, META_INF_TEMPLATES);
                        Matcher matcher = Pattern.compile(staticTemplateLoader.templatePathRegex()).matcher(templateName);
                        if (matcher.matches()) {
                            URL url = IOPlugin.class.getResource("/" + templateUrl);
                            if (url != null) {
                                templateURLs.put(matcher.group(1), url);
                            }
                        }
                    }
                }
                staticTemplateLoader.setTemplateURLs(templateURLs);
                this.templateLoaders.add(staticTemplateLoader);

            }
        }
        return InitState.INITIALIZED;
    }

    @Override
    public void start(Context context) {
        // template loaders have been created manually in init step
        for (TemplateLoader<?> templateLoader : templateLoaders) {
            injector.injectMembers(templateLoader);
        }
    }

    @Override
    public Object nativeUnitModule() {
        return new IOModule(templateLoaders, renderers, parsers);
    }

    private String substringAfter(String str, String sep) {
        int idx = str.indexOf(sep);
        if (idx == -1) {
            return str;
        } else {
            return str.substring(idx + sep.length());
        }
    }
}
