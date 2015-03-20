/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.jasper;

import org.seedstack.io.api.RendererErrorCode;
import org.seedstack.io.spi.templateloader.AbstractBaseStaticTemplateLoader;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.seedstack.seed.core.api.SeedException;

import java.net.URL;

/**
 * Loads JRXML files from META-INF/templates.
 * 
 * @author pierre.thirouin@ext.mpsa.com
 * 
 */
public class JasperStaticTemplateLoader extends AbstractBaseStaticTemplateLoader<JasperTemplate> {

	@Override
	public String templatePathRegex() {
		return "(.+)\\.jrxml";
	}

	@Override
	public JasperTemplate load(String name) {
		URL url = templateURLs.get(name);
		if (url != null) {
			JasperDesign jasperDesign = null;
			try {
				jasperDesign = JRXmlLoader.load(url.openStream());
			} catch (Exception e) {
				SeedException.wrap(e, RendererErrorCode.LOAD_TEMPLATE_EXCEPTION).thenThrows();
			}
			return new JasperTemplate(jasperDesign, url.getFile());
		} else {
			return null;
		}
	}

	@Override
	public String templateRenderer() {
		return "JasperRenderer";
	}

    @Override
    public String templateParser() {
        return null;
    }
}
