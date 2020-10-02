package com.kajal.ym;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private EditText mSearchField;
    Context context;
    ArrayList<Places> arrayList = new ArrayList<>();
    RecyclerView mrecycleView;

    public SearchAdapter(Context context, ArrayList<Places> arrayList, EditText mSearchField,RecyclerView mrecycleView) {
        this.context =  context;
        this.arrayList = arrayList;
        this.mSearchField = mSearchField;
        this.mrecycleView = mrecycleView;
    }

    class SearchViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        RelativeLayout relativeLayout;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name_text);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.parent_layout);
        }

        public void onClick(final int position, final String name) {
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mSearchFeild.setText(name);
                    //mrecycleView.setVisibility(View.GONE);
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("place", name);
                    context.startActivity(intent);

                }
            });
        }
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.onClick(position, arrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
