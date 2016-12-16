/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.fixture;

import com.google.common.io.CharStreams;
import org.seedstack.io.spi.AbstractBaseParser;

import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Named("custom")
public class CustomParser<T> extends AbstractBaseParser<T> {

    @Override
    public List<T> parse(InputStream inputStream, Class<T> clazz) {
        String data = null;
        try {
            InputStreamReader r = new InputStreamReader(inputStream, "UTF-8");
            data = CharStreams.toString(r);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String[] split = data.split("-");
        List<T> result = new ArrayList<>();
        result.add((T) new BeanDTO(split[0], split[1]));
        return result;
    }
}
