/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.supercsv;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.net.URL;
import java.util.HashMap;

/**
 * @author pierre.thirouin@ext.mpsa.com
 *         Date: 26/03/14
 */
public class SuperCsvStaticTemplateLoaderTest {

    private static final String TEMPLATE_NAME = "templateName";
    private static final String RESOURCE = "/templates/pojo.csv.properties";
    private static final int COLUMN_NUMBER = 10;

    @Test
    public void load_template() {
        SuperCsvStaticTemplateLoader underTest = new SuperCsvStaticTemplateLoader();
        HashMap<String, URL> templateURLs = new HashMap<String, URL>();
        templateURLs.put(TEMPLATE_NAME, SuperCsvStaticTemplateLoaderTest.class.getResource(RESOURCE));
        underTest.setTemplateURLs(templateURLs);
        SuperCsvTemplate template = underTest.load(TEMPLATE_NAME);

        Assertions.assertThat(template).isNotNull();
        Assertions.assertThat(template.getCharsetName()).isEqualTo("UTF-8");
        Assertions.assertThat(template.getColumns().size()).isEqualTo(COLUMN_NUMBER);
        Assertions.assertThat(template.getReadingCellProcessors().size()).isEqualTo(COLUMN_NUMBER);
        Assertions.assertThat(template.getWritingCellProcessors().size()).isEqualTo(COLUMN_NUMBER);
        Assertions.assertThat(template.getFields().size()).isEqualTo(COLUMN_NUMBER);
        Assertions.assertThat(template.getHeaders().size()).isEqualTo(COLUMN_NUMBER);

    }

    @Test
    public void load_unkown_template() throws Exception {
        SuperCsvStaticTemplateLoader underTest = new SuperCsvStaticTemplateLoader();
        underTest.setTemplateURLs(new HashMap<String, URL>());
        SuperCsvTemplate templateNull = underTest.load(TEMPLATE_NAME);
        Assertions.assertThat(templateNull).isNull();
    }
}
