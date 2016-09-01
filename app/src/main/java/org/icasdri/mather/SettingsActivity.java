/*
 * Copyright 2016 icasdri
 *
 * This file is part of Mather. The original source code for Mather can be
 * found at <https://github.com/icasdri/Mather>. See COPYING for licensing
 * details.
 */

package org.icasdri.mather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

public class SettingsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(this.getResources().getString(R.string.settings_title));
        this.setContentView(R.layout.settings_activity);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    public static class SettingsFragment extends PreferenceFragment {
        SharedPreferences getSharedPreferences() {
            return PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            this.addPreferencesFromResource(R.xml.preferences);

            final Preference resetUserKeysButton = this.findPreference("pref_reset_userkeys");
            resetUserKeysButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new MaterialDialog.Builder(SettingsFragment.this.getActivity())
                            .title(R.string.prompt_reset_userkeys_title)
                            .content(R.string.prompt_reset_userkeys_desc)
                            .positiveText(R.string.gen_reset)
                            .negativeText(R.string.gen_cancel)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    SharedPreferences prefs = SettingsFragment.this.getSharedPreferences();
                                    prefs.edit().remove("userkeys_arr").apply();
                                }
                            })
                            .show();
                    return true;
                }
            });
        }
    }
}
