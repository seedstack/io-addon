/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.supercsv;

import org.seedstack.io.api.Parse;
import org.seedstack.io.api.Parser;
import org.seedstack.io.api.Parsers;
import org.seedstack.io.fixture.CustomerBean;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.seed.it.SeedITRunner;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * @author pierre.thirouin@ext.mpsa.com
 *         Date: 25/03/14
 */
@RunWith(SeedITRunner.class)
public class SuperCSVParserIT {

    private static final String CSV_CONTENT = "Numéro client;Prénom;Nom;Date de naissance;Mailling;Marié;Nombre d'enfants;Citation favorite;Email;Points de fidélité\n" +
            "1;John;Dunbar;13/06/1945;\"1600 Amphitheatre Parkway\n" +
            "Mountain View, CA 94043\n" +
            "United States\";;;\"\"\"May the Force be with you.\"\" - Star Wars\";jdunbar@gmail.com;0\n" +
            "2;Bob;Down;25/02/1919;\"1601 Willow Rd.\n" +
            "Menlo Park, CA 94025\n" +
            "United States\";yes;0;\"\"\"Frankly, my dear, I don't give a damn.\"\" - Gone With The Wind\";bobdown@hotmail.com;123456\n";

    @Parse("pojo")
    private Parser<CustomerBean> parser;

    @Inject
    private Parsers parsers;

    @Test
    public void parser_are_injected () {
        Assertions.assertThat(parser).isNotNull();
        Assertions.assertThat(parsers.getParserFor("pojo")).isNotNull();
    }

    @Test
    public void parse_csv() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(CSV_CONTENT.getBytes());
        List<CustomerBean> customerBeans = parser.parse(byteArrayInputStream, CustomerBean.class);
        Assertions.assertThat(customerBeans.size()).isEqualTo(2);
    }

    @Test
    public void parse_csv_with_parsers() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(CSV_CONTENT.getBytes());
        Parser parser = parsers.getParserFor("pojo");
        List<CustomerBean> customerBeans = parser.parse(byteArrayInputStream, CustomerBean.class);
        Assertions.assertThat(customerBeans.size()).isEqualTo(2);
    }
}
