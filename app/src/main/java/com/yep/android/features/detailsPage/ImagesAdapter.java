package com.yep.android.features.detailsPage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yep.android.R;
import com.yep.android.data.model.response.ImageUrl;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static timber.log.Timber.d;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {

    private ArrayList<ImageUrl> imageUrls;
    private Context context;

    @Inject
    ImagesAdapter() {

    }

    public void setImageUrls(Context context, ArrayList imageUrls){
        d("imageUrls >>> " + imageUrls);
        this.imageUrls = imageUrls;
        this.context = context;
    }


    @Inject
    DetailsPresenter detailsPresenter;


    @Override
    public ImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_detail_images, parent, false);
        return new ImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImagesViewHolder holder, int position) {
        d("onBindViewHolder >> imageUrls >> " + imageUrls);

//        Glide.with(context)
//                .load(imageUrls.get(position).getImageUrl())
//                .apply(RequestOptions.centerCropTransform())
//                .into(holder.imageView);

        Glide.with(context)
                .load(imageUrls.get(position).getImageUrl())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_image_area_grey600_24dp)
                        .centerCrop())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return imageUrls == null ? 0 : imageUrls.size();
    }

    public class ImagesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView) public ImageView imageView;

        ImagesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v ->
            {


            });

        }
    }

}
