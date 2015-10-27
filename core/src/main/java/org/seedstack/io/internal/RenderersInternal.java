/**
 * Copyright (c) 2013, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.internal;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import org.seedstack.io.api.Renderer;
import org.seedstack.io.api.RendererErrorCode;
import org.seedstack.io.api.Renderers;
import org.seedstack.io.spi.AbstractTemplateRenderer;
import org.seedstack.io.spi.template.Template;
import org.seedstack.io.spi.templateloader.TemplateLoader;
import org.apache.commons.lang.StringUtils;
import org.seedstack.seed.core.api.SeedException;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * This class implements Renderers.
 *
 * @author pierre.thirouin@ext.mpsa.com
 */
class RenderersInternal implements Renderers {

    private List<TemplateLoader<?>> templateLoaders;

    private Map<String, Class<Renderer>> renderers;

    @Inject
    Injector injector;

    /**
     * Constructor.
     */
    RenderersInternal() {
    }

    /**
     * Constructor.
     *
     * @param templateLoaders the template loaders
     * @param renderers       the renderer classes
     */
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
            if (StringUtils.isBlank(templateLoader.templateRenderer())) {
                throw SeedException.createNew(RendererErrorCode.NO_RENDERER_FOUND);
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
                    throw SeedException.wrap(e, RendererErrorCode.LOAD_TEMPLATE_EXCEPTION);
                }
            }
            return renderer;
        } else {
            try {
                return injector.getInstance(Key.get(Renderer.class, Names.named(templateName)));

            } catch (Exception e) {
                throw SeedException.wrap(e, RendererErrorCode.NO_TEMPLATE_FOUND_EXCEPTION)
                        .put("templateName", templateName);
            }
        }
    }
}
