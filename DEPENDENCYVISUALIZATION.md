# Visualization of Dependencies 

During the generation stage, a pair of auxiliary files is created to help visualizing dependencies
between modules. The information they contain is similar but they are presented in a different way.
The files are generated in the root of the project folder. 

## Dependency Graph

This representation shows all modules and its dependencies as a directed graph in which
modules are represented by nodes and dependencies by edges between them. It is stored in the
dependencies.dot file and can be can be used with [Graphviz](https://www.graphviz.org/). This is an
example of the generated graph: 

![Dependency Graph](https://github.com/borisf/java-generator/blob/master/img/dependencies.dot.png) 

## Adjacency Matrix

For projects with many inter module dependencies it can be difficult to visualize the dependencies
as a graph and it can be convenient to use an
[adjacency matrix](https://en.wikipedia.org/wiki/Adjacency_matrix). The file dependencies.png
contains an image with it. The colors on the header indicates what type of module it is:

  - Blue: App.
  - Green: Android module.
  - Yellow: Non-android module.
  
The cells in a particular row indicates the dependencies for that module, while the cell in a column
indicate what other modules depoend on it. The type of dependency is also encoded using a color,
based on [Gradle documentation](https://docs.gradle.org/current/userguide/java_library_plugin.html#sec:java_library_configurations_graph):
  - Black: No dependency.
  - Green: user declared dependencies.
  - Pink: used when a component compiles, or runs against the library.
  - Blue: internal to the component, for its own use.
  - White: inherited from the Java plugin.
  - Purple: two or more configurations from the [Java library plugin configurations](https://docs.gradle.org/current/userguide/java_library_plugin.html#sec:java_library_configurations_graph).
  - Red: one or more configurations, with at least one not from the Java library plugin.

This is the generated image for the project in the example graph: 

![Adjacency Matrix](https://github.com/borisf/java-generator/blob/master/img/dependencies.png) 


