/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.supercsv.internal;

import com.google.common.base.Strings;
import org.seedstack.io.spi.AbstractBaseStaticTemplateLoader;
import org.seedstack.io.supercsv.Column;
import org.seedstack.io.supercsv.SuperCsvTemplate;
import org.seedstack.seed.SeedException;
import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.net.URL;
import java.util.Properties;

class SuperCsvStaticTemplateLoader extends AbstractBaseStaticTemplateLoader<SuperCsvTemplate> {
    private static final String NULLABLE = ".nullable";
    private static final String UNIQUE = ".unique";
    private static final String TRUE = "true";
    private static final String TYPE = ".type";
    private static final String FORMAT = ".format";
    private static final String INTEGER = "integer";
    private static final String INT = "int";
    private static final String LONG = "long";
    private static final String NUMBER = "number";
    private static final String DOUBLE = "double";
    private static final String BIG_DECIMAL = "bigDecimal";
    private static final String BOOLEAN = "boolean";
    private static final String DATE = "date";
    private static final String S_S = "\\s*,\\s*";
    private static final String BIG_BECIMAL = "bigBecimal";
    private static final String FALSE = "false";
    private static final String CHARSET_NAME = "charsetName";
    private static final String QUOTE = "quote";
    private static final String SEPARATOR = "separator";
    private static final String END_OF_LINE = "endOfLine";
    private static final String SHOW_HEADER = "showHeader";
    private static final String SUPER_CSV = "SuperCSV";
    private static final String NAME = ".name";
    private static final String COLUMNS = "columns";
    private static final String TEMPLATE = "template";
    private static final String URL = "url";

    @Override
    public String templatePathRegex() {
        return "(.+)\\.csv\\.properties";
    }

    @Override
    public SuperCsvTemplate load(String name) {
        URL url = templateURLs.get(name);
        if (url != null) {
            // Loads properties
            Properties properties = new Properties();
            try {
                properties.load(url.openStream());
            } catch (Exception e) {
                throw SeedException.wrap(e, SuperCsvErrorCode.ERROR_LOADING_SUPER_CSV_TEMPLATE)
                        .put(URL, url.toExternalForm())
                        .put(TEMPLATE, name);
            }
            SuperCsvTemplate superCsvTemplate = new SuperCsvTemplate(url.getFile());
            checkGeneralConfig(superCsvTemplate, properties);

            // Get the list of collumns and trim each element
            String columnList = properties.getProperty(COLUMNS);
            if (Strings.isNullOrEmpty(columnList)) {
                throw new IllegalArgumentException("The columns property must be initialized in template " + url.getFile());
            }
            String[] columns = columnList.trim().split(S_S);

            // For each column get the name to print and the CellProcessor
            for (String column : columns) {
                String columnName = properties.getProperty(column + NAME);
                superCsvTemplate.addColumn(new Column(column, columnName != null ? columnName : column, getWritingProcessor(column,
                        properties), getReadingProcessor(column, properties)));
            }
            return superCsvTemplate;
        }
        return null;
    }

    @Override
    public String templateRenderer() {
        return SUPER_CSV;
    }

    @Override
    public String templateParser() {
        return SUPER_CSV;
    }

    /**
     * @param template   template to initialize
     * @param properties used to initialize the template
     */
    private void checkGeneralConfig(SuperCsvTemplate template, Properties properties) {
        String charsetName = properties.getProperty(CHARSET_NAME);
        String quote = properties.getProperty(QUOTE);
        String separator = properties.getProperty(SEPARATOR);
        String endOfLine = properties.getProperty(END_OF_LINE);
        String showHeader = properties.getProperty(SHOW_HEADER);
        if (!Strings.isNullOrEmpty(quote)) {
            template.setQuote(quote.charAt(0));
        }
        if (!Strings.isNullOrEmpty(separator)) {
            template.setSeparator(separator.charAt(0));
        }
        if (!Strings.isNullOrEmpty(endOfLine)) {
            template.setEndOfLine(endOfLine);
        }
        if (!Strings.isNullOrEmpty(charsetName)) {
            template.setCharsetName(charsetName);
        }
        if (!Strings.isNullOrEmpty(showHeader) && FALSE.equals(showHeader)) {
            template.showHeader(false);
        } else {
            template.showHeader(true);
        }
    }

    private CellProcessor getWritingProcessor(String column, Properties properties) {
        String nullable = properties.getProperty(column + NULLABLE);
        String unique = properties.getProperty(column + UNIQUE);
        return checkNullable(nullable, checkUnique(unique, checkWritingType(column, properties, null)));
    }

    private CellProcessor getReadingProcessor(String column, Properties properties) {
        String nullable = properties.getProperty(column + NULLABLE);
        String unique = properties.getProperty(column + UNIQUE);
        return checkNullable(nullable, checkUnique(unique, checkReadingType(column, properties, null)));
    }

    /**
     * Checks if the column values are nullable (true by default).
     *
     * @param nullable      true or false
     * @param cellProcessor column
     */
    private CellProcessor checkNullable(String nullable, CellProcessor cellProcessor) {
        CellProcessor result;
        if (Strings.isNullOrEmpty(nullable) || TRUE.equalsIgnoreCase(nullable)) {
            if (cellProcessor != null) {
                result = new Optional(cellProcessor);
            } else {
                result = new Optional();
            }
        } else {
            if (cellProcessor != null) {
                result = new NotNull(cellProcessor);
            } else {
                result = new NotNull();
            }
        }
        return result;
    }

    /**
     * Checks if the column values must be unique (false by default).
     *
     * @param unique        true or false
     * @param cellProcessor column
     */
    private CellProcessor checkUnique(String unique, CellProcessor cellProcessor) {
        CellProcessor result = cellProcessor;
        if (TRUE.equals(unique)) {
            if (cellProcessor != null) {
                result = new UniqueHashCode(cellProcessor);
            } else {
                result = new UniqueHashCode();
            }
        }
        return result;
    }

    /**
     * Checks type and format of the column (String by default).
     *
     * @param column        column name
     * @param properties    template properties
     * @param cellProcessor column
     */
    private CellProcessor checkWritingType(String column, Properties properties, CellProcessor cellProcessor) {
        String type = properties.getProperty(column + TYPE);
        String format = properties.getProperty(column + FORMAT);
        CellProcessor result = cellProcessor;
        if (Strings.isNullOrEmpty(format)) {
            return cellProcessor;
        } else if (DATE.equalsIgnoreCase(type)) {
            result = new FmtDate(format);

        } else if (INTEGER.equalsIgnoreCase(type) || INT.equalsIgnoreCase(type)
                || DOUBLE.equalsIgnoreCase(type) || LONG.equalsIgnoreCase(type)
                || BIG_DECIMAL.equalsIgnoreCase(type) || NUMBER.equalsIgnoreCase(type)) {
            result = new FmtNumber(format);

        } else if (BOOLEAN.equalsIgnoreCase(type)) {
            String[] yesNo = format.trim().split(S_S);
            result = new FmtBool(yesNo[0], yesNo[1]);
        }
        return result;
    }

    /**
     * Checks type and format of the column (String by default).
     *
     * @param column        column name
     * @param properties    template properties
     * @param cellProcessor column
     */
    private CellProcessor checkReadingType(String column, Properties properties, CellProcessor cellProcessor) {
        String type = properties.getProperty(column + TYPE);
        String format = properties.getProperty(column + FORMAT);

        CellProcessor result = cellProcessor;
        if (!Strings.isNullOrEmpty(format) && DATE.equalsIgnoreCase(type)) {
            result = new ParseDate(format);

        } else if (NUMBER.equalsIgnoreCase(type)) {
            result = new ParseDouble();

        } else if (INT.equalsIgnoreCase(type) || INTEGER.equalsIgnoreCase(type)) {
            result = new ParseInt();

        } else if (DOUBLE.equalsIgnoreCase(type)) {
            result = new ParseDouble();

        } else if (LONG.equalsIgnoreCase(type)) {
            result = new ParseLong();

        } else if (BIG_BECIMAL.equalsIgnoreCase(type)) {
            result = new ParseBigDecimal();

        } else if (!Strings.isNullOrEmpty(format) && BOOLEAN.equalsIgnoreCase(type)) {
            String[] yesNo = format.trim().split(S_S);
            result = new ParseBool(yesNo[0], yesNo[1]);
        }

        return result;
    }
}
