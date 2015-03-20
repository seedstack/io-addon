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

import org.seedstack.io.spi.template.AbstractBaseTemplate;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.design.JasperDesign;

import java.util.Map;

/**
 * @author pierre.thirouin@ext.mpsa.com
 * 
 */
class JasperTemplate extends AbstractBaseTemplate {

	private JasperDesign jasperDesign;

	private Map<JRExporterParameter, Object> jrExporterParameters;

	private String name;

	/**
	 * Constructor.
	 */
	JasperTemplate() {
	}

    /**
     * Constructor.
     *
     * @param jasperDesign
     *            The JasperDesign object
     * @param name
     *            template name
     */
    public JasperTemplate(JasperDesign jasperDesign, String name) {
        super();
        this.jasperDesign = jasperDesign;
        this.name = name;
    }

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return "This template is based on JasperDesign";
	}

	/**
	 * @return the jasperDesign
	 */
	public JasperDesign getJasperDesign() {
		return jasperDesign;
	}

	/**
	 * @param jasperDesign
	 *            the jasperDesign to set
	 */
	public void setJasperDesign(JasperDesign jasperDesign) {
		this.jasperDesign = jasperDesign;
	}

	/**
	 * @return the jrExporterParameters
	 */
	public Map<JRExporterParameter, Object> getJrExporterParameters() {
		return jrExporterParameters;
	}

	/**
	 * @param jrExporterParameters
	 *            the jrExporterParameters to set
	 */
	public void setJrExporterParameters(Map<JRExporterParameter, Object> jrExporterParameters) {
		this.jrExporterParameters = jrExporterParameters;
	}

}
