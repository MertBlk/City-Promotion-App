package com.example.a23210202036;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private ArrayList<String> images = new ArrayList<>();

    public void setImages(ArrayList<String> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder holder, int position) {
        String imageUrl = images.get(position);
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .apply(new RequestOptions().centerCrop())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SliderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sliderImageView);  // slider_item.xml'deki ImageView
        }
    }
}
