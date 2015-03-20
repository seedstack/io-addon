/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.api;

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
