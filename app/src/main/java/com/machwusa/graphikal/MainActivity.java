package com.machwusa.graphikal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.machwusa.graphikal.ui.UserAdapter;
import com.machwusa.graphikal.util.AplClient;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private UserAdapter userAdapter;
    private ViewGroup content;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        content = findViewById(R.id.content_holder);
        progressBar = findViewById(R.id.progress_bar);


        //Set up recycler
        RecyclerView userRecyclerView = findViewById(R.id.rv_users_list);
        userAdapter = new UserAdapter(this.getApplicationContext());
        userRecyclerView.setAdapter(userAdapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        getUsers();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(getApplicationContext(), AddUserActivity.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reload) {
            recreate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * Apollo client stuff
     * */

    private void getUsers() {
        AplClient.getApolloClient()
                .query(UsersQuery.builder().build())
                .enqueue(new ApolloCall.Callback<UsersQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull final Response<UsersQuery.Data> response) {
                       /*final String name = Objects.requireNonNull(Objects.requireNonNull(response.getData()).users()).get(1).name;

                       for (int i = 0; i < Objects.requireNonNull(response.getData().users).size(); i++){
                           Log.d(TAG, "User"+i+": " + Objects.requireNonNull(response.getData().users()).get(i).name);
                       }*/

//                        progressBar.setVisibility(View.VISIBLE);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userAdapter.setUsers(response.getData().users());
                                progressBar.setVisibility(View.GONE);
                                content.setVisibility(View.VISIBLE);
                            }
                        });


                    }

                    @Override
                    public void onFailure(@NotNull final ApolloException e) {

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                showMessage(e.getMessage());
                            }
                        });
                        Log.d(AplClient.TAG, Arrays.toString(e.getStackTrace()));
                    }
                });
    }

    public void showMessage(String message){

        content.removeAllViews();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        TextView textView = new TextView(MainActivity.this);
        textView.setText(message);
        textView.setTextColor(Color.DKGRAY);
        textView.setTextSize((float) 18.9);
        textView.setLayoutParams(layoutParams);

        content.addView(textView);
    }
}