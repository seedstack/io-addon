/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.fixture;

import org.seedstack.io.spi.AbstractBaseParser;
import org.apache.commons.io.IOUtils;

import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pierre.thirouin@ext.mpsa.com
 *         Date: 25/03/14
 */
@Named("custom")
public class CustomParser<T> extends AbstractBaseParser<T> {

    @Override
    public List<T> parse(InputStream inputStream, Class<T> clazz) {
        String data = null;
        try {
            data = IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String[] split = data.split("-");
        List<T> result = new ArrayList<T>();
        result.add((T)new BeanDTO(split[0], split[1]));
        return result;
    }
}
