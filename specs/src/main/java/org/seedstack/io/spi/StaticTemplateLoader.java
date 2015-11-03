/**
 * Copyright (c) 2013, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.spi;

import java.net.URL;
import java.util.Map;

/**
 * StaticTemplateLoader loads static templates from META-INF directory at the plugin startup. Static templates to load are defined
 * by a regex specified with <tt>templatePathRegex</tt>.
 * 
 * @author pierre.thirouin@ext.mpsa.com
 * @param <T>
 *            template
 * 
 */
public interface StaticTemplateLoader<T extends Template> extends TemplateLoader<T> {

	/**
	 * @return template regex
	 */
	String templatePathRegex();

	/**
	 * @param templateURLs
	 */
	void setTemplateURLs(Map<String, URL> templateURLs);

}
