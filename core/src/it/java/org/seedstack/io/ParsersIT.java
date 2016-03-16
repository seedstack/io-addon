/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io;

import com.google.inject.Inject;
import org.seedstack.io.fixture.BeanDTO;
import org.seedstack.io.fixture.CustomParser;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.seed.it.SeedITRunner;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * @author pierre.thirouin@ext.mpsa.com
 *         Date: 25/03/14
 */
@RunWith(SeedITRunner.class)
public class ParsersIT {

    @Inject
    Parsers parsers;

    @Parse("custom")
    Parser<BeanDTO> parser;

    /**
     * Tests injection of <tt>Renderers</tt>
     */
    @Test
    public void parsers_injection_is_working() {
        Assertions.assertThat(parsers).isNotNull();
    }

    @Test
    public void parser_custom_injection_is_working() {
        Assertions.assertThat(parser).isNotNull();
        Assertions.assertThat(parser).isInstanceOf(CustomParser.class);
        Assertions.assertThat(parsers.getParserFor("custom")).isInstanceOf(CustomParser.class);
    }

    @Test
    public void parser_is_working() throws Exception {
        String input = "foo-bar";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input.getBytes());
        List<BeanDTO> beanDTOs = parser.parse(byteArrayInputStream, BeanDTO.class);
        Assertions.assertThat(beanDTOs.get(0).getA()).isEqualTo("foo");
        Assertions.assertThat(beanDTOs.get(0).getB()).isEqualTo("bar");
    }
}
