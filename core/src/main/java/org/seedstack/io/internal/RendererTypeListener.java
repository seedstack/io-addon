/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.internal;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.seedstack.io.Render;
import org.seedstack.io.Renderer;
import org.seedstack.io.Renderers;

import java.lang.reflect.Field;

class RendererTypeListener implements TypeListener {
    private final Renderers renderers;

    RendererTypeListener(Renderers renderers) {
        this.renderers = renderers;
    }

    @Override
    public <T> void hear(TypeLiteral<T> type, TypeEncounter<T> encounter) {
        for (Field field : type.getRawType().getDeclaredFields()) {
            if (field.getType() == Renderer.class && field.isAnnotationPresent(Render.class)) {
                encounter.register(new RendererMemberInjector<>(field, renderers));
            }
        }
    }
}
