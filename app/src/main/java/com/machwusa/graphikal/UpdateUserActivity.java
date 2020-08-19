package com.machwusa.graphikal;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.machwusa.graphikal.util.AplClient;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UpdateUserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName;
    private EditText etAge;
    private EditText etProfession;
    private EditText etComment;
    private EditText etHobbyTitle;
    private EditText etHobbyDesc;

    private Button btnUpdateUser;
    private Button btnSavePost;
    private Button btnSaveHobby;

    private LinearLayout bottomLinearLayout;

    private String userId;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etName = findViewById(R.id.update_user_name);
        etAge = findViewById(R.id.update_user_age);
        etProfession = findViewById(R.id.update_user_profession);
        etComment = findViewById(R.id.update_post);
        etHobbyTitle = findViewById(R.id.update_hobby_title);
        etHobbyDesc = findViewById(R.id.update_hobby_description);
        btnUpdateUser = findViewById(R.id.btn_update_user);
        btnUpdateUser.setOnClickListener(this);
        btnSavePost = findViewById(R.id.btn_update_post);
        btnSavePost.setOnClickListener(this);
        btnSaveHobby = findViewById(R.id.btn_update_hobby);
        btnSaveHobby.setOnClickListener(this);

        bottomLinearLayout = findViewById(R.id.ll_update_bottom_view);

        Intent intent = this.getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
            etName.setText(intent.getStringExtra("name"));
            etAge.setText(String.valueOf(intent.getIntExtra("age", 0)));
            etProfession.setText(intent.getStringExtra("profession"));
        }

      /*  FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_update_user:
                updateUser(userId);
                break;
            case R.id.btn_update_post:
                savePost(userId);
                break;
            case R.id.btn_update_hobby:
                saveHobby(userId);
                break;
        }

    }

    private void savePost(String id) {

        if (!etComment.getText().toString().isEmpty()) {

            AplClient.getApolloClient().mutate(AddPostMutation
                    .builder()
                    .postComment(etComment.getText().toString().trim())
                    .userId(id)
                    .build())
                    .enqueue(new ApolloCall.Callback<AddPostMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<AddPostMutation.Data> response) {

                            String post = Objects.requireNonNull(Objects.requireNonNull(response.getData()).CreatePost).comment;

                            UpdateUserActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    etComment.setText("");
                                    Toast.makeText(UpdateUserActivity.this, "Post has been saved", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(@NotNull final ApolloException e) {
                            UpdateUserActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UpdateUserActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    });

        }else {
            Toast.makeText(this,"Post missing",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void saveHobby(String id) {

        if (!etHobbyTitle.getText().toString().isEmpty() && !etHobbyDesc.getText().toString().isEmpty()) {
            AplClient.getApolloClient().mutate(AddHobbyMutation
                    .builder()
                    .hobbyTitle(etHobbyTitle.getText().toString().trim())
                    .hobbyDescription(etHobbyDesc.getText().toString().trim())
                    .userId(id)
                    .build())
                    .enqueue(new ApolloCall.Callback<AddHobbyMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<AddHobbyMutation.Data> response) {

                            final String title = Objects.requireNonNull(Objects.requireNonNull(response.getData()).CreateHobby).title;

                            UpdateUserActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    etHobbyTitle.setText("");
                                    etHobbyDesc.setText("");
                                    Toast.makeText(UpdateUserActivity.this, title + " has been saved", Toast.LENGTH_LONG).show();
                                }
                            });


                        }

                        @Override
                        public void onFailure(@NotNull final ApolloException e) {
                            UpdateUserActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UpdateUserActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    });
        }else {
            Toast.makeText(this,"Hobby title or description missing",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void updateUser(String id) {
        AplClient.getApolloClient()
                .mutate(UpdateUserMutation
                .builder()
                .id(id)
                .name(etName.getText().toString().trim())
                .age(Integer.parseInt(etAge.getText().toString().trim()))
                .profession(etProfession.getText().toString().trim())
                .build()
                ).enqueue(new ApolloCall.Callback<UpdateUserMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<UpdateUserMutation.Data> response) {
                final String name = Objects.requireNonNull(Objects.requireNonNull(response.getData()).UpdateUser).name;

                UpdateUserActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UpdateUserActivity.this, name + " updated!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(UpdateUserActivity.this, MainActivity.class));
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}