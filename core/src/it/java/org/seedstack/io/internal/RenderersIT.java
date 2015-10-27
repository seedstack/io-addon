/**
 * Copyright (c) 2013, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.internal;

import com.google.inject.Inject;
import org.seedstack.io.api.Render;
import org.seedstack.io.api.Renderer;
import org.seedstack.io.api.Renderers;
import org.seedstack.io.fixture.CustomRenderer;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.seed.it.SeedITRunner;

import java.io.ByteArrayOutputStream;

/**
 * @author pierre.thirouin@ext.mpsa.com
 */
@RunWith(SeedITRunner.class)
public class RenderersIT {

    @Inject
    Renderers renderers;

    @Render("custom")
    Renderer renderer;

    /**
     * Tests injection of <tt>Renderers</tt>
     */
    @Test
    public void renderers_injection_is_working() {
        Assertions.assertThat(renderers).isNotNull();
    }

    @Test
    public void render_custom_injection_is_working() {
        Assertions.assertThat(renderer).isNotNull();
        Assertions.assertThat(renderer).isInstanceOf(CustomRenderer.class);
        Assertions.assertThat(renderers.getRendererFor("custom")).isInstanceOf(CustomRenderer.class);
    }

    @Test
    public void renderer_is_working() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        renderer.render(baos, null);

        Assertions.assertThat(baos.toString("UTF-8")).isEqualTo("Hello World!");
    }
}
