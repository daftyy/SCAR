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

public class banking_myadapter_past extends FirebaseRecyclerAdapter<model, banking_myadapter_past.myviewholder_2_other> {
    CheckBox done;
    String id;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String userID = user.getUid();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = db.getReference();
    DatabaseReference pastRef = rootRef.child("users").child(userID).child("reminders").child("past").child("Banking");
    DatabaseReference doneRef = rootRef.child("users").child(userID).child("reminders").child("done");

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public banking_myadapter_past(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder_2_other holder, int position, @NonNull model model) {

        holder.row_company.setText(model.getCompany());
        holder.row_amount.setText(model.getAmount());
        holder.row_date.setText(model.getDate());
    }

    @NonNull
    @Override
    public myviewholder_2_other onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_other_past, parent, false);
        return new myviewholder_2_other(view);

    }

    public class myviewholder_2_other
            extends RecyclerView.ViewHolder{

        TextView row_company;
        TextView row_amount;
        TextView row_date;


        public myviewholder_2_other(@NonNull View itemView) {
            super(itemView);

            row_company = itemView.findViewById(R.id.row_company_2);
            row_amount = itemView.findViewById(R.id.row_amount_2);
            row_date = itemView.findViewById(R.id.row_date_2);

            done = itemView.findViewById(R.id.row_checkbox_2);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = getRef(getBindingAdapterPosition()).getKey();
                    moveFirebaseRecord(pastRef.child(id), doneRef.child(id));
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
