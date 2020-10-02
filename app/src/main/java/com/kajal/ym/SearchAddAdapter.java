package com.kajal.ym;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchAddAdapter extends RecyclerView.Adapter<SearchAddAdapter.MyViewHolder> {

    private Context context;
    private List<SearchAddClass> mSearchAddList;

    public SearchAddAdapter(Context context, List<SearchAddClass> mSearchAddList) {
        this.context = context;
        this.mSearchAddList = mSearchAddList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        EditText mSearchAddPlace, mSearchAddDistance;
        ImageButton mSearchAddBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mSearchAddPlace = itemView.findViewById(R.id.search_add_field);
            mSearchAddDistance = itemView.findViewById(R.id.search_add_distance);
            mSearchAddBtn = itemView.findViewById(R.id.search_add_img);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.search_add_activity, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        SearchAddClass text = mSearchAddList.get(position);

        Intent intent = ((Activity)context).getIntent();
        final String locationName = intent.getStringExtra("place");
        holder.mSearchAddPlace.setText(locationName);

        holder.mSearchAddPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty() && s.toString().length()>=3 && !s.toString().equalsIgnoreCase(locationName)){
                    Intent intent = new Intent(context,SearchActivity.class);
                    intent.putExtra("searchTerm", s.toString());
                    context.startActivity(intent);
                }

            }
        });





        //holder.removeFeature.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        ((MainActivity) context).removeFeature(position,mFeaturesList);
        //    }
        //});
    }

    @Override
    public int getItemCount() {
        return mSearchAddList.size();
    }
}


