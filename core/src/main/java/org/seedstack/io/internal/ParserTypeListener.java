/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.internal;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.seedstack.io.Parse;
import org.seedstack.io.Parser;
import org.seedstack.io.Parsers;

import java.lang.reflect.Field;

class ParserTypeListener implements TypeListener {
    private final Parsers parsers;

    ParserTypeListener(Parsers parsers) {
        this.parsers = parsers;
    }

    @Override
    public <T> void hear(TypeLiteral<T> type, TypeEncounter<T> encounter) {
        for (Field field : type.getRawType().getDeclaredFields()) {
            if (field.getType() == Parser.class && field.isAnnotationPresent(Parse.class)) {
                encounter.register(new ParserMemberInjector<>(field, parsers));
            }
        }
    }
}
