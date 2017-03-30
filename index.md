---
title: "Document import/export"
repo: "https://github.com/seedstack/io-addon"
author: Pierre THIROUIN
description: "Provides a simple way to import and export data in multiple formats: CSV, PDF, Office documents, ..."
zones:
    - Addons
menu:
    IOAddon:
        weight: 10
---

The IO add-on gives simple way to export and import data in multiple formats.<!--more--> 

# Dependencies

The IO add-on provides a module for importing and export CSV files with [SuperCSV](http://super-csv.github.io/super-csv/):

{{< dependency g="org.seedstack.addons.io" a="io-supercsv" >}}

It also provides a module for exporting with [JasperReports](http://community.jaspersoft.com/project/jasperreports-library):

{{< dependency g="org.seedstack.addons.io" a="io-jasper" >}}

# CSV files

## Writing

To export a POJO to a CSV file, make sure the `io-supercsv` module is in your classpath. We will export
the following POJO:

```java
public class CustomerBean {
    private String firstName;
    private String lastName;
    private int age;
}
```

Add a `customerbean.csv.properties` file in `META-INF/templates` directory:

```properties
columns=firstName,lastName,age
firstName.name=First name
lastName.name=Last name
age.name=Age
age.type=int
```


In your code, inject a renderer:

```java
public class SomeClass {
	@Render("customerbean")
	private Renderer renderer;
	
	private List<CustomerBean> customers;
	
	public void exportCustomers(OuputStream os) {
	    renderer.render(os, customers);
	}
}
```

Or programmatically obtain the required renderer:

```java
public class SomeClass {
	@Inject
	private Renderers renderers;

	public void exportCustomers(OuputStream os, String name) {
		Renderer renderer = renderers.getRendererFor(name);
	    renderer.render(os, customers);
	}
}
```

## Reading

To import the POJO, the configuration is the same as export configuration. Then inject a `Parser` with the `@Parse` annotation:

```java
public class SomeClass {
	@Parse("customerbean")
	private Parser<CustomerBean> parser;
	
	private List<CustomerBean> customers;
	
	public void importCustomers(InputStream is) {
	    customers = parser.parse(is, CustomerBean.class);
	} 
}
```

Or use `Parsers` to programatically obtain the required parser:

```java
public class SomeClass {
	@Inject
	private Parsers parsers;

	public void importCustomers(InputStream is, String name) {
		Parser parser = parsers.getParserFor(name);
	    renderer.render(os, customers);
		customers = parser.parse(is, CustomerBean.class);
	}
}
```

# JasperReports

JasperReports can generate multiple file formats from a common JRXML template. One of the most useful format is PDF, as in
the following example:

```java
public class SomeClass {
	@Render("pdftemplate")
	private Renderer renderer;
	private List<CustomerBean> customers;
	
	public void exportCustomers(OuputStream os) {
	    renderer.render(os, customers, "application/pdf", parameters);
	}
}
```

You can pass any Jasper parameter (like `SUBREPORT_DIR`) using the fourth parameter or `render()`which is a
`Map<String, Object>`.

{{% callout info %}}
The Jasper module does not provide a parser.
{{% /callout %}}
