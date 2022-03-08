package com.example.SCAR_vJAVAproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class done_myadapter extends FirebaseRecyclerAdapter<model, done_myadapter.myviewholder_done> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String userID = user.getUid();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = db.getReference();
    DatabaseReference doneRef = rootRef.child("users").child(userID).child("reminders").child("done");
    String id;

    public done_myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder_done holder, int position, @NonNull model model) {

        holder.row_company.setText(model.getCompany());
        holder.row_amount.setText(model.getAmount());
        holder.row_date.setText(model.getDate());
    }

    @NonNull
    @Override
    public myviewholder_done onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_other_done, parent, false);
        return new myviewholder_done(view);

    }

    public class myviewholder_done extends RecyclerView.ViewHolder{

        TextView row_company;
        TextView row_amount;
        TextView row_date;
        FloatingActionButton delete;


        public myviewholder_done(@NonNull View itemView) {
            super(itemView);

            row_company = itemView.findViewById(R.id.row_company_3);
            row_amount = itemView.findViewById(R.id.row_amount_3);
            row_date = itemView.findViewById(R.id.row_date_3);
            delete = itemView.findViewById(R.id.delete_fab);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = getRef(getBindingAdapterPosition()).getKey();
                    doneRef.child(id).removeValue();
                }
            });
        }
    }
}
