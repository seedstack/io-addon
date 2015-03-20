/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io;

import org.seedstack.io.api.Render;
import org.seedstack.io.api.Renderer;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.seedstack.seed.core.api.SeedException;
import org.seedstack.seed.it.SeedITRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * @author pierre.thirouin@ext.mpsa.com
 *
 */
@RunWith(SeedITRunner.class)
public class JasperRendererIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(JasperRendererIT.class);

    private List<CustomerBean> customers;
    
	/**
	 * Temporary folder to stock the file.
	 */
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	@Render("report")
	Renderer jasperRenderer;
	
    /**
     * Initialize the test.
     */
    @Before
    public void setUp() {
        // create the customer beans
        final CustomerBean john = new CustomerBean("1", "李四", "Джон Доу", new GregorianCalendar(1945, Calendar.JUNE, 13).getTime(), "1600 Amphitheatre Parkway\nMountain View, CA 94043\nUnited States", null, null, "\"May the Force be with you.\" - Star Wars", "jdunbar@gmail.com", 0L);

        final CustomerBean bob = new CustomerBean("2", "这段文字是写在简体中国，我希望它会通过", "Down", new GregorianCalendar(1919, Calendar.FEBRUARY, 25).getTime(), "1601 Willow Rd.\nMenlo Park, CA 94025\nUnited States", true, 0, "\"Frankly, my dear, I don't give a damn.\" - Gone With The Wind", "bobdown@hotmail.com", 123456L);

        customers = Arrays.asList(john, bob);
    }


	/**
	 * Exports a customerBean in PDF.
	 */
    @Test
    public void render_PDF() {
		jasperRenderer.render(new ByteArrayOutputStream(), customers, "application/pdf", null);
    }

	/**
	 * Exports a customerBean in XML.
	 */
    @Test
    public void render_PDF_in_chinese() {
    	try {
    	File file = folder.newFile("pojo.pdf");
		FileOutputStream fos = new FileOutputStream(file);
		jasperRenderer.render(fos, customers, "application/pdf", null);
		fos.close();
    	} catch (Exception e) {
    		Assertions.fail(e.getMessage(), e);
    	}
    }

	/**
	 * Exports a customerBean in RTF.
	 */
    @Test
    public void render_RTF() {
    	jasperRenderer.render(new ByteArrayOutputStream(), customers, "application/rtf", null);
    }

	/**
	 * Exports a customerBean in XLS.
	 */
    @Test
    public void render_XLS() {
    	try {
    	jasperRenderer.render(new ByteArrayOutputStream(), customers, "application/xls", null);
    	} catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
    		fail();
    	}
    }

	/**
	 * Exports a customerBean in ODT.
	 */
    @Test
    public void render_ODT() {
    	jasperRenderer.render(new ByteArrayOutputStream(), customers, "application/odt", null);
    }

	/**
	 * Exports a customerBean in ODS.
	 */
    @Test
    public void render_ODS() {
    	jasperRenderer.render(new ByteArrayOutputStream(), customers, "application/ods", null);
    }

	/**
	 * Exports a customerBean in DOCX.
	 */
    @Test
    public void render_DOCX() {
    	jasperRenderer.render(new ByteArrayOutputStream(), customers, "application/docx", null);
    }

	/**
	 * Exports a customerBean in XLSX.
	 */
    @Test
    public void render_XLSX() {
    	jasperRenderer.render(new ByteArrayOutputStream(), customers, "application/xlsx", null);
    }

	/**
	 * Exports a customerBean in PPTX.
	 */
    @Test
    public void render_PPTX() {
    	jasperRenderer.render(new ByteArrayOutputStream(), customers, "application/pptx", null);
    }

	/**
	 * Exports a customerBean in XHMTL.
	 */
    @Test
    public void render_XHMTL() {
    	jasperRenderer.render(new ByteArrayOutputStream(), customers, "text/xhmtl", null);
    }

	/**
	 * Exports a customerBean in CSV.
	 */
    @Test
    public void render_without_MIME_type() {
        try {
        	jasperRenderer.render(new ByteArrayOutputStream(), customers);
        	Assertions.failBecauseExceptionWasNotThrown(SeedException.class);
        } catch(Exception e) {
        	Assertions.assertThat(e).isNotNull();
        	LOGGER.info(e.getMessage());
        }
    }

	/**
	 * Tests exception when render with an unsupported MIME type.
	 */
    @Test
    public void render_with_fake_MIME_type() {
        try {
        	jasperRenderer.render(new ByteArrayOutputStream(), customers, "application/3D", null);
        	Assertions.failBecauseExceptionWasNotThrown(SeedException.class);
        } catch(Exception e) {
        	Assertions.assertThat(e).isNotNull();
        	LOGGER.info(e.getMessage());
        }
    }
}
