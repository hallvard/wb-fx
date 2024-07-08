# wb-fx

A workbench-style application shell built on java, javafx and quarkus-fx

The workbench provides a set of *views* organized in *tab groups*. Tabs may be dragged and docked so you can get a layout suitable for your task.

[Views may be linked](app/src/main/resources/markdown/views/linking-views.md) so one may provide data to another.
When a view is opened, it is automatically linked to other views that provide data it needs, but you can link or unlink manually, if needed (see below).

The workbench is a [JavaFX](https://openjfx.io/) application built on [Quarkus](https://quarkus.io/) with [quarkus-fx](https://github.com/quarkiverse/quarkus-fx).
Build it with `mvn install` and run with `mvn quarkus:dev -f app`.

## Documentation

Documentation is provided inside the application, using a markdown view.
The markdown files are mainly in the `app` module, in the `src/main/resources/markdown` folder, but some are in other modules.

Diagram are made with [plantuml](https://plantuml.com) and generated using `mvn plantuml:generate -f app`
