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

public class custom_ReminderActivity_future extends AppCompatActivity {

    Switch switch_rems;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(custom_ReminderActivity_future.this, CategoryActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.done_mover){
            String ogi = "future";
            Intent intent = new Intent(custom_ReminderActivity_future.this, done_ReminderActivity.class);
            intent.putExtra("OGI", ogi);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.addCustom){
            Intent intent1 = new Intent(custom_ReminderActivity_future.this, AddCustomRemActivity.class);
            startActivity(intent1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_activity_reminder_future);

        switch_rems = (Switch) findViewById(R.id.switch_rems_banking_future);

        switch_rems.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startActivity(new Intent(custom_ReminderActivity_future.this, custom_ReminderActivity_past.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new custom_recFragment_future()).commit();
    }
}