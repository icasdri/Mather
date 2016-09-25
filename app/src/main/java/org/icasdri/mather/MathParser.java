/*
 * Copyright 2016 icasdri
 *
 * This file is part of Mather. The original source code for Mather can be
 * found at <https://github.com/icasdri/Mather>. See COPYING for licensing
 * details.
 */

package org.icasdri.mather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStreamReader;

@SuppressLint("SetJavaScriptEnabled")
public class MathParser {

    private Context context;
    private WebView jsWebView;
    private boolean initialized;

    public enum ResultType {
        NONE, ANS, ERROR, FUNCTION, INIT_ERROR, CLEAR_COMPLETE
    }

    public static class Result {
        String text;
        ResultType resultType;

        public Result(String text, ResultType resultType) {
            this.text = text;
            this.resultType = resultType;
        }
    }

    public interface Callback {
        void processResult(Result result);
    }

    public static final Callback doNothingCallback = new Callback() {
        @Override public void processResult(Result result) { /* do nothing */ }
    };

    public class InitializationException extends Exception {
        public InitializationException(String message) {
            super(message);
        }
    }

    private void loadMathJsLibrary() throws InitializationException {
        InputStreamReader reader = new InputStreamReader(
                this.context.getResources().openRawResource(R.raw.math)
        );

        StringBuilder mathjsBuilder = new StringBuilder();
        char[] buf = new char[100];
        try {
            int read = 0;
            while ((read = reader.read(buf, 0, 100)) > 0){
                mathjsBuilder.append(buf, 0, read);
            }
        } catch (IOException e) {
            throw new InitializationException("Failed to initialize math evaluation library.");
        }

        this.jsWebView.evaluateJavascript(mathjsBuilder.toString(), new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value1) {
                MathParser.this.jsWebView.evaluateJavascript("var parser = math.parser()",
                        new ValueCallback<String>() {

                    @Override
                    public void onReceiveValue(String value2) {
                        initialized = true;
                    }

                });
            }
        });
    }

    public MathParser(Context context) {
        this.context = context;
    }

    public void initialize() throws InitializationException {
        if (!this.initialized) {
            this.jsWebView = new WebView(this.context);
            this.jsWebView.getSettings().setJavaScriptEnabled(true);
            this.jsWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);

            this.loadMathJsLibrary();
        }
    }

    public void eval(String expression, final Callback cb) {
        if (!this.initialized) {
            cb.processResult(new Result("Math evaluation library not initialized!",
                                        ResultType.INIT_ERROR));
            return;
        }
        
        final String mathjsExpression = String.format("parser.eval('%s').toString()", expression);
        this.jsWebView.evaluateJavascript(mathjsExpression, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                if (value.length() == 0 || "null".equals(value)) {
                    cb.processResult(new Result(null, ResultType.NONE));
                } else if (value.startsWith("function")){
                    // capture the function name and args and return it
                    String ret = value.substring(9, value.indexOf('{')).trim();
                    cb.processResult(new Result(ret, ResultType.FUNCTION));
                } else {
                    cb.processResult(new Result(value, ResultType.ANS));
                }
            }
        });
    }

    public void clear(final Callback cb) {
        if (!this.initialized) {
            cb.processResult(new Result("Math evaluation library not initialized!",
                    ResultType.INIT_ERROR));
            return;
        }

        this.jsWebView.evaluateJavascript("parser.clear()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                cb.processResult(new Result(null, ResultType.CLEAR_COMPLETE));
            }
        });
    }
}
