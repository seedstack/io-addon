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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Render annotation marks fields which will be automatically valued with the renderer corresponding to the value of the annotation.
 * 
 * @author pierre.thirouin@ext.mpsa.com
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Render {

	/**
	 * @return renderer key
	 */
	String value();
}
