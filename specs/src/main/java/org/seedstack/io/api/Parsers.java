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

/**
 * @author pierre.thirouin@ext.mpsa.com
 *         Date: 24/03/14
 */
public interface Parsers {

    /**
     * Returns a parser corresponding to a specific key.
     *
     * @param key
     *            The key associated to the parser
     * @return Renderer
     */
    Parser getParserFor(String key);
}
