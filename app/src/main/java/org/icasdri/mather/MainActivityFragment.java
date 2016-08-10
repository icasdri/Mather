/*
 * Copyright 2016 icasdri
 *
 * This file is part of Mather. The original source code for Mather can be
 * found at <https://github.com/icasdri/Mather>. See COPYING for licensing
 * details.
 */

package org.icasdri.mather;

import android.app.Activity;
import android.content.Context;
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
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import javax.net.ssl.ManagerFactoryParameters;

/**
 * Fragment controlling the main recycler view.
 */
public class MainActivityFragment extends Fragment {
    private EditText mainInput;

    private RecyclerView mainRecyclerView;
    private MathItemAdaptor mainAdapter;

    private GridView userKeysGridView;
    private UserKeysAdapter userKeysAdapter;

    private ViewGroup mainInputAndUserkeysWrapper;
    private ViewGroup mainInputWrapper;

    private ImageButton mainInputSwitcherButton;
    private boolean userKeysKeyboardInUse;

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

        InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        /* Main Input switcher button initialization */
        mainInputSwitcherButton = (ImageButton) fragment.findViewById(R.id.main_input_switcher_button);
        mainInputSwitcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.err.println("SWITCHER BUTTON CLICKED");
                MainActivityFragment.this
                        .useUserKeysKeyboard(!MainActivityFragment.this.userKeysKeyboardInUse);
            }
        });

        /* Recycler view initialization */
        this.mainRecyclerView = (RecyclerView) fragment.findViewById(R.id.main_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setStackFromEnd(true);
        this.mainRecyclerView.setLayoutManager(layoutManager);

        this.mainAdapter = new MathItemAdaptor(this);
        this.mainRecyclerView.setAdapter(this.mainAdapter);

        ItemTouchHelper touchHelper = new ItemTouchHelper(this.mainAdapter.new TouchHelperCallback());
        touchHelper.attachToRecyclerView(this.mainRecyclerView);

        /* User key buttons initialization */
        this.userKeysGridView = (GridView) fragment.findViewById(R.id.userkeys_gridview);
        this.userKeysAdapter = new UserKeysAdapter(this);
        this.userKeysGridView.setAdapter(this.userKeysAdapter);

        this.mainInputAndUserkeysWrapper = (ViewGroup) fragment.findViewById(R.id.main_input_and_userkeys_wrapper);
        this.mainInputWrapper = (ViewGroup) fragment.findViewById(R.id.main_input_wrapper);

        MainActivityFragment.this.userKeysKeyboardInUse = false;  // make different so change is triggered
        MainActivityFragment.this.useUserKeysKeyboard(true);

        final ViewTreeObserver observer = this.mainInputAndUserkeysWrapper.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                        MainActivityFragment.this.mainRecyclerView.getLayoutParams();
                params.bottomMargin = MainActivityFragment.this.mainInputAndUserkeysWrapper.getHeight();
            }
        });

        return fragment;
    }

    void useUserKeysKeyboard(boolean use) {
        if (use != this.userKeysKeyboardInUse) {
            InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (use) {
                this.mainInputSwitcherButton.setImageResource(R.drawable.ic_keyboard);
                this.userKeysGridView.setVisibility(View.VISIBLE);
                this.userKeysGridView.requestFocus();
                this.userKeysKeyboardInUse = true;
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                this.mainInputSwitcherButton.setImageResource(R.drawable.ic_dialpad);
                this.userKeysGridView.setVisibility(View.GONE);
                this.userKeysKeyboardInUse = false;
                this.mainInput.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) this.mainRecyclerView.getLayoutParams();
                params.bottomMargin = MainActivityFragment.this.mainInputWrapper.getHeight();
            }
        }
    }

    void evaluateUserInput() {
        String input = this.mainInput.getText().toString();
        this.mainInput.setText("");

        MathItem item = new MathItem(input);
        this.mainAdapter.add(item);
        item.eval(((MainActivity) getActivity()).parser);

        this.mainRecyclerView.smoothScrollToPosition(this.mainAdapter.getItemCount());
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
        this.mainAdapter.clear();
    }
}
