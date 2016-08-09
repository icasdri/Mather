# Mather

**Mather** is a powerful, simple-to-use, no BS expression-based computation engine for Android. In addition to basic calculator arithmetic, it supports variables, user-defined functions, complex math expression evaluation, trigonometric functions, unit conversions (and more general unit arithmetic), complex numbers, matrices, and more. Powered by [Math.js](https://mathjs.org/).

#### Screenshots
![screenshot](https://cloud.githubusercontent.com/assets/9786418/17490863/f0bfd4fe-5d72-11e6-8595-c151bf64b818.png)

### Building

**Mather** uses the [Gradle](https://gradle.org) build system for compilation, dependency, and asset management (including its external dependency on *Math.js*). 

To build a debug version of the app, simply make sure you have a modern local Gradle installation, then run the following.  The build has been tested to work with the latest `Gradle 2.14`.

```
gradle build
```

Additionally, you may import the repo as a project into Android Studio just like any normal Android project.

### License

**Mather** is Free Software licensed under the GPLv3+ and makes use of components under different (GPL-compatible) licenses. See [COPYING](https://github.com/icasdri/Mather/blob/master/COPYING) for details.
