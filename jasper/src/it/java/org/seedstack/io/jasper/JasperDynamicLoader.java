/**
 * Copyright (c) 2013, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.jasper;

import org.seedstack.io.spi.TemplateLoader;

import java.util.HashSet;
import java.util.Set;

/**
 * @author pierre.thirouin@ext.mpsa.com
 */
public class JasperDynamicLoader implements TemplateLoader<JasperTemplate> {

	@Override
	public JasperTemplate load(String name) {
		return new JasperTemplate();
	}

	@Override
	public Set<String> names() {
		Set<String> set = new HashSet<String>();
		set.add("JasperDynamicLoader");
		return set;
	}

	@Override
	public boolean contains(String name) {
		return "JasperDynamicLoader".equals(name);
	}

	@Override
	public String templateRenderer() {
		return "JasperRenderer";
	}

    @Override
    public String templateParser() {
        throw new UnsupportedOperationException("Jasper module does not provide a parser");
    }
}
