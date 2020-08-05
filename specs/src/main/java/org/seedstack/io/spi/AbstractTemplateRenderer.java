/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.spi;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.seedstack.io.Renderer;

/**
 * This class should be extended when you want to add a <code>Renderer</code> with template. <br>
 * It implements the {@link Renderer} interface
 *
 * @param <T> template
 */
public abstract class AbstractTemplateRenderer<T> implements Renderer {

    /**
     * Renderer template
     */
    @SuppressFBWarnings(value = "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD", justification = "used in subclasses")
    protected T template;

    /**
     * @param template template to set
     */
    public void setTemplate(T template) {
        this.template = template;
    }
}
