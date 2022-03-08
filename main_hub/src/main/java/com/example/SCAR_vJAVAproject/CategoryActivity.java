package com.example.SCAR_vJAVAproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CategoryActivity extends AppCompatActivity {

    private Button other;
    private Button banking;
    private Button shopping;
    private Button telecom;
    private Button custom;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CategoryActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.done_mover){
            String ogi = "future";
            Intent intent = new Intent(CategoryActivity.this, done_ReminderActivity.class);
            intent.putExtra("OGI", ogi);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        other = findViewById(R.id.other_button);
        banking = findViewById(R.id.banking_button);
        shopping = findViewById(R.id.shopping_button);
        telecom = findViewById(R.id.telecom_button);
        custom = findViewById(R.id.user_button);

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, other_ReminderActivity_future.class));
            }
        });

        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, custom_ReminderActivity_future.class));
            }
        });

        banking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, banking_ReminderActivity_future.class));
            }
        });

        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, shopping_ReminderActivity_future.class));
            }
        });

        telecom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, telecom_ReminderActivity_future.class));
            }
        });

    }
}