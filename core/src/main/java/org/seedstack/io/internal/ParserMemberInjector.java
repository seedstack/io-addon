/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.internal;


import com.google.common.base.Strings;
import com.google.inject.MembersInjector;
import io.nuun.kernel.api.plugin.PluginException;
import org.reflections.ReflectionUtils;
import org.seedstack.io.Parse;
import org.seedstack.io.Parser;
import org.seedstack.io.Parsers;
import org.seedstack.seed.SeedException;
import org.seedstack.shed.reflect.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

class ParserMemberInjector<T> implements MembersInjector<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParserMemberInjector.class);
    private final Field field;
    private final Parsers parsers;
    private final Parse annotation;

    ParserMemberInjector(Field field, Parsers parsers) {
        this.field = field;
        this.parsers = parsers;
        this.annotation = field.getAnnotation(Parse.class);
    }

    @Override
    public void injectMembers(T instance) {
        // Pre verification
        if (Strings.isNullOrEmpty(annotation.value())) {
            LOGGER.error("Value for annotation {} on field {} can not be null or empty.", annotation.annotationType(),
                    field.toGenericString());
            throw new PluginException("Value for annotation %s on field %s can not be null or empty.",
                    annotation.annotationType(), field.toGenericString());
        }

        try {
            Parser<?> parser = parsers.getParserFor(annotation.value());
            ReflectUtils.setValue(ReflectUtils.makeAccessible(field), instance, parser);
        } catch (RuntimeException e) {
            throw SeedException.wrap(e, IoErrorCode.ERROR_LOADING_TEMPLATE);
        }
    }
}
