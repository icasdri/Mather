# Mather


**Mather** is a powerful, simple-to-use, no BS expression-based computation engine for Android. Powered by [Math.js](https://mathjs.org/). 

[![F-Droid](https://f-droid.org/wiki/images/0/06/F-Droid-button_get-it-on.png)](https://f-droid.org/repository/browse/?fdid=org.icasdri.mather)

In addition to basic calculator arithmetic, Mather supports...

* Variables
* User-defined functions
* Complex math expression evaluation
* Trigonometric functions
* Unit conversions (and more general unit arithmetic)
* Complex numbers
* Matrices, and more. 

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
