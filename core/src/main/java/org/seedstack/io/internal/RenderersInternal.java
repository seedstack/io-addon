/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.internal;

import com.google.common.base.Strings;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import org.seedstack.io.Renderer;
import org.seedstack.io.Renderers;
import org.seedstack.io.spi.AbstractTemplateRenderer;
import org.seedstack.io.spi.Template;
import org.seedstack.io.spi.TemplateLoader;
import org.seedstack.seed.SeedException;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

class RenderersInternal implements Renderers {
    private static final String TEMPLATE = "template";
    private final List<TemplateLoader<?>> templateLoaders;
    private final Map<String, Class<Renderer>> renderers;
    @Inject
    private Injector injector;

    RenderersInternal(List<TemplateLoader<?>> templateLoaders, Map<String, Class<Renderer>> renderers) {
        this.templateLoaders = templateLoaders;
        this.renderers = renderers;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Renderer getRendererFor(String templateName) {
        TemplateLoader<?> templateLoader = null;
        for (TemplateLoader<?> tl : templateLoaders) {
            if (tl.contains(templateName)) {
                templateLoader = tl;
                break;
            }
        }

        // For renderers with template
        if (templateLoader != null) {
            if (Strings.isNullOrEmpty(templateLoader.templateRenderer())) {
                throw SeedException.createNew(IoErrorCode.NO_RENDERER_FOUND)
                        .put(TEMPLATE, templateName);
            }

            Renderer renderer = injector.getInstance(Key.get(Renderer.class, Names.named(templateLoader.templateRenderer())));
            Class<?> rendererClazz = renderers.get(templateLoader.templateRenderer());
            if (AbstractTemplateRenderer.class.isAssignableFrom(rendererClazz)) {
                // Loads the template and initializes the renderer
                Template template;
                try {
                    template = templateLoader.load(templateName);
                    ((AbstractTemplateRenderer) renderer).setTemplate(template);
                    //
                    // Catch all possible fails when fail to load a template
                    //
                } catch (Exception e) {
                    throw SeedException.wrap(e, IoErrorCode.ERROR_LOADING_TEMPLATE)
                            .put(TEMPLATE, templateName);
                }
            }
            return renderer;
        } else {
            try {
                return injector.getInstance(Key.get(Renderer.class, Names.named(templateName)));
            } catch (Exception e) {
                throw SeedException.wrap(e, IoErrorCode.NO_TEMPLATE_FOUND_EXCEPTION)
                        .put(TEMPLATE, templateName);
            }
        }
    }
}
