/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.spi;

import org.seedstack.io.api.Parser;

import java.io.InputStream;
import java.util.List;

/**
 * Class to extends in order to have.
 *
 * @param <TEMPLATE> template used to parse.
 * @param <PARSED_OBJECT> object parsed.
 *
 * @author pierre.thirouin@ext.mpsa.com
 *         Date: 25/03/14
 */
public abstract class AbstractTemplateParser<TEMPLATE, PARSED_OBJECT> implements Parser<PARSED_OBJECT> {

    /**
     * Parser template
     */
    protected TEMPLATE template;

    /**
     * @param template template to set
     */
    public void setTemplate(TEMPLATE template) {
        this.template = template;
    }

    /**
     * Parse an InputStream as "clazz" object.
     *
     * @param inputStream input stream
     * @param clazz       expected object
     * @return list of parsed object
     */
    public abstract List<PARSED_OBJECT> parse(InputStream inputStream, Class<PARSED_OBJECT> clazz);
}
