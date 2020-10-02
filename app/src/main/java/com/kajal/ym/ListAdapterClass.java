package com.kajal.ym;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kajal.ym.R;

import java.util.List;

public class ListAdapterClass extends RecyclerView.Adapter<ListAdapterClass.MyViewHolder> {

    private Context context;
    private List<AddList> mFeaturesList;

    public ListAdapterClass(Context context, List<AddList> mFeaturesList) {
        this.context = context;
        this.mFeaturesList= mFeaturesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;
        ImageButton removeFeature;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.feature);
            removeFeature = itemView.findViewById(R.id.removeFeature);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.addlist, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        AddList text = mFeaturesList.get(position);
        holder.mTextView.setText(text.getService());

        holder.removeFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).removeFeature(position,mFeaturesList);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mFeaturesList.size();
    }
}