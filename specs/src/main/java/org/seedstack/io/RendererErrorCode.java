/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io;


import org.seedstack.seed.ErrorCode;

/**
 * @author pierre.thirouin@ext.mpsa.com
 * 
 */
public enum RendererErrorCode implements ErrorCode {

	/**
	 * Exception thrown when the template can't be load.
	 */
	LOAD_TEMPLATE_EXCEPTION,
	
	/**
	 * Exception thrown when no template can be found.
	 */
	NO_TEMPLATE_FOUND_EXCEPTION,
	
	/**
	 * Exception thrown by Jasper renderer.
	 */
	JASPER_EXCEPTION,

	/**
	 * Exception thrown by Jasper renderer when it is unable to compile the jasper design.
	 */
	COMPILE_JASPER_REPORT_EXCEPTION,

	/**
	 * Exception thrown by SuperCSV renderer.
	 */
	SUPER_CSV_EXCEPTION, NO_PARSER_FOUND, INCORRECT_PARAM, NO_RENDERER_FOUND
}
