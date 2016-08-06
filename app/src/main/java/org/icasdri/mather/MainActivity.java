package org.icasdri.mather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private WebView jsWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText mainInput = (EditText) findViewById(R.id.main_input);
        mainInput.setInputType(InputType.TYPE_CLASS_TEXT
                + InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mainInput.requestFocus();

        mainInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_GO:
                        // TODO: actually implement
                        return true;
                    default:
                        return false;
                }
            }
        });

        this.jsWebView = new WebView(getApplicationContext());
        this.jsWebView.getSettings().setJavaScriptEnabled(true);
        this.jsWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        InputStreamReader reader = new InputStreamReader(
                getResources().openRawResource(R.raw.math)
        );

        StringBuilder builder = new StringBuilder();
        char[] buf = new char[100];
        try {
            int read = 0;
            while ((read = reader.read(buf, 0, 100)) > 0){
                builder.append(buf, 0, read);
            }
        } catch (IOException e) {
            // TODO: handle exception with e.g. Toast
            throw new RuntimeException(e);
        }

        String mathjs = builder.toString();
        ValueCallback<String> doNothingCallback = new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                // do nothing, there should be no value
            }
        };

        this.jsWebView.evaluateJavascript(mathjs, doNothingCallback);
        this.jsWebView.evaluateJavascript("var parser = math.parser()", doNothingCallback);
        this.jsWebView.evaluateJavascript("parser.eval('f(x) = x + 2')", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                mainInput.setText(value);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings_action) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
