package org.icasdri.mather;

import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;

public class UserKeysAdapter extends BaseAdapter {
    private MainActivityFragment frag;

    JSONArray keys;

    private static final String[] defaultKeys = {
            "7", "8", "9", "/", "%", "DEL",
            "4", "5", "6", "*", "(", ")",
            "1", "2", "3", "-", "[", "]",
            "0", ".", "=", "+", ",", "EVAL"
    };

    public UserKeysAdapter(MainActivityFragment frag) {
        this.frag = frag;
        reload();
    }

    void reload() {
        SharedPreferences prefs = this.frag.getSharedPreferences();
        String rawKeysJson = prefs.getString("userkeys_arr", null);
        boolean needPopulateWithDefault = true;
        if (rawKeysJson != null) {
            try {
                this.keys = new JSONArray(rawKeysJson);
                needPopulateWithDefault = false;
            } catch (JSONException e) {
                // TODO: wrap exception and pass it higher (we're handling it too low here)
                Toast errorToast = Toast.makeText(this.frag.getActivity().getApplicationContext(),
                        "Failed to populate custom userkeys, populating default", Toast.LENGTH_LONG);
                errorToast.show();
            }
        }

        if (needPopulateWithDefault) {
            this.resetUserKeysToDefault();
        }
    }

    void resetUserKeysToDefault() {
        try {
            this.keys = new JSONArray(UserKeysAdapter.defaultKeys);
            this.applyChangesToUserKeys();
        } catch (JSONException e) {
            // this should not happen
            throw new RuntimeException(e);
        }
    }

    void applyChangesToUserKeys() {
        this.notifyDataSetChanged();
        SharedPreferences prefs = this.frag.getSharedPreferences();
        prefs.edit()
             .putString("userkeys_arr", this.keys.toString())
             .apply();
    }

    @Override
    public int getCount() {
        return keys.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return keys.getString(position);
        } catch (JSONException e) {
            // TODO: wrap exception and pass it higher (we're handling it too low here)
            Toast errorToast = Toast.makeText(this.frag.getActivity().getApplicationContext(),
                    "Failed to populate custom userkeys, populating default", Toast.LENGTH_LONG);
            errorToast.show();
            return "0";
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final String text = (String) this.getItem(position);

        View generalButtonView;
        if (text.equals("DEL") || text.equals("EVAL")) {
            ImageButton button;
            if (convertView instanceof ImageButton) {
                // if we can use a recycled one, do so
                button = (ImageButton) convertView;
            } else {
                button = (ImageButton) View.inflate(
                        this.frag.getContext(), R.layout.userkey_imagebutton, null);
            }

            switch (text) {
                case "DEL":
                    button.setImageResource(R.drawable.ic_backspace);
                    break;
                case "EVAL":
                    button.setImageResource(R.drawable.ic_send);
                    break;
            }

            generalButtonView = button;
        } else {
            Button button;
            if (convertView instanceof Button) {
                // if we can use a recycled one, do so
                button = (Button) convertView;
            } else {
                button = (Button) View.inflate(
                        this.frag.getContext(), R.layout.userkey_button, null);
            }
            button.setText(text);

            generalButtonView = button;
        }

        generalButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (text) {
                    case "DEL":
                        UserKeysAdapter.this.frag.userInputBackspace();
                        break;
                    case "EVAL":
                        UserKeysAdapter.this.frag.evaluateUserInput();
                        break;
                    default:
                        UserKeysAdapter.this.frag.injectUserInput(text);
                        break;
                }
            }
        });

        generalButtonView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean longClickConsumed = false;
                final SharedPreferences prefs = UserKeysAdapter.this.frag.getSharedPreferences();
                if (!prefs.getBoolean("pref_lock_userkeys", false)) {
                    UserKeysAdapter.this.showEditUserKeyDialog(position, prefs);
                    longClickConsumed = true;
                }
                return longClickConsumed;
            }
        });

        return generalButtonView;
    }

    void showEditUserKeyDialog(final int position, final SharedPreferences prefs) {
        new MaterialDialog.Builder(this.frag.getContext())
                .title(R.string.prompt_userkeys_edit_title)
                .content(R.string.prompt_userkeys_edit_desc)
                .input(null, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        try {
                            UserKeysAdapter.this.keys.put(position, input);
                            UserKeysAdapter.this.applyChangesToUserKeys();
                        } catch (JSONException e) {
                            // shouldn't happen, it's just a string
                            throw new RuntimeException(e);
                        }
                    }
                }).show();
    }
}
