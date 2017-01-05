/*
 * Copyright 2016-2017 icasdri
 *
 * This file is part of Mather. The original source code for Mather can be
 * found at <https://github.com/icasdri/Mather>. See COPYING for licensing
 * details.
 */

package org.icasdri.mather;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MathItemAdaptor extends RecyclerView.Adapter<MathItemAdaptor.ViewHolder> {
    private MainActivityFragment frag;
    private List<MathItem> list;

    public MathItemAdaptor(MainActivityFragment frag) {
        this.frag = frag;
        this.list = new ArrayList<>();
    }

    public void add(MathItem item) {
        this.list.add(item);
        this.notifyItemInserted(list.size());
    }

    public void remove(int position) {
        this.list.remove(position);
        this.notifyItemRemoved(position);
    }

    public void clear() {
        this.list.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.math_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        MathItem item = this.list.get(position);
        item.setChangeListener(vh);
        vh.updateFrom(item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TouchHelperCallback extends ItemTouchHelper.Callback {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return ItemTouchHelper.Callback.makeMovementFlags(
                    0,  // drag flags
                    ItemTouchHelper.START | ItemTouchHelper.END  // swipe flags
            );
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder vh, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder vh, int direction) {
            MathItemAdaptor.this.remove(vh.getAdapterPosition());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements MathItem.ChangeListener {
        private MathItem item;
        private TextView inputView;
        private TextView resultView;

        public ViewHolder(View v) {
            super(v);
            this.inputView = (TextView) v.findViewById(R.id.math_item_input_view);
            this.resultView = (TextView) v.findViewById(R.id.math_item_result_view);

            this.inputView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MathItemAdaptor.this.frag.injectUserInput(
                        ViewHolder.this.item.getInput()
                    );
                }
            });

            this.resultView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MathParser.Result result = ViewHolder.this.item.getResult();
                    String text = result.text;
                    int selStart = -1;
                    int selEnd = -1;
                    switch (result.resultType) {
                        case FUNCTION:
                            selStart = text.indexOf('(') + 1;
                            selEnd = text.indexOf(')');
                            break;
                    }
                    MathItemAdaptor.this.frag.injectUserInput(text, selStart, selEnd);
                }
            });
        }

        public void updateFrom(MathItem item) {
            this.inputView.setText(item.getInput());

            MathParser.Result result;
            if ((result = item.getResult()) == null) {
                this.resultView.setVisibility(View.GONE);
            } else {
                final int normalColor = this.inputView.getCurrentTextColor();

                this.resultView.setVisibility(View.VISIBLE);

                switch (result.resultType) {
                    case ANS:
                        this.resultView.setTypeface(Typeface.MONOSPACE, Typeface.NORMAL);
                        this.resultView.setTextColor(normalColor);
                        this.resultView.setText(result.text);
                        break;
                    case FUNCTION:
                        this.resultView.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
                        this.resultView.setTextColor(normalColor);
                        this.resultView.setText(result.text);
                        break;
                    case ERROR:
                        this.resultView.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                        this.resultView.setTextColor(Color.RED);
                        this.resultView.setText(result.text);
                        break;
                    case NONE:
                        this.resultView.setText("");
                        break;
                }
            }
            this.item = item;
        }

        @Override
        public void handleChange() {
            MathItemAdaptor.this.notifyItemChanged(this.getAdapterPosition());
        }
    }
}
