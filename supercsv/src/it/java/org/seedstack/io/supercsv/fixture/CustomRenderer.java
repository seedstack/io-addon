/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.supercsv.fixture;

import org.seedstack.io.spi.AbstractBaseRenderer;

import javax.inject.Named;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Named("custom")
public class CustomRenderer extends AbstractBaseRenderer {
    @Override
    public void render(OutputStream outputStream, Object model) {
        render(outputStream, model, null, null);
    }

    @Override
    public void render(OutputStream outputStream, Object model, String mimeType, Map<String, Object> parameters) {
        try {
            outputStream.write("Hello World!".getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
