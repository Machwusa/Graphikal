package com.machwusa.graphikal.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.machwusa.graphikal.R;
import com.machwusa.graphikal.UsersQuery;
import com.machwusa.graphikal.util.AplClient;

import java.util.Collections;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<UsersQuery.User> users = Collections.emptyList();

    public UserAdapter(Context context) {
        this.context = context;
    }

    public void setUsers(List<UsersQuery.User> users){
        this.users = users;
        this.notifyDataSetChanged();
        Log.d(AplClient.TAG, "Updating users");
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater
                .from(parent.getContext());

        final View itemView = layoutInflater.inflate(R.layout.user_row, parent, false);

        return new UserViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {

        final UsersQuery.User user = this.users.get(position);

        holder.setUsers(user);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tvUser;
        TextView tvAge;
        TextView tvProfession;
        CardView container;
        String userId;
        Context context;

        public UserViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            tvUser = itemView.findViewById(R.id.tv_username);
            tvAge = itemView.findViewById(R.id.tv_age);
            tvProfession = itemView.findViewById(R.id.tv_profession);
            container = itemView.findViewById(R.id.card);

            this.context = context;
        }

        void setUsers(UsersQuery.User user){
            tvUser.setText(user.name());
            tvAge.setText(user.age().toString());
            tvProfession.setText(user.profession());
            userId = user.id();

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(AplClient.TAG, userId);
                }
            });
        }
    }
}
