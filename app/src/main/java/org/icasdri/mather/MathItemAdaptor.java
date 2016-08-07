package org.icasdri.mather;

import android.support.v7.widget.RecyclerView;
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
                    MathItemAdaptor.this.frag.injectUserInput(
                        ViewHolder.this.item.getResult().text
                    );
                }
            });
        }

        public void updateFrom(MathItem item) {
            this.inputView.setText(item.getInput());
            MathParser.Result result;
            if ((result = item.getResult()) == null) {
                this.resultView.setVisibility(View.GONE);
            } else {
                switch (result.resultType) {
                    case ANS:
                        this.resultView.setVisibility(View.VISIBLE);
                        this.resultView.setText(result.text);
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
