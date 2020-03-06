/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io;

import com.google.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.io.fixture.CustomRenderer;
import org.seedstack.seed.testing.junit4.SeedITRunner;

import java.io.ByteArrayOutputStream;

@RunWith(SeedITRunner.class)
public class RenderersIT {

    @Inject
    Renderers renderers;

    @Render("custom")
    Renderer renderer;

    /**
     * Tests injection of <code>Renderers</code>
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
