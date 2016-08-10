package org.icasdri.mather;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;

public class UserKeysAdapter extends BaseAdapter {
    private MainActivityFragment frag;

    final String[] keys = {
            "7", "8", "9", "/", "%", "DEL",
            "4", "5", "6", "*", "(", ")",
            "1", "2", "3", "-", "[", "]",
            "0", ".", "=", "+", ",", "EVAL"
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

        return generalButtonView;
    }
}
