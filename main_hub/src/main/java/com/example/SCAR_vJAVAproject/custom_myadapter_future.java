package com.example.SCAR_vJAVAproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class custom_myadapter_future extends FirebaseRecyclerAdapter<model_custom, custom_myadapter_future.myviewholder_custom> {


    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String userID = user.getUid();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = db.getReference();
    DatabaseReference remRef = rootRef.child("users").child(userID).child("reminders").child("future").child("Custom");
    DatabaseReference doneRef = rootRef.child("users").child(userID).child("reminders").child("done");
    DatabaseReference pastRef = rootRef.child("users").child(userID).child("reminders").child("past").child("Custom");

    String id;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public custom_myadapter_future(@NonNull FirebaseRecyclerOptions<model_custom> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder_custom holder, int position, @NonNull model_custom model) {

        holder.row_event_custom.setText(model.getEvent());
        holder.row_desc_custom.setText(model.getDescription());
        holder.row_date_custom.setText(model.getDate_custom());

    }

    @NonNull
    @Override
    public myviewholder_custom onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_custom, parent, false);
        return new myviewholder_custom(view);

    }

    public class myviewholder_custom extends RecyclerView.ViewHolder{

        TextView row_event_custom;
        TextView row_desc_custom;
        TextView row_date_custom;
        CheckBox done;



        public myviewholder_custom(@NonNull View itemView) {
            super(itemView);

            row_event_custom = itemView.findViewById(R.id.custom_row_event);
            row_desc_custom = itemView.findViewById(R.id.custom_row_desc);
            row_date_custom = itemView.findViewById(R.id.custom_row_date);
            future2past(remRef, pastRef);

            done = itemView.findViewById(R.id.custom_row_checkbox);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = getRef(getBindingAdapterPosition()).getKey();
                    remRef.child(id).removeValue();
                }
            });
        }
    }

    private void future2past(final DatabaseReference fromPath, final DatabaseReference toPath){

        remRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String pulled_date = snapshot.child("date_custom").getValue().toString();

                    String yyyy = "";
                    String mm = "";
                    String dd = "";
                    /*if(yyyy.isEmpty() && mm.isEmpty() && dd.isEmpty()){
                        break;
                    }*/

                    Date curDate = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                    String regex = "^([0-9][0-9])/([0-9][0-9])/(\\d{4})";
                    //Creating a pattern object
                    Pattern pattern = Pattern.compile(regex);
                    //Matching the compiled pattern in the String
                    Matcher matcher = pattern.matcher(pulled_date);
                    boolean bool = matcher.matches();
                    if(bool) {
                        yyyy = matcher.group(3);
                        mm = matcher.group(2);
                        dd = matcher.group(1);
                        dd = removeZero(dd);
                        mm = removeZero(mm);
                        yyyy = removeZero(yyyy);
                    }
                    else {
                        Log.d("CUSTOM_TEST","Date is not valid");
                    }
                    int day = Integer.parseInt(dd);
                    int month = Integer.parseInt(mm)-1;
                    int year = Integer.parseInt(yyyy)-1900;

                    Date chkDate = new Date(year,month,day);
                    int compareDate = chkDate.compareTo(curDate);
                    Log.d("CUSTOM_TEST",chkDate.toString());
                    Log.d("CUSTOM_TEST", Integer.toString(compareDate));
                    if (compareDate == -1)
                    {
                        Log.d("CUSTOM_TEST","date is in the past mannnnn XXXXXXXXXXX");
                        String key = snapshot.getKey();
                        toPath.child(key).setValue(snapshot.getValue());
                        fromPath.child(key).removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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






