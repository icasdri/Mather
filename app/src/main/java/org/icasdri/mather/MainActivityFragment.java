/*
 * Copyright 2016 icasdri
 *
 * This file is part of Mather. The original source code for Mather can be
 * found at <https://github.com/icasdri/Mather>. See COPYING for licensing
 * details.
 */

package org.icasdri.mather;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Fragment controlling the main recycler view.
 */
public class MainActivityFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private MathItemAdaptor adapter;
    private EditText mainInput;
    private Button evalButton;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.main_fragment, container, false);

        /* Main input field initialization */
        this.mainInput = (EditText) fragment.findViewById(R.id.main_input);

        this.mainInput.setInputType(InputType.TYPE_CLASS_TEXT
                + InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        this.mainInput.requestFocus();

        this.mainInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_GO:
                        MainActivityFragment.this.evaluateUserInput();
                        return true;
                    default:
                        return false;
                }
            }
        });

        /* Eval button initialization */
        this.evalButton = (Button) fragment.findViewById(R.id.main_input_eval_button);
        this.evalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityFragment.this.evaluateUserInput();
            }
        });

        /* Recycler view initialization */
        this.recyclerView = (RecyclerView) fragment.findViewById(R.id.main_recycler_view);

        this.layoutManager = new LinearLayoutManager(this.getContext());
        this.layoutManager.setStackFromEnd(true);
        this.recyclerView.setLayoutManager(layoutManager);

        this.adapter = new MathItemAdaptor(this);
        this.recyclerView.setAdapter(this.adapter);

        ItemTouchHelper touchHelper = new ItemTouchHelper(this.adapter.new TouchHelperCallback());
        touchHelper.attachToRecyclerView(this.recyclerView);

        return fragment;
    }

    void evaluateUserInput() {
        String input = this.mainInput.getText().toString();
        this.mainInput.setText("");

        MathItem item = new MathItem(input);
        this.adapter.add(item);
        item.eval(((MainActivity) getActivity()).parser);

        this.recyclerView.smoothScrollToPosition(this.adapter.getItemCount());
    }

    void injectUserInput(String s) {
        this.injectUserInput(s, -1, -1);
    }

    void injectUserInput(String s, int selIndex) {
        this.injectUserInput(s, selIndex, -1);
    }

    void injectUserInput(String s, int selStart, int selEnd) {
        Editable ed = this.mainInput.getText();
        int origLen = ed.toString().length();
        ed.append(s);

        if (selStart > 0 && selEnd > 0) {
            this.mainInput.setSelection(origLen + selStart, selEnd);
        } else if (selStart > 0) {
            this.mainInput.setSelection(origLen + selStart);
        }
    }

    void clear() {
        ((MainActivity) getActivity()).parser.clear(MathParser.doNothingCallback);
        this.adapter.clear();
    }
}
