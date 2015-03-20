/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io;

import org.seedstack.io.api.Render;
import org.seedstack.io.api.Renderer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.seed.it.SeedITRunner;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author pierre.thirouin@ext.mpsa.com
 *
 */
@RunWith(SeedITRunner.class)
public class CustomXlsRendererIT {
	
	@Render("myCustomXlsRenderer")
	Renderer renderer;
	
	final CustomerBean john = new CustomerBean("1", "John", "Dunbar",
			new GregorianCalendar(1945, Calendar.JUNE, 13).getTime(),
			"1600 Amphitheatre Parkway\nMountain View, CA 94043\nUnited States", true, 0,
			"\"May the Force be with you.\" - Star Wars", "jdunbar@gmail.com", 0L);
	
	/**
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		renderer.render(new ByteArrayOutputStream(), john);
	}

}
