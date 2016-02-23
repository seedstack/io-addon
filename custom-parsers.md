---
title: "Custom parsers"
name: "Document import/export"
zones:
    - Addons
tags:
    - "parser"
    - "document"
    - "import"
    - "template"
menu:
    IOAddon:
        weight: 30
---

If available parsers don't fit your needs, the IO add-on provide an SPI for custom parsers. There are three options to
provide your own parser: static template, dynamic template or without template.

# Static template

In this case, templates are loaded from files within the `META-INF/templates` directory. You need to extend three classes:
{{< java "org.seedstack.io.spi.AbstractBaseStaticTemplateLoader" >}}, {{< java "org.seedstack.io.spi.AbstractBaseTemplate" >}} and
{{< java "org.seedstack.io.spi.AbstractTemplateParser" >}}.

- The template loader loads the template from the corresponding resource in `META-INF/templates` directory.
- The template have all information necessary to parse a file.
- The parser is able to parse an InputStream and produce a model, using the template information.

# Dynamic template

In the case of a dynamic template, your loader will completely handle the loading logic. Implement the {{< java "org.seedstack.io.spi.TemplateLoader" >}}
interface:

	public class MyDynamicTemplateLoader implements TemplateLoader<MyTemplate> {

		@Override
		public MyTemplate load(String name) throws Exception {
			// Gets your template from anywhere
			return myTemplate
		}

		@Override
		Set<String> names() {
			// Returns all the templates you know
			return names;
		}

		@Override
		boolean contains(String name) {
			// Checks if you know this template
			return bool;
		}

		@Override
		public String templateRenderer() {
			// Returns the name of the associated renderer if exists, null otherwise
			return "MyTemplateRenderer";
		}

		@Override
		public String templateParser() {
			// Returns the name of the associated parser
			return "MyTemplateParser";
		}

	}

# Without template

A parser without template doesn't need any information to parse the model. It is often the case of specific parsers
that are not meant to be reusable. Extend {{< java "org.seedstack.io.spi.AbstractBaseParser" >}} and annotate it
with {{< java "javax.inject.Named" "@" >}}:

	@Named("custom")
	public class CustomParser<PARSED_OBJECT> extends AbstractBaseParser<PARSED_OBJECT> {

		public CustomParser() {
		}

		@Override
		public List<PARSED_OBJECT> parse(InputStream inputStream, Class<PARSED_OBJECT> clazz) {
            List<PARSED_OBJECT> beans = new ArrayList<PARSED_OBJECT>();
			return beans;
		}

	}

You can inject it as usual:

	@Parse("custom")
	Parser parser;
