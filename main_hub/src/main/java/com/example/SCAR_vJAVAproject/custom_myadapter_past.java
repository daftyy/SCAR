package com.example.SCAR_vJAVAproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class custom_myadapter_past extends FirebaseRecyclerAdapter<model_custom, custom_myadapter_past.myviewholder_2_custom> {
    CheckBox done;
    String id;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String userID = user.getUid();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = db.getReference();
    DatabaseReference pastRef = rootRef.child("users").child(userID).child("reminders").child("past").child("Custom");
    DatabaseReference doneRef = rootRef.child("users").child(userID).child("reminders").child("done");

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public custom_myadapter_past(@NonNull FirebaseRecyclerOptions<model_custom> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder_2_custom holder, int position, @NonNull model_custom model) {

        holder.row_event_custom.setText(model.getEvent());
        holder.row_desc_custom.setText(model.getDescription());
        holder.row_date_custom.setText(model.getDate_custom());
    }

    @NonNull
    @Override
    public myviewholder_2_custom onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_custom, parent, false);
        return new myviewholder_2_custom(view);

    }

    public class myviewholder_2_custom
            extends RecyclerView.ViewHolder{

        TextView row_event_custom;
        TextView row_desc_custom;
        TextView row_date_custom;


        public myviewholder_2_custom(@NonNull View itemView) {
            super(itemView);

            row_event_custom = itemView.findViewById(R.id.custom_row_event);
            row_desc_custom = itemView.findViewById(R.id.custom_row_desc);
            row_date_custom = itemView.findViewById(R.id.custom_row_date);

            done = itemView.findViewById(R.id.custom_row_checkbox);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = getRef(getBindingAdapterPosition()).getKey();
                    pastRef.child(id).removeValue();
                    //moveFirebaseRecord(pastRef.child(id), doneRef.child(id));
                }
            });
        }
    }
    private void moveFirebaseRecord(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {

                            System.out.println("Copy failed");
                        } else {
                            fromPath.removeValue();
                            System.out.println("Success");

                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
