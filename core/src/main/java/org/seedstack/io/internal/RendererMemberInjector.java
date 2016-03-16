/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.internal;

import com.google.inject.MembersInjector;
import org.seedstack.io.Render;
import org.seedstack.io.Renderer;
import org.seedstack.io.RendererErrorCode;
import org.seedstack.io.Renderers;
import io.nuun.kernel.api.plugin.PluginException;
import org.apache.commons.lang.StringUtils;
import org.seedstack.seed.SeedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @param <T> template
 * @author pierre.thirouin@ext.mpsa.com
 */
class RendererMemberInjector<T> implements MembersInjector<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RendererMemberInjector.class);
    private final Field field;
    private final Renderers renderers;
    private final Render annotation;

    /**
     * Constructor.
     *
     * @param field     where inject the renderer
     * @param renderers object which get all the renderers
     */
    RendererMemberInjector(Field field, Renderers renderers) {
        this.field = field;
        this.renderers = renderers;
        this.annotation = field.getAnnotation(Render.class);

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
            Renderer renderer = renderers.getRendererFor(annotation.value());
            field.setAccessible(true);
            field.set(instance, renderer);
        } catch (Exception e) {
            throw SeedException.wrap(e, RendererErrorCode.LOAD_TEMPLATE_EXCEPTION);
        }
    }

}
