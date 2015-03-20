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
import com.google.inject.Key;
import com.google.inject.name.Names;
import org.seedstack.io.api.Parser;
import org.seedstack.io.api.Parsers;
import org.seedstack.io.api.RendererErrorCode;
import org.seedstack.io.spi.AbstractTemplateParser;
import org.seedstack.io.spi.template.Template;
import org.seedstack.io.spi.templateloader.TemplateLoader;
import org.apache.commons.lang.StringUtils;
import org.seedstack.seed.core.api.SeedException;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * @author pierre.thirouin@ext.mpsa.com
 *         Date: 25/03/14
 */
class ParsersInternal implements Parsers {

    private List<TemplateLoader<?>> templateLoaders;

    private Map<String, Class<Parser>> parsers;

    @Inject
    Injector injector;

    ParsersInternal() {
    }

    ParsersInternal(List<TemplateLoader<?>> templateLoaders, Map<String, Class<Parser>> parsers) {
        this.templateLoaders = templateLoaders;
        this.parsers = parsers;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Parser getParserFor(String templateName) {
        // Finds the template loader associated to the given template name
        TemplateLoader<?> templateLoader = null;
        for (TemplateLoader<?> tl : templateLoaders) {
            if (tl.contains(templateName)) {
                templateLoader = tl;
                break;
            }
        }

        // For parsers with template
        if (templateLoader != null) {
            if (StringUtils.isBlank(templateLoader.templateParser())) {
                throw SeedException.createNew(RendererErrorCode.NO_PARSER_FOUND);
            }

            Parser parser = injector.getInstance(Key.get(Parser.class, Names.named(templateLoader.templateParser())));
            Class<?> parserClazz = parsers.get(templateLoader.templateParser());
            if (AbstractTemplateParser.class.isAssignableFrom(parserClazz)) {
                // Loads the template and initializes the parser
                Template template;
                try {
                    template = templateLoader.load(templateName);
                    ((AbstractTemplateParser) parser).setTemplate(template);

                    // Catch all possible fails when fail to load a template
                } catch (Exception e) {
                    throw SeedException.wrap(e, RendererErrorCode.LOAD_TEMPLATE_EXCEPTION);
                }
            }
            return parser;
        } else {
            try {
                return injector.getInstance(Key.get(Parser.class, Names.named(templateName)));

            } catch (Exception e) {
                throw SeedException.wrap(e, RendererErrorCode.NO_TEMPLATE_FOUND_EXCEPTION)
                        .put("templateName", templateName);
            }
        }
    }
}
