package com.example.SCAR_vJAVAproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSActivity<cad> extends AppCompatActivity {


    String amount;
    String company;
    String date;

    String day1;
    String month1;
    String year1;


    private TextView sms_body;
    private TextView thread_id;
    private Button last;
    private Button next;
    private Button upload;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String userID = user.getUid();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = db.getReference();
    DatabaseReference remRef;
    String[] cadc = new String[4];



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SMSActivity.this, HomeActivity.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsactivity);

        thread_id = findViewById(R.id.thread_id);
        sms_body = findViewById(R.id.sms_text);

        last = findViewById(R.id.last_text);
        next = findViewById(R.id.next_text);
        upload = findViewById(R.id.upload_btn);

        ActivityCompat.requestPermissions(SMSActivity.this, new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null);

        cursor.moveToFirst();
        if(spamCheck(cursor.getString(12)) == true){
            cursor.moveToNext();
        }
        sms_body.setText(cursor.getString(12));
        thread_id.setText(cursor.getString(2));
        extract(cursor.getString(12), cursor.getString(2));

        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cursor.isFirst()){
                    Toast.makeText(SMSActivity.this, "You are on the First SMS", Toast.LENGTH_SHORT).show();
                }
                else {
                    cursor.moveToPrevious();
                    if(spamCheck(cursor.getString(12)) == true){
                        cursor.moveToNext();
                    }
                    sms_body.setText(cursor.getString(12));
                    thread_id.setText(cursor.getString(2));
                    extract(cursor.getString(12), cursor.getString(2));
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cursor.isLast()){
                    Toast.makeText(SMSActivity.this, "You are on the last SMS", Toast.LENGTH_SHORT).show();
                }
                else {
                    cursor.moveToNext();
                    if(spamCheck(cursor.getString(12)) == true){
                        cursor.moveToNext();
                    }
                    sms_body.setText(cursor.getString(12));
                    thread_id.setText(cursor.getString(2));
                    extract(cursor.getString(12), cursor.getString(2));
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String company_UP = cadc[0];
                String amount_UP = cadc[1];
                String date_UP = cadc[2];
                String category = getCategory(company_UP);
                model rems = new model(amount, company, date);
                rems.setCompany(company_UP);
                rems.setAmount(amount_UP);
                rems.setDate(date_UP);
                remRef = rootRef.child("users").child(userID).child("reminders").child("future").child(category);
                remRef.push().setValue(rems);

                int day_date = Integer.parseInt(removeZero(day1));
                int month_date = Integer.parseInt(removeZero(month1));
                int year_date = Integer.parseInt(removeZero(year1));
                GregorianCalendar event_date = new GregorianCalendar(year_date, month_date-1, day_date);

                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE, cadc[0]);
                intent.putExtra(CalendarContract.Events.DESCRIPTION, cadc[1]+" by "+cadc[2]);
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
                    Toast.makeText(SMSActivity.this, "You have no calendar app on this device", Toast.LENGTH_SHORT).show();
                }


                Toast.makeText(SMSActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private String[] extract(String text, String sender) {
        cadc[0] = "default";
        cadc[1] = "default";
        cadc[2] = "default";

        //String input = "XF-AIRTEL";
        Matcher name = Pattern.compile("([A-Z][A-Z][-])", Pattern.CASE_INSENSITIVE).matcher(sender);
        while (name.find()) {
            cadc[0] = sender.replace(name.group(0), "");
        }

        //String input = "Hi your bill is due is Sep 6, 2021. Last day for payment 09/7/21 ie 9/JUL/2021";
        Matcher m = Pattern.compile("(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-zA-Z.,-]*[\\s-]?(\\d{1,2})?[,\\s-]?[\\s]?\\d{4}|(\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4}|\\d{2,4}[/-]\\d{1,2}[/-]\\d{1,2})|(\\d{1,2}[/-](?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[/-]\\d{2,4})|((?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[/-]\\d{1,2}[/-]\\d{2,4})|(\\d{1,2})[\\s]\\d{1,2}[\\s]\\d{1,4}|(\\d{1,2})[\\s](?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[\\s]\\d{4}", Pattern.CASE_INSENSITIVE).matcher(text);
        while (m.find()) {
            cadc[2] = m.group(0);

        }
        if(cadc[2] != "default") {
            GetDate a1 = new GetDate();
            a1.RemoveSpecialCharacter(cadc[2]);
        }


        Pattern pattern = Pattern.compile("[R][s][.]?[ ]?\\d{1,9}[,\\.]?(\\d{1,9})?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find())
        {
            cadc[1] = matcher.group(0);
        }

        return cadc;
    }


    class GetDate
    {
        public void Month(String[] result)
        {
            String[] mthArr = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
            String day = "";
            String mm = result[1];
            Format format = new Format();
            for (int x=0;x<3;x++)
            {
                for (int z=0;z<mthArr.length;z++)
                {
                    if (result[x].toLowerCase().contains(mthArr[z].toLowerCase()))
                    {
                        int mmi = z+1;
                        if (mmi<10)
                        {
                            mm = Integer.toString(mmi);
                            mm = "0"+mm;
                        }
                        else
                        {
                            mm = Integer.toString(mmi);
                        }
                    }
                }
            }
            if (result[0].matches("^[a-zA-Z]+$"))
            {
                day = result[1];
            }
            else
            {
                day = result[0];
            }
            day1 = format.DF(day);
            month1 = format.DF(mm);
            year1 = format.Year(result[2].replaceAll("\\s", ""));
            cadc[2] = format.FinalDate(day1,month1,year1);
        }

        public String[] Split(String input)
        {
            String[] result = input.split("\\s+");
            Month(result);
            return result;
        }

        public String RemoveSpecialCharacter(String input)
        {
            String output = input.replaceAll("\\W", " ");
            Split(output);
            return output;
        }
    }

    static class Format
    {
        public String DF(String m) {
            if (m.length() < 2) {
                m = "0" + m;
            }
            return m;
        }
        public String Year(String year)
        {
            if (year.length()<3)
            {
                year = "20"+year;
            }
            return year;
        }
        public String FinalDate (String d, String m,String y)
        {
            String fd = d+"/"+m+"/"+y;
            return fd;
        }
    }

    public static boolean spamCheck(String TextBody) {

        String[] spam = {"credited", "debited", "congrats", "congratulations", "bonus", "verification", "rummy", "cricket", "poker", "fantasy"};
        int length = spam.length;
        for(int i=0;i<length;i++)
            if (TextBody.toLowerCase().contains(spam[i]))
            {
                return true;
            }
        return false;
    }

    private String getCategory(String company){
        String[] Telecom = {"AIRTEL", "JIO", "VODAFONE", "MTNL", "Vi", "Vodafone"};
        String[] Shopping = {"LNKART", "AMAZON"};
        String[] Banking = {"HDFCBK", "UNIONB", "SCBL", "AXISMR", "AxisBk"};
        String category;


        if( Arrays.asList(Telecom).contains(company)){
            category = "Telecom";
        }
        else if( Arrays.asList(Shopping).contains(company)){
            category = "Shopping";
        }
        else if( Arrays.asList(Banking).contains(company)){
            category = "Banking";
        }
        else{
            category = "Other";
        }
        return category;
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