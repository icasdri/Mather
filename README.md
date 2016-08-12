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
* Matrices
* and more. 

#### Screenshots
![Screenshot 1](https://cloud.githubusercontent.com/assets/9786418/17631585/b69e4c9c-6092-11e6-9124-c509b97e035b.png) ![Screenshot 2](https://cloud.githubusercontent.com/assets/9786418/17631589/b85cc66c-6092-11e6-8adf-0a6e84aa6c5e.png) ![Screenshot 3](https://cloud.githubusercontent.com/assets/9786418/17631592/b9f1fcae-6092-11e6-9965-c24d72b84b6a.png)

#### Usage

Mather's interface is simple and intuitive. Simply type the expression you want to calculate and then hit the blue *eval* button to evaluate it. See the [Screenshots](#Screenshots) above for examples of supported expressions.

For input, Mather provides a compact calculator-like keypad with buttons for the numbers 0 through 9, the basic arithmetic operations, and various parentheses and brackets. Additionally, the tap of a button switches to the full Android keyboard so that you can easily type variable names, function names, unit names, etc.

As a shortcut to typing out calculations, tapping on any previous operation copies its contents to the input field. This makes it easy to edit previous operations and access previously defined variables.

To get rid of a previous calculation that may be cluttering your view, simply swipe to the left or right on it to "dismiss" it, like you would notifications. Finally, select `Clear` from the menu to clear all calculations and reset all defined variables and functions.


### Building

**Mather** uses the [Gradle](https://gradle.org) build system for compilation, dependency, and asset management (including its external dependency on *Math.js*). 

To build a debug version of the app, make sure you have a modern local Gradle installation in addition to the Android SDK and Build Tools. Then run the following.  The build has been tested to work with the latest `Gradle 2.14`, `Android SDK 25`, and `Build Tools 24`.

```
gradle build
```

Additionally, you may import the repo as a project into Android Studio just like any normal Android project.

### License

**Mather** is Free Software licensed under the GPLv3+ and makes use of components under different (GPL-compatible) licenses. See [COPYING](https://github.com/icasdri/Mather/blob/master/COPYING) for details.
