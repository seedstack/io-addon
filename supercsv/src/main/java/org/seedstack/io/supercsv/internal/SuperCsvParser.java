/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.supercsv.internal;

import org.seedstack.io.spi.AbstractTemplateParser;
import org.seedstack.io.supercsv.SuperCsvTemplate;
import org.seedstack.seed.SeedException;
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

import static com.google.common.base.Preconditions.checkNotNull;

@Named("SuperCSV")
class SuperCsvParser<T> extends AbstractTemplateParser<SuperCsvTemplate, T> {
    private static final String TEMPLATE = "template";

    @Override
    public List<T> parse(InputStream inputStream, Class<T> clazz) {
        checkNotNull(inputStream, "inputStream must not be null");
        checkNotNull(clazz, "clazz must not be null");

        ICsvBeanReader beanReader = null;
        List<T> beans = new ArrayList<>();

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
            throw SeedException.wrap(e, SuperCsvErrorCode.ERROR_DURING_SUPER_CSV_PARSING)
                    .put(TEMPLATE, template.getName());
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
            // ignore
        }
    }
}
