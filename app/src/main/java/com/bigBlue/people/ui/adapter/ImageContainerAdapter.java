package com.bigBlue.people.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bigBlue.people.R;
import com.bigBlue.people.model.ImageContainer;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The type Image container adapter.
 */
public class ImageContainerAdapter extends RecyclerView.Adapter<ImageContainerAdapter.BaseViewHolder> {

    private final int HEADER_VIEW_TYPE = 121;
    private final int VIEW_HOLDER_TYPE = 122;

    private Context context;
    private AdapterClickHandler callback;
    private List<ImageContainer> imageContainers;

    /**
     * Instantiates a new Image container adapter.
     *
     * @param context         the context
     * @param imageContainers the image containers
     */
    public ImageContainerAdapter(Context context, List<ImageContainer> imageContainers) {
        this.context = context;
        this.imageContainers = imageContainers;
    }

    /**
     * Sets callback.
     *
     * @param callback the callback
     */
    public void setCallback(AdapterClickHandler callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder;
        if (viewType == HEADER_VIEW_TYPE) {
            viewHolder = new HeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_header, parent, false));
        } else {
            viewHolder = new ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image_container, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        try {
            if (holder instanceof HeaderViewHolder) {
                HeaderViewHolder viewHolder = (HeaderViewHolder) holder;

            } else if (holder instanceof ImageViewHolder) {
                ImageViewHolder viewHolder = (ImageViewHolder) holder;

                Glide.with(context).load(imageContainers.get(position).getImageUri())
                        .placeholder(R.drawable.ic_account).error(R.drawable.ic_account).into(viewHolder.mServices);
            }
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return imageContainers.size();
    }

    @Override
    public int getItemViewType(int position) {
        return imageContainers.get(position).getImageType() == 1 ? HEADER_VIEW_TYPE : VIEW_HOLDER_TYPE;
    }

    /**
     * The type Base view holder.
     */
    class BaseViewHolder extends RecyclerView.ViewHolder {

        /**
         * Instantiates a new Base view holder.
         *
         * @param itemView the item view
         */
        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     * The type Image view holder.
     */
    class ImageViewHolder extends BaseViewHolder {

        /**
         * The M services.
         */
        @BindView(R.id.img_services)
        ImageView mServices;

        /**
         * Instantiates a new Image view holder.
         *
         * @param itemView the item view
         */
        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) callback.onItemsClicked(getAdapterPosition());
                }
            });
        }
    }

    /**
     * The type Header view holder.
     */
    public class HeaderViewHolder extends BaseViewHolder {
        /**
         * Instantiates a new Header view holder.
         *
         * @param itemView the item view
         */
        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) callback.onHeaderClicked(getAdapterPosition());
                }
            });
        }
    }
}
