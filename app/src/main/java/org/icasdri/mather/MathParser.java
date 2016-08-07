package org.icasdri.mather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStreamReader;

public class MathParser {

    private Context context;
    private WebView jsWebView;

    private static final ValueCallback<String> doNothingValueCallback = new ValueCallback<String>() {
        @Override public void onReceiveValue(String value) { /* do nothing */ }
    };

    public interface Callback {
        void processResult(String result);
        void processCompleted();
    }

    public static final Callback doNothingCallback = new Callback() {
        @Override public void processResult(String result) { /* do nothing */ }
        @Override public void processCompleted() { /* do nothing */ }
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

        this.jsWebView.evaluateJavascript(mathjsBuilder.toString(), doNothingValueCallback);
        this.jsWebView.evaluateJavascript("var parser = math.parser()", doNothingValueCallback);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public MathParser(Context context) throws InitializationException {
        this.context = context;
        this.jsWebView = new WebView(this.context);
        this.jsWebView.getSettings().setJavaScriptEnabled(true);
        this.jsWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);

        this.loadMathJsLibrary();
    }

    public void initialize() {

    }

    public void eval(String expression, final Callback callback) {
        String mathjsExpression = String.format("parser.eval('%s')", expression);
        this.jsWebView.evaluateJavascript(mathjsExpression, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                if ("null".equals(value)) {
                    callback.processCompleted();
                } else {
                    callback.processResult(value);
                }
            }
        });
    }

    public void clear(final Callback callback) {
        this.jsWebView.evaluateJavascript("parser.clear()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                callback.processCompleted();
            }
        });
    }
}
