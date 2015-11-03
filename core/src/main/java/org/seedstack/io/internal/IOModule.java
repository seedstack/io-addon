/**
 * Copyright (c) 2013, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.internal;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import org.seedstack.io.Parser;
import org.seedstack.io.Parsers;
import org.seedstack.io.Renderer;
import org.seedstack.io.Renderers;
import org.seedstack.io.spi.TemplateLoader;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p/>
 * This module bind the renderer (named with the name of a template) to a renderer provider.
 *
 * @author pierre.thirouin@ext.mpsa.com
 */
class IOModule extends AbstractModule {

    private List<TemplateLoader<?>> templateLoaders;

    private Map<String, Class<Renderer>> mapRenderer;

    private Map<String, Class<Parser>> mapParser;

    /**
     * Constructor.
     *
     * @param templateLoaders list of TemplateLoader
     * @param mapRenderer renderer to bind
     * @param mapParser parser to bind
     */
    IOModule(List<TemplateLoader<?>> templateLoaders, Map<String, Class<Renderer>> mapRenderer, Map<String, Class<Parser>> mapParser) {
        this.templateLoaders = templateLoaders;
        this.mapRenderer = mapRenderer;
        this.mapParser = mapParser;
    }

    @Override
    protected void configure() {

        //--- RENDERERS

        // Bind the Renderers class
        Renderers renderers = new RenderersInternal(templateLoaders, mapRenderer);
        bind(Renderers.class).toInstance(renderers);

        // Bind all renderers
        for (Entry<String, Class<Renderer>> renderer : mapRenderer.entrySet()) {
            bind(Renderer.class).annotatedWith(Names.named(renderer.getKey())).to(renderer.getValue());
        }

        // Bind the TypeListener for Render
        bindListener(Matchers.any(), new RendererTypeListener(renderers));

        //--- PARSERS

        // Bind the Parsers class
        Parsers parsers = new ParsersInternal(templateLoaders, mapParser);
        bind(Parsers.class).toInstance(parsers);

        // Bind all parsers
        for (Entry<String, Class<Parser>> parser : mapParser.entrySet()) {
            bind(Parser.class).annotatedWith(Names.named(parser.getKey())).to(parser.getValue());
        }

        // Bind the TypeListener for Parser
        bindListener(Matchers.any(), new ParserTypeListener(parsers));

    }

}
