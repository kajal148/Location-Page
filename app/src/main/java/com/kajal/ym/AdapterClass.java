package com.kajal.ym;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {

    private Context context;
    private List<Uri> imageList;
    private Boolean isBrochure;

    public AdapterClass(Context context, List<Uri> imageList, Boolean isBrochure) {
        this.context = context;
        this.imageList= imageList;
        this.isBrochure = isBrochure;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        ImageButton removeBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.image);
            removeBtn = itemView.findViewById(R.id.removeImage);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        Glide.with(context)  //2
                .load(imageList.get(position))
                .into(holder.mImage);

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).removeImage(position,imageList);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}