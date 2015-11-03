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
import java.util.Set;

/**
 * This class should be extended to add a loader of static templates. Static templates are load from the META-INF/templates
 * directory.
 * 
 * @author pierre.thirouin@ext.mpsa.com
 * @param <T>
 *            template
 */
public abstract class AbstractBaseStaticTemplateLoader<T extends Template> implements StaticTemplateLoader<T> {

	protected Map<String, URL> templateURLs;

	/**
	 * Constructor.
	 */
	public AbstractBaseStaticTemplateLoader() {
	}

	@Override
	public Set<String> names() {
		return templateURLs.keySet();
	}

	@Override
	public boolean contains(String name) {
		return templateURLs.containsKey(name);
	}

	/**
	 * @param templateURLs
	 *            the templateURLs to set
	 */
	public void setTemplateURLs(Map<String, URL> templateURLs) {
		this.templateURLs = templateURLs;
	}

}
