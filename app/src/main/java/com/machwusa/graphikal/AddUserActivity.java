package com.machwusa.graphikal;

import android.content.Intent;
import android.os.Bundle;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.machwusa.graphikal.util.AplClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName;
    private EditText etAge;
    private EditText etProfession;
    private EditText etComment;
    private EditText etHobbyTitle;
    private EditText etHobbyDesc;

    private Button btnSaveUser;
    private Button btnSaveHobbyPost;

    private LinearLayout bottomLinearLayout;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etName = findViewById(R.id.add_user_name);
        etAge = findViewById(R.id.add_user_age);
        etProfession = findViewById(R.id.add_user_profession);
        etComment = findViewById(R.id.add_post);
        etHobbyTitle = findViewById(R.id.add_hobby_title);
        etHobbyDesc = findViewById(R.id.add_hobby_description);
        btnSaveUser = findViewById(R.id.btn_save_user);
        btnSaveUser.setOnClickListener(this);
        btnSaveHobbyPost = findViewById(R.id.btn_save_post_hobby);
        btnSaveHobbyPost.setOnClickListener(this);

        bottomLinearLayout = findViewById(R.id.ll_bottom_view);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_save_user:
                saveUser();
                break;
            case R.id.btn_save_post_hobby:
                saveHobby(userId);
                savePost(userId);
                break;
        }

    }


    private void saveUser() {
        if (!etName.getText().toString().isEmpty() && !etAge.getText().toString().isEmpty()) {
            AplClient.getApolloClient()
                    .mutate(AddUserMutation
                            .builder()
                            .username(etName.getText().toString().trim())
                            .userAge(Integer.parseInt(etAge.getText().toString().trim()))
                            .userProfession(etProfession.getText().toString().trim())
                            .build())
                    .enqueue(new ApolloCall.Callback<AddUserMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<AddUserMutation.Data> response) {

                            final String id = Objects.requireNonNull(Objects.requireNonNull(response.getData()).CreateUser).id;

                            AddUserActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (id != null){
                                        userId = id;
                                        bottomLinearLayout.setVisibility(View.VISIBLE);

                                        Toast.makeText(AddUserActivity.this, "id = " + userId, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            Toast.makeText(AddUserActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }else {
            Toast.makeText(this,"Name or age missing",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void savePost(final String id) {
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

                            //Toast.makeText(AddUserActivity.this, post + " has been saved", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(AddUserActivity.this, MainActivity.class));
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            Toast.makeText(AddUserActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

                            String title = Objects.requireNonNull(Objects.requireNonNull(response.getData()).CreateHobby).title;

                            Toast.makeText(AddUserActivity.this, title + " has been saved", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            Toast.makeText(AddUserActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }else {
            Toast.makeText(this,"Hobby title or description missing",
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(AddUserActivity.this, MainActivity.class));
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}