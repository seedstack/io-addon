/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io;

import java.io.InputStream;
import java.util.List;

/**
 * Parser provide a method to parse object from an InputStream.
 *
 * @param <PARSED_OBJECT> parsed object
 * @author pierre.thirouin@ext.mpsa.com
 *         Date: 24/03/14
 */
public interface Parser<PARSED_OBJECT> {

    /**
     * Parses an InputStream and retrieves object.
     *
     * @param inputStream input stream
     * @param clazz       parsed object
     * @return list of T
     */
    List<PARSED_OBJECT> parse(InputStream inputStream, Class<PARSED_OBJECT> clazz);

}
