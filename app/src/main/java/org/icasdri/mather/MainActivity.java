/*
 * Copyright 2016 icasdri
 *
 * This file is part of Mather. The original source code for Mather can be
 * found at <https://github.com/icasdri/Mather>. See COPYING for licensing
 * details.
 */

package org.icasdri.mather;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MathParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.parser = new MathParser(this.getApplicationContext());
        try {
            this.parser.initialize();
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

        switch (id) {
            case R.id.settings_action:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(intent);
                return true;
            case R.id.clear_action:
                this.clear();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sleepThreaded(final long millis, final Runnable after) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    // just continue if we're interrupted
                }

                MainActivity.this.runOnUiThread(after);
            }
        }).start();
    }

    void clear() {
        final MainActivityFragment frag = (MainActivityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_fragment);

        final ProgressDialog progressDialog = ProgressDialog.show(this, null, "Clearing...", true);
        this.parser.clear(new MathParser.Callback() {
            @Override
            public void processResult(final MathParser.Result result) {
                /* The javascript callback on the parser clearing likes to come back early for
                   some reason. We wait three and a half seconds for state to stabilize before
                   returning control to the user. Fragment clearing happens a second before that
                   to see more natural */

                MainActivity.this.sleepThreaded(2500, new Runnable() {
                    @Override
                    public void run() {
                        frag.clear();

                        MainActivity.this.sleepThreaded(1000, new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                if (result.resultType != MathParser.ResultType.CLEAR_COMPLETE) {
                                    Toast errorToast = Toast.makeText(
                                            MainActivity.this.getApplicationContext(),
                                            "An error may have occurred while clearing.",
                                            Toast.LENGTH_LONG
                                    );
                                    errorToast.show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
