package com.machwusa.graphikal;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.machwusa.graphikal.ui.UserAdapter;
import com.machwusa.graphikal.util.AplClient;

import org.jetbrains.annotations.NotNull;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        if (id == R.id.action_settings) {
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

                        progressBar.setVisibility(View.VISIBLE);
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
                    public void onFailure(@NotNull ApolloException e) {
//                        progressBar.setVisibility(View.GONE);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                        Log.d(AplClient.TAG, e.getStackTrace().toString());
                    }
                });
    }
}