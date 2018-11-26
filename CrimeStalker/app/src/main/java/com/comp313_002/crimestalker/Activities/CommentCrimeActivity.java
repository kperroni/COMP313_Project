package com.comp313_002.crimestalker.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comp313_002.crimestalker.Classes.Crime;
import com.comp313_002.crimestalker.R;
import com.google.android.gms.common.util.Strings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;


import java.util.ArrayList;
import java.util.List;

public class CommentCrimeActivity extends AppCompatActivity {
    private FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    private DatabaseReference myRef2 = database2.getReference("crimes/CrimeReports");

    private ListView listViewCrime ;
    //List<Crime> crimeList;
    String key;
    Crime crime;
    int maxValue = 0;
    String valueInitial;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_crime);
        context = this;
        // listen all components
        listViewCrime = (ListView)findViewById(R.id.listComments);
        final TextView title = (TextView) findViewById(R.id.textTitle);
        final TextView desc = (TextView) findViewById(R.id.textDesc);
        final TextView oldComment = (TextView) findViewById(R.id.textOldComments);
        final Button btnBack = (Button) findViewById(R.id.buttonBack);
        final Button btnSave = (Button) findViewById(R.id.buttonSave);
        final Button btnPost = (Button) findViewById(R.id.buttonPost);
        final EditText comment = (EditText) findViewById(R.id.editText);
        // getting the value key from the previous activity ReadCrime
        key = getIntent().getExtras().getString("key");
        oldComment.setText("No comments yet");
        //reading the key-value of firebase specific for the unique key
        myRef2.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                crime = dataSnapshot.getValue(Crime.class);
                int size = crime.getComments().size();
                int countRealsize=0;
                List value = new ArrayList<>();
                //this is to fix the real number of the list with the correct component
                for (int i=0 ; i<crime.getComments().size();i++){
                    try{

                        if (!crime.getComments().get(i).toString().isEmpty()){

                            value.add(countRealsize,crime.getComments().get(i).toString());
                            countRealsize ++;
                        }
                    }
                    catch (Exception e){
                      continue;
                    }
                }

                title.setText(crime.getTitle());
                desc.setText(crime.getDescription());
                // continue if there are values found in firebase, then in this case the value can be deleted
                if (value.size()>0){
                    maxValue = value.size(); //set the max value
                    oldComment.setText("All Comments: (click to delete)");
                    ArrayAdapter arrayAdapter = new ArrayAdapter(CommentCrimeActivity.this, android.R.layout.simple_list_item_1,value);
                    listViewCrime.setAdapter(arrayAdapter);
                    listViewCrime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

                            valueInitial = parent.getItemAtPosition(pos).toString();
                            for (int i=0 ; i<crime.getComments().size();i++){
                                try{

                                    if (crime.getComments().get(i).toString().equals(valueInitial)){
                                        String internal= String.valueOf(i);
                                        if (i>0) {
                                            myRef2.child(key).child("comments").child(internal).removeValue();
                                        }
                                        else {
                                            Toast.makeText(CommentCrimeActivity.this, "This comment cannot be deleted, try anoter!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                catch (Exception e){
                                    continue;
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(CommentCrimeActivity.this, ReadCrime.class));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!Strings.isEmptyOrWhitespace(comment.getText().toString())){
                    String internalValue = String.valueOf(getMaxValue());//define the correct value to be include, otherwise it will leave the firebase to create its owns key
                    myRef2.child(key).child("comments").child(internalValue).setValue(comment.getText().toString());
                    comment.setText("");
                }
            }
        });
    //send value to Tweet
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TweetComposer.Builder builder = new TweetComposer.Builder(context).text(
                        context.getString(R.string.report_title) + " " + crime.getTitle() + " " +
                                context.getString(R.string.report_desc) + " " + crime.getDescription() + " " +
                                context.getString(R.string.report_loc_lat) + " " + crime.getLatitude() + " " +
                                context.getString(R.string.report_loc_long) + " " + crime.getLongitude()
                );
                builder.show();
            }
        });
    }
    //getting the max value to be used correctly into the listview
    public int getMaxValue (){
        int max = 0;
        for (int i=0 ; i<crime.getComments().size();i++){
            try{

                if (!crime.getComments().get(i).toString().isEmpty()){
                    String internal= String.valueOf(i);
                    if (max < i ){
                        max = i;
                    }
                    if (max ==0){
                        max ++;
                    }
                }
            }
            catch (Exception e){
                continue;
            }
        }
        return max ;
    }

}
