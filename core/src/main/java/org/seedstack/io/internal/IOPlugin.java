/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.internal;

import com.google.inject.Injector;
import org.seedstack.io.api.Parser;
import org.seedstack.io.api.Renderer;
import org.seedstack.io.spi.templateloader.StaticTemplateLoader;
import org.seedstack.io.spi.templateloader.TemplateLoader;
import io.nuun.kernel.api.plugin.InitState;
import io.nuun.kernel.api.plugin.RoundEnvironment;
import io.nuun.kernel.api.plugin.context.Context;
import io.nuun.kernel.api.plugin.context.InitContext;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequest;
import io.nuun.kernel.api.plugin.request.ClasspathScanRequestBuilder;
import io.nuun.kernel.core.AbstractPlugin;
import org.apache.commons.lang.StringUtils;
import org.kametic.specifications.Specification;
import org.seedstack.seed.core.utils.SeedSpecifications;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pierre.thirouin@ext.mpsa.com
 */
public class IOPlugin extends AbstractPlugin {

    private static final String META_INF_TEMPLATES = "META-INF/templates/";
    private List<StaticTemplateLoader<?>> templateLoaderRegexs = new ArrayList<StaticTemplateLoader<?>>();
    private List<TemplateLoader<?>> templateLoaders = new ArrayList<TemplateLoader<?>>();
    private RoundEnvironment roundEnvironment;
    private Specification<Class<?>> templateLoaderSpec;
    private Specification<Class<?>> rendererSpec;
    private Specification<Class<?>> parserSpec;
    private Map<String, Class<Renderer>> renderers = new HashMap<String, Class<Renderer>>();
    private Map<String, Class<Parser>> parsers = new HashMap<String, Class<Parser>>();

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
        // Scan templateloaders
        if (roundEnvironment.firstRound()) {
            templateLoaderSpec = and(descendantOf(TemplateLoader.class), not(SeedSpecifications.classIsInterface()), not(SeedSpecifications.classIsAbstract()));
            rendererSpec = and(descendantOf(Renderer.class), not(SeedSpecifications.classIsInterface()), not(SeedSpecifications.classIsAbstract()));
            parserSpec = and(descendantOf(Parser.class), not(SeedSpecifications.classIsInterface()), not(SeedSpecifications.classIsAbstract()));

            return classpathScanRequestBuilder().specification(templateLoaderSpec).specification(rendererSpec).specification(parserSpec).build();
        } else {
            // for each templateloader with regex, parse "META-INF/templates/" directory to find corresponding templates
            ClasspathScanRequestBuilder classpathScanRequestBuilder = classpathScanRequestBuilder();
            for (StaticTemplateLoader<?> templateLoader : templateLoaderRegexs) {
                classpathScanRequestBuilder.resourcesRegex(templateLoader.templatePathRegex());
            }
            return classpathScanRequestBuilder.build();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public InitState init(InitContext initContext) {
        if (roundEnvironment.firstRound()) {
            Map<Specification, Collection<Class<?>>> scannedClassesByAnnotationClass = initContext.scannedTypesBySpecification();

            // Gets all implementation of TemplateLoader
            Collection<Class<?>> templateLoaderClasses = scannedClassesByAnnotationClass.get(templateLoaderSpec);

            if (templateLoaderClasses != null) {
                for (Class<?> templateLoaderClass : templateLoaderClasses) {
                    if (TemplateLoader.class.isAssignableFrom(templateLoaderClass)) {

                        if (StaticTemplateLoader.class.isAssignableFrom(templateLoaderClass)) {
                            Class<StaticTemplateLoader<?>> staticTemplateLoaderClass = (Class<StaticTemplateLoader<?>>) templateLoaderClass;
                            StaticTemplateLoader<?> staticTemplateLoader;
                            try {
                                staticTemplateLoader = staticTemplateLoaderClass.newInstance();
                            } catch (Exception e) {
                                throw new RuntimeException("Unable to instantiate StaticTemplateLoader " + staticTemplateLoaderClass.getCanonicalName(), e);
                            }
                            templateLoaderRegexs.add(staticTemplateLoader);

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
                Map<String, URL> templateURLs = new HashMap<String, URL>();

                Collection<String> templateUrls = mapResourcesByRegex.get(staticTemplateLoader.templatePathRegex());
                for (String templateUrl : templateUrls) {
                    if (templateUrl.startsWith(META_INF_TEMPLATES)) {
                        String templateName = StringUtils.substringAfter(templateUrl, META_INF_TEMPLATES);
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
    public void provideRoundEnvironment(RoundEnvironment roundEnvironment) {
        this.roundEnvironment = roundEnvironment;

    }

    @Override
    public Object nativeUnitModule() {
        return new IOModule(templateLoaders, renderers, parsers);
    }

}
