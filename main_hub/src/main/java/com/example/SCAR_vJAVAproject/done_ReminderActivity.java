package com.example.SCAR_vJAVAproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class done_ReminderActivity extends AppCompatActivity {
    String kidhar_se;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(done_ReminderActivity.this, CategoryActivity.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.done_activity_reminder);

        Intent incoming = getIntent();
        kidhar_se = incoming.getStringExtra("OGI");
        System.out.println(kidhar_se);

        getSupportFragmentManager().beginTransaction().replace(R.id.wrapper_done, new done_recFragment()).commit();
    }
}