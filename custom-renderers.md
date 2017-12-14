---
title: "Custom renderers"
addon: "Document I/O"
repo: "https://github.com/seedstack/io-addon"
zones:
    - Addons
weight: -1    
menu:
    Document I/O:
        parent: "contents"
        weight: 20
---

If available renderers don't fit your needs, the IO add-on provides an SPI to register your custom renderers. There are
three options to provide your own renderer: static template, dynamic template or without template.<!--more-->

# Static template

In this case, templates are loaded from files within the `META-INF/templates` directory. You need to extend three classes:
{{< java "org.seedstack.io.spi.AbstractBaseStaticTemplateLoader" >}}, {{< java "org.seedstack.io.spi.AbstractBaseTemplate" >}} and
{{< java "org.seedstack.io.spi.AbstractTemplateRenderer" >}}.

- The template loader loads the template from the corresponding resource in `META-INF/templates` directory.
- The template have all information necessary to render a model.
- The renderer is able to render a model into an OutputStream, using the template information.

# Dynamic template

In the case of a dynamic template, your loader will completely handle the loading logic. Implement the {{< java "org.seedstack.io.spi.TemplateLoader" >}}
interface:

```java
public class MyDynamicTemplateLoader implements TemplateLoader<MyTemplate> {
    @Override
    public MyTemplate load(String name) throws Exception {
        // Gets your template from anywhere
        return myTemplate;
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
        // Returns the name of the associated renderer
        return "MyTemplateRenderer";
    }
}
```

# Without template

A renderer without template doesn't need any information to render the model. It is often the case of specific renderers
that are not meant to be reusable. Extend {{< java "org.seedstack.io.spi.AbstractBaseRenderer" >}} and annotate it
with {{< java "javax.inject.Named" "@" >}}.

```java
@Named("custom")
public class CustomRenderer extends AbstractBaseRenderer {
    @Override
    public void render(OutputStream outputStream, Object model) {
        render(outputStream, model, null, null);
    }

    @Override
    public void render(OutputStream outputStream, Object model, String mimeType, Map<String, Object> parameters) {
        try {
            outputStream.write("Hello World!".getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

You can inject it as usual:

```java
public class SomeClass {
	@Renderer("custom")
	private Renderer renderer;
}
```
