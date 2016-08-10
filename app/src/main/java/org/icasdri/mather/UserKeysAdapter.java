package org.icasdri.mather;

import android.app.ActionBar;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

public class UserKeysAdapter extends BaseAdapter {
    private MainActivityFragment frag;

    final String[] keys = {
            "7", "8", "9", "/", "", "DEL",
            "4", "5", "6", "*", "(", ")",
            "1", "2", "3", "-", "[", "]",
            "0", ".", ",", "+", "%", "EVAL"
    };

    public UserKeysAdapter(MainActivityFragment frag) {
        this.frag = frag;
    }

    @Override
    public int getCount() {
        return keys.length;
    }

    @Override
    public Object getItem(int position) {
        return keys[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        final FrameLayout button;
        if (convertView == null) {
            // if it's not recycled, create it
            button = (FrameLayout) View.inflate(this.frag.getContext(), R.layout.userkey_button, null);
        } else {
            // if it's recycled, use it
            button = (FrameLayout) convertView;
        }

        final TextView internalTextView = (TextView) button.findViewById(R.id.userkey_button_internal_text);
        String text = (String) this.getItem(position);


        int sz; // resource id of padding to be set
        switch (text) {
            case "DEL":
                internalTextView.setBackgroundResource(R.drawable.ic_backspace);
                sz = R.dimen.userkey_with_icon_size;
                break;
            case "EVAL":
                internalTextView.setBackgroundResource(R.drawable.ic_send);
                sz = R.dimen.userkey_with_icon_size;
                break;
            default:
                internalTextView.setText(text);
                sz = R.dimen.userkey_normal_size;
                break;
        }

        {
            int n = (int) this.frag.getResources().getDimension(sz);
            internalTextView.setWidth(n);
            internalTextView.setHeight(n);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = internalTextView.getText().toString();
                switch (text) {
                    case "DEL":
                        // TODO: handle DEL userkey
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
        return button;
    }
}
