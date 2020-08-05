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
import org.seedstack.io.Render;
import org.seedstack.io.Renderer;
import org.seedstack.io.Renderers;
import org.seedstack.seed.SeedException;
import org.seedstack.shed.reflect.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

class RendererMemberInjector<T> implements MembersInjector<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RendererMemberInjector.class);
    private final Field field;
    private final Renderers renderers;
    private final Render annotation;

    RendererMemberInjector(Field field, Renderers renderers) {
        this.field = field;
        this.renderers = renderers;
        this.annotation = field.getAnnotation(Render.class);

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
            Renderer renderer = renderers.getRendererFor(annotation.value());
            ReflectUtils.setValue(ReflectUtils.makeAccessible(field), instance, renderer);
        } catch (RuntimeException e) {
            throw SeedException.wrap(e, IoErrorCode.ERROR_LOADING_TEMPLATE);
        }
    }
}
