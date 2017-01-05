/*
 * Copyright 2016-2017 icasdri
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
        NONE, ANS, ERROR, FUNCTION, INIT_ERROR, CLEAR_COMPLETE, CLEAR_ERROR
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

    private void evaluateJavascriptFromResource(int resourceId, ValueCallback<String> cb) {
        InputStreamReader reader = new InputStreamReader(
            this.context.getResources().openRawResource(resourceId)
        );

        StringBuilder jsBuilder = new StringBuilder();
        char[] buf = new char[1024];
        try {
            int read = 0;
            while ((read = reader.read(buf, 0, 1024)) > 0){
                jsBuilder.append(buf, 0, read);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to evaluate JavaScript from resources file.");
        }

        this.jsWebView.evaluateJavascript(jsBuilder.toString(), cb);
    }

    private void loadMathJsLibrary() {
        this.evaluateJavascriptFromResource(R.raw.math, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                MathParser.this.evaluateJavascriptFromResource(R.raw.helper, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        MathParser.this.initialized = true;
                    }
                });
            }
        });
    }

    public MathParser(Context context) {
        this.context = context;
    }

    public void initialize() {
        if (!this.initialized) {
            this.jsWebView = new WebView(this.context);
            this.jsWebView.getSettings().setJavaScriptEnabled(true);
            this.jsWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);

            this.loadMathJsLibrary();
        }
    }

    private String sanitizeJavaScriptReturnValue(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        } else {
            return value;
        }
    }

    public void eval(String expression, final Callback cb) {
        if (!this.initialized) {
            cb.processResult(new Result("Not Initialized!", ResultType.INIT_ERROR));
            return;
        }
        
        final String expr = String.format("evaluate('%s')", expression.replaceAll("'", "\\'"));

        this.jsWebView.evaluateJavascript(expr, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                value = sanitizeJavaScriptReturnValue(value);
                switch (value.charAt(0)) {
                    case 's':
                        cb.processResult(new Result(value.substring(1), ResultType.ANS));
                        break;
                    case 'f':
                        cb.processResult(new Result(value.substring(1), ResultType.FUNCTION));
                        break;
                    case 'n':
                        cb.processResult(new Result(null, ResultType.NONE));
                        break;
                    case 'e':
                        cb.processResult(new Result(value.substring(1), ResultType.ERROR));
                        break;
                }
            }
        });
    }

    public void clear(final Callback cb) {
        if (!this.initialized) {
            cb.processResult(new Result("Not Initialized!", ResultType.INIT_ERROR));
            return;
        }

        this.jsWebView.evaluateJavascript("clear()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String errorMessage) {
                errorMessage = sanitizeJavaScriptReturnValue(errorMessage);
                if (errorMessage.length() == 0) {
                    cb.processResult(new Result("Cleared.", ResultType.CLEAR_COMPLETE));
                } else {
                    cb.processResult(new Result(errorMessage, ResultType.CLEAR_ERROR));
                }
            }
        });
    }
}
