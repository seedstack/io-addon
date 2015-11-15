/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.supercsv;

import com.google.inject.Inject;
import org.seedstack.io.Render;
import org.seedstack.io.Renderer;
import org.seedstack.io.Renderers;
import org.seedstack.io.supercsv.fixture.CustomerBean;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.seedstack.seed.SeedException;
import org.seedstack.seed.it.SeedITRunner;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * @author pierre.thirouin@ext.mpsa.com
 *
 */
@RunWith(SeedITRunner.class)
public class SuperCSVRendererIT {
	/**
	 * Temporary folder to stock the file.
	 */
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private final String[] header = new String[] { "customerNo", "firstName", "lastName", "birthDate", "mailingAddress",
			"married", "numberOfKids", "favouriteQuote", "email", "loyaltyPoints" };

	private final CustomerBean john = new CustomerBean("1", "John", "Dunbar",
			new GregorianCalendar(1945, Calendar.JUNE, 13).getTime(),
			"1600 Amphitheatre Parkway\nMountain View, CA 94043\nUnited States", null, null,
			"\"May the Force be with you.\" - Star Wars", "jdunbar@gmail.com", 0L);

	private final CustomerBean bob = new CustomerBean("2", "Bob", "Down",
			new GregorianCalendar(1919, Calendar.FEBRUARY, 25).getTime(), "1601 Willow Rd.\nMenlo Park, CA 94025\nUnited States",
			true, 0, "\"Frankly, my dear, I don't give a damn.\" - Gone With The Wind", "bobdown@hotmail.com", 123456L);

	private final CellProcessor[] processors = new CellProcessor[] { new UniqueHashCode(), // customerNo (must be unique)
			new NotNull(), // firstName
			new NotNull(), // lastName
			new ParseDate("dd/MM/yyyy"), // birthDate
			new NotNull(), // mailingAddress
			new Optional(new ParseBool("yes", "no")), // married
			new Optional(new ParseInt()), // numberOfKids
			new NotNull(), // favouriteQuote
			new NotNull(), // email
			new LMinMax(0L, LMinMax.MAX_LONG) // loyaltyPoints
	};

	private List<CustomerBean> customers;

	@Render("pojo")
	private Renderer rendererDefault;

	@Render("pojo1")
	private Renderer rendererCustom;

	@Inject
	private Renderers renderer;

	/**
	 * Constructor.
	 */
	public SuperCSVRendererIT() {
	}

	/**
	 * Initializes the test.
	 */
	@Before
	public void setUp() {
		customers = new ArrayList<CustomerBean>();
		customers.add(john);
		customers.add(bob);
	}

	/**
	 * Writes and reads a customerBean in CSV.
	 */
	@Test
	public void testParseCSVFile() throws Exception {
        File file = write(rendererDefault, customers);

        List<CustomerBean> customers = read(file, "UTF-8", header, processors, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

        Assertions.assertThat(john).isEqualTo(customers.get(0));
        Assertions.assertThat(bob).isEqualTo(customers.get(1));
	}

	/**
	 * Tests the <tt>charsetName</tt> options.
	 */
	@Test
	public void testEncoding() throws Exception {
        File file = write(rendererDefault, customers);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        String line = reader.readLine();
        Assertions.assertThat(line).startsWith("Numéro");
        reader.close();
	}

	/**
	 * Tests the <tt>XXX.nullable</tt> csv option.
	 */
    @Test(expected = SeedException.class)
	public void testFieldNotNull() throws Exception {
        write(rendererDefault, new CustomerBean());
        fail("should have failed due to not null constraint violation");
	}

	/**
	 * Tests the <tt>XXX.unique</tt> csv option.
	 */
	@Test(expected = SeedException.class)
	public void testFieldUnique() throws Exception  {
        customers.add(new CustomerBean("1", "John", "Dunbar", new GregorianCalendar(1945, Calendar.JUNE, 13).getTime(),
                "1600 Amphitheatre Parkway\nMountain View, CA 94043\nUnited States", null, null,
                "\"May the Force be with you.\" - Star Wars", "jdunbar@gmail.com", 0L));

        write(rendererDefault, customers);
        fail("should have failed due to unique violation");
	}

	/**
	 * Tests the generals csv options.
	 */
	@Test
	public void testCSVFileWithCustomPref() throws Exception {
        File file = write(rendererCustom, customers);

        List<CustomerBean> customers = read(file, "UTF-8", header, processors, CsvPreference.STANDARD_PREFERENCE);
        Assertions.assertThat(john).isEqualTo(customers.get(0));
        Assertions.assertThat(bob).isEqualTo(customers.get(1));
	}

	/**
	 * Tests the generals csv options.
	 */
	@Test(expected=SeedException.class)
	public void catch_seed_exception_for_fake_template() {
		renderer.getRendererFor("fake").render(new ByteArrayOutputStream(), customers);
	}

	/**
	 * Write a CSV file.
	 *
	 * @param renderer
	 *            renderer used to render
	 * @param model
	 *            model to write
	 * @return a file
	 * @throws Exception
	 *             when the file can't be write
	 */
	public File write(Renderer renderer, Object model) throws Exception {
		File file = folder.newFile("pojo.csv");
		FileOutputStream fos = new FileOutputStream(file);
		renderer.render(fos, model);
		fos.close();
		return file;
	}

	/**
	 * Reads a CSV file with SuperCSV and returns a list of customer.
	 *
	 * @param file
	 *            file to read
	 * @param encoding
	 *            charset name
	 * @param header
	 *            header columns
	 * @param processors
	 *            list of cellProcessor
	 * @param prefs
	 *            general csv file preferences
	 * @return list of customer
	 * @throws Exception
	 *             throw when the file can't be read
	 */
	public List<CustomerBean> read(File file, String encoding, String[] header, CellProcessor[] processors, CsvPreference prefs) throws Exception {
		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new InputStreamReader(new FileInputStream(file), encoding), prefs);
			beanReader.getHeader(true);
			List<CustomerBean> customers = new ArrayList<CustomerBean>();
			CustomerBean customer;
			while ((customer = beanReader.read(CustomerBean.class, header, processors)) != null) {
				customers.add(customer);
			}
			return customers;
		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
	}
}
