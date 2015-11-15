/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.spi;

/**
 * Class to be extended to create a new template.
 *
 * @author pierre.thirouin@ext.mpsa.com
 *
 */
public abstract class AbstractBaseTemplate implements Template {

	/**
	 * @return the name
	 */
	public abstract String getName();

	/**
	 * @return the description
	 */
	public abstract String getDescription();
}
