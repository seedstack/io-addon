/**
 * Copyright (c) 2013, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.spi.templateloader;

import java.util.Set;

import org.seedstack.io.spi.template.Template;

/**
 * A <tt>TemplateLoader</tt> is associated to an {@link org.seedstack.io.spi.AbstractTemplateRenderer}. The association is made by the name of the
 * renderer specified with <tt>templateRenderer</tt>.
 * 
 * @author pierre.thirouin@ext.mpsa.com
 * 
 * @param <T>
 *            template
 */
public interface TemplateLoader<T extends Template> {

	/**
	 * @param name
	 *            name of the resquested template
	 * @return a template
	 */
	T load(String name);

	/**
	 * @return list of the template names
	 */
	Set<String> names();

	/**
	 * @param name
	 *            template name
	 * @return true if the loader contains the template, false otherwise
	 */
	boolean contains(String name);

	/**
	 * @return key of the renderer associated to the template. Should return null if there is no renderer associated.
	 */
	String templateRenderer();

    /**
     * @return key of the parser associated to the template. Should return null if there is no parser associated.
     */
    String templateParser();
}
