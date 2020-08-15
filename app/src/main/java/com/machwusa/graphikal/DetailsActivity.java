package com.machwusa.graphikal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.machwusa.graphikal.ui.DetailsAdapter;
import com.machwusa.graphikal.ui.UserAdapter;
import com.machwusa.graphikal.util.AplClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    private String userId;
    private String name;
    private DetailsAdapter detailsAdapter;
    private RecyclerView detailsRecyclerView;
    private LinearLayout detsLinearLayout;

    private List<GetUserQuery.Post> postList;
    private List<GetUserQuery.Hobby> hobbyList;
    private boolean isHobbySelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tvName = findViewById(R.id.dets_name);
        TextView tvAge = findViewById(R.id.dets_age);
        TextView tvProfession = findViewById(R.id.dets_profession);
        Button btnHobbies = findViewById(R.id.btn_hobbies);
        Button btnPosts = findViewById(R.id.btn_posts);
        detsLinearLayout = findViewById(R.id.dets_linear_layout);
        isHobbySelected = false;

        postList = Collections.emptyList();
        hobbyList = Collections.emptyList();



        Intent intent = this.getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
            name = intent.getStringExtra("name");
            int age = intent.getIntExtra("age", 0);
            String profession = intent.getStringExtra("profession");

            tvName.setText(name);
            tvAge.setText("Age: " + age);
            tvProfession.setText("Profession: " + profession);


            //Set up recycler
            detailsRecyclerView = findViewById(R.id.details_recycler);
            detailsAdapter = new DetailsAdapter(this.getApplicationContext());
            detailsRecyclerView.setAdapter(detailsAdapter);
            detailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            detailsRecyclerView.setVisibility(View.INVISIBLE);


            btnHobbies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isHobbySelected = true;
                    detailsRecyclerView.setVisibility(View.VISIBLE);
                    detailsRecyclerView.removeAllViews();
                    getUserData();

                }
            });

            btnPosts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isHobbySelected = false;
                    detailsRecyclerView.setVisibility(View.VISIBLE);
                    detailsRecyclerView.removeAllViews();
                    getUserData();
                }
            });

            //getUsers();
        }



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getUserData() {
        AplClient.getApolloClient()
                .query(GetUserQuery.builder()
                        .userId(userId)
                        .build())
                .enqueue(new ApolloCall.Callback<GetUserQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull final Response<GetUserQuery.Data> response) {


                        DetailsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hobbyList = Objects.requireNonNull(Objects.requireNonNull(response.getData()).user).hobbies;
                                postList = Objects.requireNonNull(response.getData().user).posts;

                                if (isHobbySelected){
                                    if (hobbyList.isEmpty())
                                        showMessage("No hobbies available for " + name);
                                }else{
                                    if (postList != null && postList.isEmpty())
                                        showMessage("No posts available for " + name);

                                }

                                detailsAdapter.setUserData(postList, hobbyList, isHobbySelected);

                            }
                        });


                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
//                        progressBar.setVisibility(View.GONE);
                        DetailsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showMessage("Connection error!");
                            }
                        });
                        Log.d(AplClient.TAG, e.getStackTrace().toString());
                    }
                });
    }

    public void showMessage(String message){

        detsLinearLayout.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        TextView textView = new TextView(DetailsActivity.this);
        textView.setText(message);
        textView.setTextColor(Color.CYAN);
        textView.setTextSize((float) 18.9);
        textView.setLayoutParams(layoutParams);

        detsLinearLayout.addView(textView);
    }
}