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

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DeleteActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnYes;
    private Button btnNo;
    private TextView tvDelete;

    private String userId;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnYes = findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(this);
        btnNo = findViewById(R.id.btn_no);
        btnNo.setOnClickListener(this);
        tvDelete = findViewById(R.id.tv_delete);

        Intent intent = this.getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
            name = intent.getStringExtra("name");

            tvDelete.setText(name);
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

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_yes:
                deleteUser(userId);
                break;
            case R.id.btn_no:
                startActivity(new Intent(DeleteActivity.this, MainActivity.class));
                break;
        }

    }

    private void deleteUser(String id) {

        AplClient.getApolloClient()
                .mutate(DeleteUserMutation
                .builder()
                .userId(id)
                .build())
                .enqueue(new ApolloCall.Callback<DeleteUserMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull final Response<DeleteUserMutation.Data> response) {
                        DeleteActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DeleteActivity.this, Objects.requireNonNull(Objects.requireNonNull(response.getData()).RemoveUser).name + " deleted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(DeleteActivity.this, MainActivity.class));
                                DeleteActivity.this.finish();
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });

    }
}