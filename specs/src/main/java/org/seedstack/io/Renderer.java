/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io;

import java.io.OutputStream;
import java.util.Map;

/**
 * This class allow you to render a model in a specific format.
 * 
 * @author pierre.thirouin@ext.mpsa.com
 * 
 */
public interface Renderer {

	/**
	 * Renders a model in a specific format.
	 * 
	 * @param outputStream
	 *            An outputstream
	 * @param model
	 *            the model to be rendered
	 */
	void render(OutputStream outputStream, Object model);

	/**
	 * Renders a model in a specific format.
	 * 
	 * @param outputStream
	 *            An outputstream
	 * @param model
	 *            the model to be rendered
	 * @param mimeType
	 *            MIME type
	 * @param parameters
	 *            optional parameters
	 */
	void render(OutputStream outputStream, Object model, String mimeType, Map<String, Object> parameters);

}
