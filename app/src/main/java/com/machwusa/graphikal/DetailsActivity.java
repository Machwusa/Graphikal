package com.machwusa.graphikal;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private String userId;
    private String name;
    private int age;
    private String profession;

    private TextView tvName;
    private TextView tvAge;
    private TextView tvProfession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvName = findViewById(R.id.dets_name);
        tvAge = findViewById(R.id.dets_age);
        tvProfession = findViewById(R.id.dets_profession);

        Intent intent = this.getIntent();
        if (intent != null){
            userId = intent.getStringExtra("userId");
            name = intent.getStringExtra("name");
            age = intent.getIntExtra("age", 0);
            profession = intent.getStringExtra("profession");

            tvName.setText(name);
            tvAge.setText("Age: " + age);
            tvProfession.setText("Profession: " +profession);
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
}