package com.kajal.ym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private EditText mSearchField;
    private ImageButton mSearchBtn;

    private RecyclerView mRecyclerView;

    private DatabaseReference databaseReference;

    ArrayList<Places> arrayList = new ArrayList<>();

    SearchAdapter searchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);

        String searchTerm = getIntent().getStringExtra("searchTerm");

        if(!searchTerm.isEmpty()) {
            mSearchField.setText(searchTerm);
            setAdapter(searchTerm);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.result_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //for search text change
        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("MyTag", "beforeTextChanged: starts");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("MyTag", "onTextChanged: starts");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("MyTag", "afterTextChanged: starts");
                if (!s.toString().isEmpty() && s.toString().length()>=3){
                    setAdapter(s.toString());
                } else {
                    arrayList.clear();
                    mRecyclerView.removeAllViews();
                }
            }
        });

        // for search button click
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = mSearchField.getText().toString();
                if (!s.toString().isEmpty()){
                    setAdapter(s.toString());
                }

            }
        });

    }

    private void setAdapter(final String searchString) {
        Log.d("MyTag", "setAdapter: starts");
        databaseReference.child("Coordinates").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("MyTag", "onDataChange: starts");

                //for every new search
                arrayList.clear();
                mRecyclerView.removeAllViews();

                int counter = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String name = snapshot.child("name").getValue(String.class);
                    if (name.toLowerCase().contains(searchString.toLowerCase())){
                        arrayList.add(new Places(name));
                        counter++;
                    }

                    //for the top 15 results
                    if (counter  == 15){
                        break;
                    }

                }

                if(arrayList.size() > 0) {
                    searchAdapter = new SearchAdapter(SearchActivity.this, arrayList, mSearchField, mRecyclerView);
                    mRecyclerView.setAdapter(searchAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("MyTag", "onCancelled: starts");
            }
        });
    }


}