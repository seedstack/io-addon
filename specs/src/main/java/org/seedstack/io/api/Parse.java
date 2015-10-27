/**
 * Copyright (c) 2013, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.api;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Parse annotation defines the parser to inject.
 *
 * @author pierre.thirouin@ext.mpsa.com
 *         Date: 24/03/14
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Parse {

    /**
     * Parser key.
     * @return
     */
    String value();

}
