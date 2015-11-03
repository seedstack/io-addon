/**
 * Copyright (c) 2013, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.internal;


import com.google.inject.MembersInjector;
import org.seedstack.io.Parse;
import org.seedstack.io.Parser;
import org.seedstack.io.Parsers;
import org.seedstack.io.RendererErrorCode;
import io.nuun.kernel.api.plugin.PluginException;
import org.apache.commons.lang.StringUtils;
import org.seedstack.seed.SeedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author pierre.thirouin@ext.mpsa.com
 *         Date: 25/03/14
 */
class ParserMemberInjector<T> implements MembersInjector<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserMemberInjector.class);
    private final Field field;
    private final Parsers parsers;
    private final Parse annotation;

    /**
     * Constructor.
     *
     * @param field
     *            where inject the parser
     * @param parsers
     *            object which get all the parsers
     */
    ParserMemberInjector(Field field, Parsers parsers) {
        this.field = field;
        this.parsers = parsers;
        this.annotation = field.getAnnotation(Parse.class);

    }

    @Override
    public void injectMembers(T instance) {

        // Pre verification
        if (StringUtils.isEmpty(annotation.value())) {
            LOGGER.error("Value for annotation {} on field {} can not be null or empty.", annotation.annotationType(),
                    field.toGenericString());
            throw new PluginException("Value for annotation %s on field %s can not be null or empty.",
                    annotation.annotationType(), field.toGenericString());
        }

        try {
            Parser parser = parsers.getParserFor(annotation.value());
            field.setAccessible(true);
            field.set(instance, parser);
        } catch (Exception e) {
            throw SeedException.wrap(e, RendererErrorCode.LOAD_TEMPLATE_EXCEPTION);
        }
    }
}
