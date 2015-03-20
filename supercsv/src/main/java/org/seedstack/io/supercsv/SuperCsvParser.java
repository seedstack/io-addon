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

import org.seedstack.io.api.RendererErrorCode;
import org.seedstack.io.spi.AbstractTemplateParser;
import org.seedstack.seed.core.api.SeedException;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;

import javax.inject.Named;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for SuperCSV.
 *
 * @param <T> parsed object
 * @author pierre.thirouin@ext.mpsa.com
 *         Date: 25/03/14
 */
@Named("SuperCSV")
public class SuperCsvParser<T> extends AbstractTemplateParser<SuperCsvTemplate, T> {

    private static final String PARAM = "param";
    private static final String TEMPLATE_NAME = "templateName";

    @Override
    public List<T> parse(InputStream inputStream, Class<T> clazz) {
        ICsvBeanReader beanReader = null;
        List<T> beans = new ArrayList<T>();

        // Checks params nullity
        SeedException.createNew(RendererErrorCode.INCORRECT_PARAM).put(PARAM, "inputStream").throwsIfNull(inputStream);
        SeedException.createNew(RendererErrorCode.INCORRECT_PARAM).put(PARAM, "clazz").throwsIfNull(clazz);

        try {
            beanReader = new CsvBeanReader(new InputStreamReader(inputStream, template.getCharsetName()), template.getPreferences());

            if (template.showHeader()) {
                beanReader.getHeader(true);
            }

            // the header elements are used to map the values to the bean (names must match)
            final List<CellProcessor> confList = template.getReadingCellProcessors();
            final CellProcessor[] processors = confList.toArray(new CellProcessor[confList.size()]);
            final String[] fields = template.getFields().toArray(new String[template.getFields().size()]);

            T bean;
            while ((bean = beanReader.read(clazz, fields, processors)) != null) {
                beans.add(bean);
            }

        } catch (Exception e) {
            throw SeedException.wrap(e, RendererErrorCode.SUPER_CSV_EXCEPTION).put(TEMPLATE_NAME, template.getName());
        } finally {
            closeQuietly(beanReader);
        }
        return beans;
    }

    private void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            throw SeedException.wrap(e, RendererErrorCode.SUPER_CSV_EXCEPTION).put(TEMPLATE_NAME, template.getName());
        }
    }
}
