/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io;

/**
 * This class aggregates all the renderers associated to a key. In case of template renderers the key is defined by the template,
 * otherwise the key is defined by the <tt>Named</tt> annotation of the renderer.
 */
public interface Renderers {

    /**
     * Returns a renderer corresponding to a specific key.
     *
     * @param key The key associated to the renderer
     * @return Renderer
     */
    Renderer getRendererFor(String key);
}
