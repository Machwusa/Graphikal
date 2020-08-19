package com.machwusa.graphikal.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.machwusa.graphikal.DeleteActivity;
import com.machwusa.graphikal.DetailsActivity;
import com.machwusa.graphikal.R;
import com.machwusa.graphikal.UpdateUserActivity;
import com.machwusa.graphikal.UsersQuery;
import com.machwusa.graphikal.util.AplClient;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
        ImageButton btnDelete;
        String userId;
        Context context;

        public UserViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            tvUser = itemView.findViewById(R.id.tv_username);
            tvAge = itemView.findViewById(R.id.tv_age);
            tvProfession = itemView.findViewById(R.id.tv_profession);
            container = itemView.findViewById(R.id.card);
            btnDelete = itemView.findViewById(R.id.btn_delete);

            this.context = context;
        }

        void setUsers(final UsersQuery.User user){
            tvUser.setText(user.name());
            tvAge.setText(String.format("Age: %s", Objects.requireNonNull(user.age()).toString()));
            tvProfession.setText(String.format("Profession: %s", user.profession()));
            userId = user.id();

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DeleteActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("name", user.name());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(context, UpdateUserActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("name", user.name());
                    intent.putExtra("age", user.age());
                    intent.putExtra("profession", user.profession());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    Log.d(AplClient.TAG, userId);

                    return false;
                }
            });

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("name", user.name());
                    intent.putExtra("age", user.age());
                    intent.putExtra("profession", user.profession());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    Log.d(AplClient.TAG, userId);
                }
            });
        }
    }
}
