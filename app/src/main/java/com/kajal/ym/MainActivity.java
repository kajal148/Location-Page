package com.kajal.ym;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView mCountTextView;
    EditText mCountEditText;

    Spinner spinner_best_months;
    Spinner spinner_best_seasons;

    String[] days ={"1","2","3","4","5","6","7","8","9","10"};
    String[] nights ={"1","2","3","4","5","6","7","8","9","10"};

    RecyclerView gallery;
    ImageView mGalleryClick;

    RecyclerView addSearchRecycleView;
    List<SearchAddClass> mAddSearchList = new ArrayList<>();
    Button mSearchAddBtn;

    List<Uri> galleryList = new ArrayList<>();

    Dialog mDialog;
    EditText mEditField;
    String add_field;
    List<AddList> mFeatureList= new ArrayList<>();
    RecyclerView mFeatureRecyclerView;

    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    private EditText mSearchField;

    public final int GALLERY_SELECTION_REQUEST=5;

    Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDialog = new Dialog(this);
        spinner_best_months = findViewById(R.id.spinner_months);
        spinner_best_seasons = findViewById(R.id.spinner_seasons);

        mGalleryClick = findViewById(R.id.icon_gallery);
        gallery = findViewById(R.id.gallery);

        mSearchField = (EditText) findViewById(R.id.search_field);

        mCountEditText =  findViewById(R.id.edit_tripSubject);
        mCountTextView = findViewById(R.id.text_tripCounter);

        mCountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int length = mCountEditText.length();
                String convert = String.valueOf(length);
                if(length==0)
                {
                    mCountTextView.setText(" 0/100");
                }else{
                    mCountTextView.setText(" "+convert+"/100");
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });


        //getting search place from SEARCH ACTIVITY
        Intent intent = getIntent();
        final String locationName = intent.getStringExtra("place");
        mSearchField.setText(locationName);

        //for search text change
        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty() && s.toString().length()>=3 && !s.toString().equalsIgnoreCase(locationName)){
                    Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                    intent.putExtra("searchTerm", s.toString());
                    startActivity(intent);
                }
            }
        });

        //getting search place from SEARCHACTIVITY
        /*intent = getIntent();
        String name = intent.getStringExtra("place");
        mSearchField.setText(name);*/
                                                    // RECYCLE VIEW FOR GALLERY AND BROCHURE

        mGalleryClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imgIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imgIntent.setType("image/* video/*");
                imgIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                imgIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(imgIntent,"Select Photo"), GALLERY_SELECTION_REQUEST);
            }
        });

        LinearLayoutManager manager_gallery = new LinearLayoutManager(this);
        manager_gallery.setOrientation(LinearLayoutManager.HORIZONTAL);
        gallery.setLayoutManager(manager_gallery);

        loadImageViewGallery(galleryList);

                // ADD Extra SEARCH BAR
        addSearchRecycleView = findViewById(R.id.search_add_recycleView);

        LinearLayoutManager manager_addSearch = new LinearLayoutManager(this);
        manager_addSearch.setOrientation(LinearLayoutManager.VERTICAL);
        addSearchRecycleView.setLayoutManager(manager_addSearch);
        loadAddSearch(mAddSearchList);

        /*mSearchAddBtn = findViewById(R.id.search_add_btn);
        mSearchAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchAddClass item = new SearchAddClass();
                mAddSearchList.add(item);
                loadAddSearch(mAddSearchList);
            }
        });*/


                            //FEATURES
        mFeatureRecyclerView = findViewById(R.id.feature_list);

        LinearLayoutManager manager_feature = new LinearLayoutManager(this);
        manager_feature.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFeatureRecyclerView.setLayoutManager(manager_feature);
        loadFeature(mFeatureList);

        //BEST MONTHS
        List<String> best_months_list = new ArrayList<String>();
        best_months_list.add("Jan");
        best_months_list.add("Feb");
        best_months_list.add("Mar");
        best_months_list.add("Apr");
        best_months_list.add("May");
        best_months_list.add("Jun");
        best_months_list.add("Jul");
        best_months_list.add("Aug");
        best_months_list.add("Sept");
        best_months_list.add("Oct");
        best_months_list.add("Nov");
        best_months_list.add("Dec");

        ArrayAdapter<String> best_months_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, best_months_list);
        best_months_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_best_months.setAdapter(best_months_Adapter);

        spinner_best_months.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String month = parent.getItemAtPosition(position).toString();
                    //optional later remove it
                    //Toast.makeText(parent.getContext(),"Selected: " +item, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //BEST SEASONS
        List<String> best_seasons_list = new ArrayList<String>();
        best_seasons_list.add("Autumn");
        best_seasons_list.add("Monsoon");
        best_seasons_list.add("Pre-Winter");
        best_seasons_list.add("Spring");
        best_seasons_list.add("Summer");
        best_seasons_list.add("Winter");


        ArrayAdapter<String> best_seasons_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, best_seasons_list);
        best_seasons_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_best_seasons.setAdapter(best_seasons_Adapter);

        spinner_best_seasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String season = parent.getItemAtPosition(position).toString();
                //optional later remove it
                //Toast.makeText(parent.getContext(),"Selected: " +item, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //DAYS AND NIGHTS
        ArrayAdapter<String> days_adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,days);
        AutoCompleteTextView days_act =  (AutoCompleteTextView)findViewById(R.id.spinner_days);
        days_act.setThreshold(1);
        days_act.setAdapter(days_adapter);
        days_act.setTextColor(Color.BLACK);

        ArrayAdapter<String> nights_adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,nights);
        AutoCompleteTextView nights_act =  (AutoCompleteTextView)findViewById(R.id.spinner_nights);
        nights_act.setThreshold(1);
        nights_act.setAdapter(nights_adapter);
        nights_act.setTextColor(Color.BLACK);
    }

    protected void onActivityResult(int requestcode, int resultcode,
                                    Intent imagereturnintent) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent);
        switch(requestcode) {
            case GALLERY_SELECTION_REQUEST:
                if (resultcode == RESULT_OK) {
                    ClipData selectedImages = imagereturnintent.getClipData();
                    if(selectedImages != null) {
                        for(int count =0; count < selectedImages.getItemCount(); count++)
                        galleryList.add(selectedImages.getItemAt(count).getUri());
                    } else if(imagereturnintent.getData() != null){
                        galleryList.add(imagereturnintent.getData());
                    }
                   loadImageViewGallery(galleryList);
                }
                break;
        }
    }

    public void removeImage(int position, List<Uri> imageList) {
        imageList.remove(position);
        loadImageViewGallery(imageList);
    }

    private void loadImageViewGallery(List<Uri> imageList) {
        //if(imageList != null && imageList.size()>0) {
            AdapterClass adapter = new AdapterClass(this,imageList, false);
            gallery.setAdapter(adapter);
        //}
    }

    public void popUpFeature(View v){
        final Button mButton;
        mDialog.setContentView(R.layout.popup_text);
        mButton = mDialog.findViewById(R.id.btn_submit);
        mEditField = mDialog.findViewById(R.id.edit_services);
        //mEditField.setText("");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_field= mEditField.getText().toString();
                if(add_field.length()!=0){
                    mFeatureList.add(new AddList(add_field));
                    loadFeature(mFeatureList);
                }
                //Toast.makeText(getApplicationContext(), add_field, Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public void removeFeature(int position, List<AddList> featureList) {
        featureList.remove(position);
        loadFeature(featureList);
    }

    private void loadFeature(List<AddList> featureList) {
        //if(featureList != null && featureList.size()>0) {
            ListAdapterClass adapter = new ListAdapterClass(this, featureList);
            mFeatureRecyclerView.setAdapter(adapter);
        //}
    }

    private void loadAddSearch(List<SearchAddClass> mAddSearchList) {
        SearchAddAdapter adapter = new SearchAddAdapter(this, mAddSearchList);
        addSearchRecycleView.setAdapter(adapter);
    }

    //function to add extra search bar
    public void addSearchBar(View view) {
        mAddSearchList.add(new SearchAddClass());
        loadAddSearch(mAddSearchList);
    }
}
