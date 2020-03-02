/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.jasper.internal;

import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.seedstack.io.jasper.JasperTemplate;
import org.seedstack.io.spi.AbstractBaseStaticTemplateLoader;
import org.seedstack.seed.SeedException;

import java.net.URL;

class JasperStaticTemplateLoader extends AbstractBaseStaticTemplateLoader<JasperTemplate> {
    @Override
    public String templatePathRegex() {
        return "(.+)\\.jrxml";
    }

    @Override
    public JasperTemplate load(String name) {
        URL url = templateURLs.get(name);
        if (url != null) {
            JasperDesign jasperDesign;
            try {
                jasperDesign = JRXmlLoader.load(url.openStream());
            } catch (Exception e) {
                throw SeedException.wrap(e, JasperErrorCode.ERROR_LOADING_JASPER_REPORT)
                        .put("template", name)
                        .put("url", url.toExternalForm());
            }
            return new JasperTemplate(jasperDesign, url.getFile());
        } else {
            return null;
        }
    }

    @Override
    public String templateRenderer() {
        return "JasperRenderer";
    }

    @Override
    public String templateParser() {
        return null;
    }
}
