package com.example.SCAR_vJAVAproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCustomRemActivity extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();

    private EditText event;
    private EditText desc;
    private EditText enteredDate;
    private Button details_upload;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String userID = user.getUid();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = db.getReference();
    DatabaseReference remRef = rootRef.child("users").child(userID).child("reminders").child("future").child("Shopping");

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddCustomRemActivity.this, custom_ReminderActivity_future.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_custom_rem);

        event = findViewById(R.id.addEvent);
        desc = findViewById(R.id.addDesc);
        enteredDate = findViewById(R.id.addDate);
        details_upload = findViewById(R.id.details_upload);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        enteredDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddCustomRemActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        details_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_event = event.getText().toString();
                String txt_desc = desc.getText().toString();
                String txt_date = enteredDate.getText().toString();
                String date_for_processes = txt_date;
                if(txt_date.isEmpty() || txt_desc.isEmpty() || txt_event.isEmpty()){
                    Toast.makeText(AddCustomRemActivity.this, "One or More fields are empty.", Toast.LENGTH_SHORT).show();
                }
                else{

                    Matcher groups = Pattern.compile("^([0-2][0-9]|3[0-1])/(0[1-9]|1[0-2])/(\\d{4})$", Pattern.CASE_INSENSITIVE).matcher(date_for_processes);
                    if(!groups.find()){
                        Toast.makeText(AddCustomRemActivity.this, "Invalid date Format", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        model_custom rems = new model_custom(txt_desc, txt_event, txt_date);
                        rems.setEvent(txt_event);
                        rems.setDescription(txt_desc);
                        rems.setDate_custom(txt_date);
                        remRef = rootRef.child("users").child(userID).child("reminders").child("future").child("Custom");
                        remRef.push().setValue(rems).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AddCustomRemActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                                event.setText("");
                                desc.setText("");
                                enteredDate.setText("");
                            }
                        });

                        int day = Integer.parseInt(removeZero(groups.group(1)));
                        int month = Integer.parseInt(removeZero(groups.group(2)));
                        int year = Integer.parseInt(removeZero(groups.group(3)));
                        System.out.println("XXXXXXX AFTER REMOVING ZEROES");
                        System.out.println(day);
                        System.out.println(month);
                        System.out.println(year);


                        GregorianCalendar event_date = new GregorianCalendar(year, month-1, day);
                        System.out.println(event_date);


                        Intent intent = new Intent(Intent.ACTION_INSERT);
                        intent.setData(CalendarContract.Events.CONTENT_URI);
                        intent.putExtra(CalendarContract.Events.TITLE, txt_event);
                        intent.putExtra(CalendarContract.Events.DESCRIPTION, txt_desc);
                        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "SCAR\u00a9");
                        intent.putExtra(CalendarContract.Events.CALENDAR_ID, 1);

                        intent.putExtra(CalendarContract.Events.ALL_DAY, true);
                        intent.putExtra(CalendarContract.Events.HAS_ALARM, true);
                        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event_date.getTimeInMillis());
                        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event_date.getTimeInMillis());

                        if(intent.resolveActivity(getPackageManager()) != null){
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(AddCustomRemActivity.this, "You have no calendar app on this device", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
            }
        });
    }

    private void updateLabel(){
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        enteredDate.setText(dateFormat.format(myCalendar.getTime()));
    }

    public static String removeZero(String str)
    {
        // Count leading zeros
        int i = 0;
        while (i < str.length() && str.charAt(i) == '0')
            i++;
        // Convert str into StringBuffer as Strings
        // are immutable.
        StringBuffer sb = new StringBuffer(str);
        // The  StringBuffer replace function removes
        // i characters from given index (0 here)
        sb.replace(0, i, "");
        return sb.toString();  // return in String
    }
}