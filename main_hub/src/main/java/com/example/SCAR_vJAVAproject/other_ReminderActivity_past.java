package com.example.SCAR_vJAVAproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class other_ReminderActivity_past extends AppCompatActivity {



    Switch switch_past;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(other_ReminderActivity_past.this, CategoryActivity.class));
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
            String ogi = "past";
            Intent intent = new Intent(other_ReminderActivity_past.this, done_ReminderActivity.class);
            intent.putExtra("OGI", ogi);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_activity_reminder_past);

        switch_past = (Switch) findViewById(R.id.switch_past2);

        switch_past.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    startActivity(new Intent(other_ReminderActivity_past.this, other_ReminderActivity_future.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.wrapper_past, new other_recFragment_past()).commit();
    }
}