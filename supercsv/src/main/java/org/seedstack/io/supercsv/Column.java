/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.supercsv;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Define a CSV colomn. This class gets an field name, a name and a configuration.
 *
 * @author pierre.thirouin@ext.mpsa.com
 */
public class Column {

    /**
     * name of the model field
     */
    private String field;

    /**
     * column name
     */
    private String name;

    /**
     * column writing configuration
     */
    private CellProcessor writingCellProcessor;

    /**
     * column reading configuration
     */
    private CellProcessor readingCellProcessor;

    /**
     * Constructor.
     */
    public Column() {
    }

    /**
     * Constructor.
     *
     * @param field                name of the model field
     * @param name                 column name
     * @param writingCellProcessor column writing configuration
     * @param readingCellProcessor column reading configuration
     */
    public Column(String field, String name, CellProcessor writingCellProcessor, CellProcessor readingCellProcessor) {
        super();
        this.field = field;
        this.name = name;
        this.writingCellProcessor = writingCellProcessor;
        this.readingCellProcessor = readingCellProcessor;
    }

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the writingCellProcessor
     */
    public CellProcessor getWritingCellProcessor() {
        return writingCellProcessor;
    }

    /**
     * @param writingCellProcessor the writingCellProcessor to set
     */
    public void setWritingCellProcessor(CellProcessor writingCellProcessor) {
        this.writingCellProcessor = writingCellProcessor;
    }

    /**
     * @return the readingCellProcessor
     */
    public CellProcessor getReadingCellProcessor() {
        return readingCellProcessor;
    }

    /**
     * @param readingCellProcessor the readingCellProcessor to set
     */
    public void setReadingCellProcessor(CellProcessor readingCellProcessor) {
        this.readingCellProcessor = readingCellProcessor;
    }
}
