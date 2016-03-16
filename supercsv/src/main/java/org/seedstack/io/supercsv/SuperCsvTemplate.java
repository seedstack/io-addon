/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.supercsv;

import org.seedstack.io.spi.AbstractBaseTemplate;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.comment.CommentMatcher;
import org.supercsv.comment.CommentStartsWith;
import org.supercsv.encoder.CsvEncoder;
import org.supercsv.encoder.DefaultCsvEncoder;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.prefs.CsvPreference.Builder;
import org.supercsv.quote.NormalQuoteMode;
import org.supercsv.quote.QuoteMode;

import java.util.ArrayList;
import java.util.List;

/**
 * CSV Template based on SuperCSV.
 *
 * @author pierre.thirouin@ext.mpsa.com
 * @see <a href="http://supercsv.sourceforge.net/">supercsv</a>
 */
public class SuperCsvTemplate extends AbstractBaseTemplate {

    private List<Column> columns = new ArrayList<Column>();

    private char quote = '"';

    private char separator = ';';

    private String endOfLine = "\n";

    private QuoteMode quoteMode = new NormalQuoteMode();

    private CsvEncoder encoder = new DefaultCsvEncoder();

    private CommentMatcher commentMatcher;

    private String charsetName = "UTF-8";

    private boolean showHeader;

    private String name;

    /**
     * Constructor.
     *
     * @param name template name
     */
    public SuperCsvTemplate(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "This template represents a CSV file."
                + " It gives you options to change general configuration, define format or provide custom validation.";
    }

    /**
     * Builder <tt>CsvPreference</tt> with template parameters.
     *
     * @return CSV preferences
     */
    public CsvPreference getPreferences() {
        Builder builder = new CsvPreference.Builder(quote, separator, endOfLine) //
                .useQuoteMode(quoteMode) //
                .useEncoder(encoder);
        if (commentMatcher != null) {
            builder.skipComments(commentMatcher);
        }
        return builder.build();
    }

    /**
     * Sets the quote mode.
     *
     * @param quoteMode quote mode
     */
    public void setQuoteMode(QuoteMode quoteMode) {
        this.quoteMode = quoteMode;
    }

    /**
     * Sets the csv encoder.
     *
     * @param encoder csv encoder
     */
    public void setCsvEncoder(CsvEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Sets the string used to indicate comments, eg. # or //.
     *
     * @param startWith string a the beginning of a comment line
     */
    public void skipComments(String startWith) {
        this.commentMatcher = new CommentStartsWith(startWith);
    }

    /**
     * Sets the comment matcher.
     *
     * @param commentMatcher comment matcher
     */
    public void skipComments(CommentMatcher commentMatcher) {
        this.commentMatcher = commentMatcher;
    }

    /**
     * Adds a column.
     *
     * @param column csv column
     */
    public void addColumn(Column column) {
        columns.add(column);
    }

    /**
     * Removes a column.
     *
     * @param column csv column
     */
    public void removeColumn(Column column) {
        columns.remove(column);
    }

    /**
     * Gets the columns.
     *
     * @return list of column
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * Get the column headers name.
     *
     * @return list of headers
     */
    public List<String> getHeaders() {
        List<String> headers = new ArrayList<String>();
        for (Column column : columns) {
            headers.add(column.getName());
        }
        return headers;
    }

    /**
     * @return list of model fields to render
     */
    public List<String> getFields() {
        List<String> fields = new ArrayList<String>();
        for (Column column : columns) {
            fields.add(column.getField());
        }
        return fields;
    }

    /**
     * @return list of column writing configuration
     */
    public List<CellProcessor> getWritingCellProcessors() {
        List<CellProcessor> cellProcessors = new ArrayList<CellProcessor>();
        for (Column column : columns) {
            cellProcessors.add(column.getWritingCellProcessor());
        }
        return cellProcessors;
    }

    /**
     * @return list of column reading configuration
     */
    public List<CellProcessor> getReadingCellProcessors() {
        List<CellProcessor> cellProcessors = new ArrayList<CellProcessor>();
        for (Column column : columns) {
            cellProcessors.add(column.getReadingCellProcessor());
        }
        return cellProcessors;
    }

    /**
     * @param quote the quote to set
     */
    public void setQuote(char quote) {
        this.quote = quote;
    }

    /**
     * @param separator the separator to set
     */
    public void setSeparator(char separator) {
        this.separator = separator;
    }

    /**
     * @param endOfLine the endOfLine to set
     */
    public void setEndOfLine(String endOfLine) {
        this.endOfLine = endOfLine;
    }

    /**
     * @return the charsetName
     */
    public String getCharsetName() {
        return charsetName;
    }

    /**
     * @param charsetName the charsetName to set
     */
    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    /**
     * @return the showHeader
     */
    public boolean showHeader() {
        return showHeader;
    }

    /**
     * @param showHeader the showHeader to set
     */
    public void showHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

}
