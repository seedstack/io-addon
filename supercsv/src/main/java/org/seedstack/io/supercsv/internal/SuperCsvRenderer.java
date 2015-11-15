/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.supercsv.internal;

import org.seedstack.io.RendererErrorCode;
import org.seedstack.io.spi.AbstractTemplateRenderer;
import org.apache.commons.lang.StringUtils;
import org.seedstack.io.supercsv.SuperCsvTemplate;
import org.seedstack.seed.SeedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;

import javax.inject.Named;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class render CSV based on a {@link SuperCsvTemplate}. This Renderer only supports <tt>application/csv</tt> MIME type.
 * 
 * @author pierre.thirouin@ext.mpsa.com
 * @see <a href="http://supercsv.sourceforge.net/">SuperCSV</a>
 */
@Named("SuperCSV")
class SuperCsvRenderer extends AbstractTemplateRenderer<SuperCsvTemplate> {

	/**
	 * MIME type supported by the renderer
	 */
	private static final String SUPPORTED_MIME_TYPE = "application/csv";

	private static final Logger LOGGER = LoggerFactory.getLogger(SuperCsvRenderer.class);

	/**
	 * Constructor.
	 */
	SuperCsvRenderer() {
	}

	@Override
	public void render(OutputStream outputStream, Object model) {
		render(outputStream, model, null, null);
	}

	@Override
	public void render(OutputStream outputStream, Object model, String mimeType, Map<String, Object> parameters) {
		if (mimeType != null && !StringUtils.equals(mimeType, SUPPORTED_MIME_TYPE)) {
			throw new IllegalArgumentException(mimeType
					+ " not supported. SuperCsvRenderer only supports application/csv MIME type");
		}

		ICsvBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvBeanWriter(new OutputStreamWriter(outputStream, template.getCharsetName()),
					template.getPreferences());

			final List<CellProcessor> confList = template.getWritingCellProcessors();
			final CellProcessor[] processors = confList.toArray(new CellProcessor[confList.size()]);
			final String[] fields = template.getFields().toArray(new String[template.getFields().size()]);

			// write the header
			if (template.showHeader()) {
				beanWriter.writeHeader(template.getHeaders().toArray(new String[template.getHeaders().size()]));
			}

			// If model is a collection, iterates over the collection
			if (model instanceof Collection) {
				Collection<?> col = (Collection<?>) model;
				Iterator<?> it = col.iterator();
				while (it.hasNext()) {
					beanWriter.write(it.next(), fields, processors);
				}
			} else {
				// else write the pojo
				beanWriter.write(model, fields, processors);
			}
			//
			// Catch all possible fails when rendering super csv export.
			//
		} catch (Exception e) {
            throw SeedException.wrap(e, RendererErrorCode.SUPER_CSV_EXCEPTION)
                    .put("templateName", template.getName());
		} finally {
			closeQuietly(beanWriter);
		}
	}

	private void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
