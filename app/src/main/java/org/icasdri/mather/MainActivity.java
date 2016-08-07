package org.icasdri.mather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MathParser parser;

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

        try {
            this.parser = new MathParser(this.getApplicationContext());
        } catch (MathParser.InitializationException e) {
            Toast errorToast = Toast.makeText(this.getApplicationContext(),
                                              e.getMessage(), Toast.LENGTH_LONG);
            errorToast.show();
        }

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
