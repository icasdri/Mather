"use strict";

/*
 * Copyright 2017 icasdri
 *
 * This file is part of Mather. The original source code for Mather can be
 * found at <https://github.com/icasdri/Mather>. See COPYING for licensing
 * details.
 */

var parser = null;
var formatArgs = {
    precision: 11,
    exponential: {lower: 1e-4, upper: 1e+7}
};

function evaluate(expr) {
    if (parser == null) {
        parser = math.parser();
    }

    var result;
    try {
        result = parser.eval(expr);
    } catch (e) {
        return "e" + e.toString();
    }

    if (typeof result == "string") {
        return "s" + result;
    } else if (typeof result == "function") {
        var resultString = result.toString();
        return "f" + resultString.substring(9, resultString.indexOf('{')).trim();
    } else if (result == null) {
        return "n"
    } else {
        return "s" + math.format(result, formatArgs);
    }
}

function clear() {
    if (parser != null) {
        try {
            parser.clear();
        } catch (e) {
            return "Clearing was not successful: " + e.toString();
        }

        if (Object.keys(parser.getAll()).length !== 0) {
            return "Clearing was not fully successful."
        }

    }

    return "";
}
