package com.bigBlue.people.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bigBlue.people.R;
import com.bigBlue.people.model.BaseModel;
import com.bigBlue.people.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The type Base recycler view adapter.
 */
public class BaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseRecyclerHolder> {

    private Context context;
    private List<BaseModel> baseModels;
    private int adapterType;

    /**
     * Instantiates a new Base recycler view adapter.
     *
     * @param context     the context
     * @param baseModels  the base models
     * @param adapterType the adapter type
     */
    public BaseRecyclerViewAdapter(Context context, List<BaseModel> baseModels, int adapterType) {
        this.context = context;
        this.baseModels = baseModels;
        this.adapterType = adapterType;
    }

    @NonNull
    @Override
    public BaseRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseRecyclerHolder(LayoutInflater.from(context).inflate(R.layout.item_base_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerHolder holder, int position) {
        try {
            BaseModel baseModel = baseModels.get(position);


            if (adapterType == Constants.ADAPTER.COMMENT) {
                holder.mHead.setText(baseModel.getName());
                holder.mTail.setText(baseModel.getEmail());
            } else if (adapterType == Constants.ADAPTER.COMMENT_AND_BODY) {
                holder.mHead.setText(baseModel.getName());
                holder.mTail.setText(baseModel.getBody());
            } else if (adapterType == Constants.ADAPTER.POSTS) {
                holder.mHead.setText(baseModel.getTitle());
                holder.mTail.setText(baseModel.getBody());
            }
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return baseModels.size();
    }

    /**
     * The type Base recycler holder.
     */
    class BaseRecyclerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_head)
        TextView mHead;
        @BindView(R.id.txt_tail)
        TextView mTail;

        /**
         * Instantiates a new Base recycler holder.
         *
         * @param itemView the item view
         */
        BaseRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
