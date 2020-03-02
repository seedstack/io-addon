/*
 * Copyright © 2013-2020, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.jasper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.io.Render;
import org.seedstack.io.Renderer;
import org.seedstack.io.jasper.fixtures.CustomerBean;
import org.seedstack.seed.testing.junit4.SeedITRunner;


import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

@RunWith(SeedITRunner.class)
public class CustomXlsRendererIT {
    @Render("myCustomXlsRenderer")
    private Renderer renderer;
    private final CustomerBean john = new CustomerBean("1", "John", "Dunbar",
            new GregorianCalendar(1945, Calendar.JUNE, 13).getTime(),
            "1600 Amphitheatre Parkway\nMountain View, CA 94043\nUnited States", true, 0,
            "\"May the Force be with you.\" - Star Wars", "jdunbar@gmail.com", 0L);

    @Test
    public void test() throws Exception {
        renderer.render(new ByteArrayOutputStream(), john);
    }

}
