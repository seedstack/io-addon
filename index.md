---
title: "Basics"
name: "Document import/export"
repo: "https://github.com/seedstack/io-addon"
author: "SeedStack"
description: "Provides a simple way to import and export data in multiple formats: CSV, PDF, Office documents, ..."
min-version: "15.11+"
backend: true
weight: -1
tags:
    - "document"
    - "export"
    - "import"
    - "csv"
    - "pdf"
zones:
    - Addons
menu:
    IOAddon:
        weight: 10
---

The IO add-on gives simple way to export and import data in multiple formats. This add-on comes with two modules:

 * CSV through [SuperCSV](http://super-csv.github.io/super-csv),
 * JasperReports.

To use the IO add-on, add the module `io-supercsv` and/or the module `io-jasper` to your project classpath.

{{< dependency g="org.seedstack.addons.io" a="io-???" >}}

# Writing CSV files

To export a POJO to a CSV file, make sure the `io-supercsv` module is in your classpath. We will export
the following POJO:

	public class CustomerBean {
	
	    private String firstName;
	    
	    private String lastName;
	    
		private int age;

	}

Add a `customerbean.csv.properties` file in `META-INF/templates` directory:

	columns=firstName,lastName,age
	firstName.name=First name
	lastName.name=Last name
	age.name=Age
	age.type=int


In your code, inject a renderer:

	@Render("customerbean")
	private Renderer renderer;
	
	private List<CustomerBean> customers;
	
	public void exportCustomers(OuputStream os) {
	    renderer.render(os, customers);
	}

Or programatically obtain the required renderer:

	@Inject
	private Renderers renderers;

	public void exportCustomers(OuputStream os, String name) {
		Renderer renderer = renderers.getRendererFor(name);
	    renderer.render(os, customers);
	}

# Reading CSV files

To import the POJO, the configuration is the same as export configuration. Then inject a `Parser` with the `@Parse` annotation:

	@Parse("customerbean")
	private Parser<CustomerBean> parser;
	
	private List<CustomerBean> customers;
	
	public void importCustomers(InputStream is) {
	    customers = parser.parse(is, CustomerBean.class);
	} 

Or use `Parsers` to programatically obtain the required parser:
  
	@Inject
	private Parsers parsers;

	public void importCustomers(InputStream is, String name) {
		Parser parser = parsers.getParserFor(name);
	    renderer.render(os, customers);
		customers = parser.parse(is, CustomerBean.class);
	}

# Writing PDF files

PDF files are generated with JasperReports. Make sure to have the `io-jasper` module in your classpath and put a JRXML
file in `META-INF/templates` directory. Example:

	@Render("pdftemplate")
	Renderer renderer;
	
	List<CustomerBean> customers;
	
	public void exportCustomers(OuputStream os) {
	    renderer.render(os, customers, "application/pdf", parameters);
	}

You can pass any Jasper parameter (like `SUBREPORT_DIR`) using the fourth parameter or `render()`which is a
`Map<String, Object>`.

{{% callout info %}}
The Jasper module does not provide a parser.
{{% /callout %}}
