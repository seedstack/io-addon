/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.jasper.fixtures;

import com.google.common.base.Preconditions;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.seedstack.io.spi.AbstractBaseRenderer;

import javax.inject.Named;
import java.io.OutputStream;
import java.util.Map;

@Named("myCustomXlsRenderer")
public class CustomXlsRenderer extends AbstractBaseRenderer {
    @Override
    public void render(OutputStream outputStream, Object model) {
        render(outputStream, model, "application/xls", null);
    }

    @Override
    public void render(OutputStream outputStream, Object model, String mimeType, Map<String, Object> parameters) {
        Preconditions.checkState("application/xls".equals(mimeType));
        try {
            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet("new sheet");

            CreationHelper createHelper = wb.getCreationHelper();
            // Create a row and put some cells in it. Rows are 0 based.
            Row row = sheet.createRow((short) 0);

            // Or do it on one line.
            CustomerBean bean = (CustomerBean) model;
            row.createCell(1).setCellValue(bean.getCustomerNo());
            row.createCell(2).setCellValue(createHelper.createRichTextString(bean.getFirstName()));
            row.createCell(3).setCellValue(createHelper.createRichTextString(bean.getLastName()));
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
            Cell cell = row.createCell(4);
            cell.setCellValue(bean.getBirthDate());
            cell.setCellStyle(cellStyle);
            row.createCell(5).setCellValue(bean.getMailingAddress());
            row.createCell(6).setCellValue(bean.getMarried());
            row.createCell(7).setCellValue(bean.getNumberOfKids());
            row.createCell(8).setCellValue(bean.getFavouriteQuote());
            row.createCell(9).setCellValue(bean.getEmail());
            row.createCell(10).setCellValue(bean.getLoyaltyPoints());

            wb.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
