/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.spi;

import org.seedstack.io.Parser;

/**
 * Class to extends to create a custom parser without template.
 *
 * @param <PARSED_OBJECT> parsed object
 */
public abstract class AbstractBaseParser<PARSED_OBJECT> implements Parser<PARSED_OBJECT> {
}
