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

import com.google.common.collect.Lists;
import org.seedstack.io.api.RendererErrorCode;
import org.seedstack.io.spi.AbstractTemplateRenderer;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import org.seedstack.seed.core.api.SeedException;

import javax.inject.Named;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class is initialized with a JasperDesign file and provide PDF and XLS.
 *
 * @author pierre.thirouin@ext.mpsa.com
 */
@Named("JasperRenderer")
class JasperRenderer extends AbstractTemplateRenderer<JasperTemplate> {

    private JasperReport jasperReport;

    /**
     * Constructor.
     */
    JasperRenderer() {
    }

    /**
     * This implementation doesn't use the parameters attribute.
     *
     * @see org.seedstack.io.api.Renderer#render(java.io.OutputStream, java.lang.Object, java.lang.String, java.util.Map)
     */
    @Override
    public void render(OutputStream outputStream, Object object, String mimeType, Map<String, Object> parameters) {
        try {
            if (jasperReport == null) {
                // Convert template to JasperDesign
                JasperDesign jd = template.getJasperDesign();
                // Compile report
                try {
                    jasperReport = JasperCompileManager.compileReport(jd);
                } catch (NoClassDefFoundError error) {

                    // Show an appropriate exception when jasper template language is set to Groovy
                    if ("org/codehaus/groovy/control/CompilationFailedException".equals(error.getMessage())) {
                        throw SeedException.wrap(error, RendererErrorCode.COMPILE_JASPER_REPORT_EXCEPTION)
                                .put("templateName", template.getName());
                    } else {
                        throw SeedException.wrap(error, RendererErrorCode.COMPILE_JASPER_REPORT_EXCEPTION)
                                .put("templateName", template.getName());
                    }

                    // Catch all possible fails when compiling a jasper report
                } catch (Exception e) {
                    throw SeedException.wrap(e, RendererErrorCode.COMPILE_JASPER_REPORT_EXCEPTION)
                            .put("templateName", template.getName());
                }
            }

            // If model is not already a collection add it in a new collection
            Collection<?> col;
            if (object instanceof Collection) {
                col = (Collection<?>) object;
            } else {
                col = Lists.newArrayList(object);
            }

            // Create the JasperPrint object
            JasperPrint jp = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(
                    col));

            // Export report
            export(mimeType, jp, outputStream);

            //
            // Catch all possible fails when render a jasper report
            //
        } catch (Exception e) {
            throw SeedException.wrap(e, RendererErrorCode.JASPER_EXCEPTION);
        }
    }

    @Override
    public void render(OutputStream outputStream, Object model) {
        throw new IllegalArgumentException("Ambiguous MIME type, JasperRenderer does not support default MIME type");
    }

    /**
     * Exports by MIME type.
     *
     * @param mimeType MIME type
     * @param jp       Jasper print
     * @param os       outputstream
     * @throws JRException
     */
    protected void export(String mimeType, JasperPrint jp, OutputStream os) throws JRException {
        if ("application/pdf".equalsIgnoreCase(mimeType)) {
            exportReport(new JRPdfExporter(), jp, os);

        } else if ("text/xml".equalsIgnoreCase(mimeType)) {
            exportReport(new HtmlExporter(), jp, os);

        } else if ("application/rtf".equalsIgnoreCase(mimeType)) {
            exportReport(new JRRtfExporter(), jp, os);

        } else if ("application/xls".equalsIgnoreCase(mimeType)) {
            exportReport(new JRXlsExporter(), jp, os);

        } else if ("application/odt".equalsIgnoreCase(mimeType)) {
            exportReport(new JROdtExporter(), jp, os);

        } else if ("application/ods".equalsIgnoreCase(mimeType)) {
            exportReport(new JROdsExporter(), jp, os);

        } else if ("application/docx".equalsIgnoreCase(mimeType)) {
            exportReport(new JRDocxExporter(), jp, os);

        } else if ("application/xlsx".equalsIgnoreCase(mimeType)) {
            exportReport(new JRXlsxExporter(), jp, os);

        } else if ("application/pptx".equalsIgnoreCase(mimeType)) {
            exportReport(new JRPptxExporter(), jp, os);

        } else if ("text/xhmtl".equalsIgnoreCase(mimeType)) {
            exportReport(new JRXhtmlExporter(), jp, os);
        } else {
            throw new IllegalArgumentException("JasperRenderer does not support " + mimeType + " MIME type.");
        }
    }

    /**
     * Xls export.
     *
     * @param jp
     * @param os
     */
    protected void exportXls(JasperPrint jp, OutputStream os) {
        // Create a JRXlsExporter instance
        JRXlsExporter exporter = new JRXlsExporter();

        // Here we assign the parameters jp and baos to the exporter
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);

        // TODO add Excel specific parameters
        exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);

        try {
            exporter.exportReport();

        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @throws JRException
     */
    protected void exportReport(JRExporter exporter, JasperPrint jp, OutputStream os) throws JRException {
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);

        if (template.getJrExporterParameters() != null) {
            for (Entry<JRExporterParameter, Object> entry : template.getJrExporterParameters().entrySet()) {
                exporter.setParameter(entry.getKey(), entry.getValue());

            }
        }
        try {
            exporter.exportReport();

        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

}
