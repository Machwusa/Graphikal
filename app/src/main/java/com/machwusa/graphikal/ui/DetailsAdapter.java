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

import com.machwusa.graphikal.GetUserQuery;
import com.machwusa.graphikal.R;
import com.machwusa.graphikal.util.AplClient;

import java.util.Collections;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {

    private Context context;
    private List<GetUserQuery.Post> posts = Collections.emptyList();
    private List<GetUserQuery.Hobby> hobbies = Collections.emptyList();
    private boolean isHobby;

    public DetailsAdapter(Context context) {
        this.context = context;
    }

    public void setUserData(List<GetUserQuery.Post> posts, List<GetUserQuery.Hobby> hobbies, boolean isHobby) {
        this.hobbies = hobbies;
        this.posts = posts;
        this.isHobby = isHobby;
        this.notifyDataSetChanged();
        Log.d(AplClient.TAG, "Updating hobbies");
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(parent.getContext());

        final View itemView = layoutInflater.inflate(R.layout.details_row, parent, false);

        return new DetailsViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
        if (isHobby) {
            final GetUserQuery.Hobby hobby = this.hobbies.get(position);
            holder.setHobby(hobby);
        }else {
            final GetUserQuery.Post post = this.posts.get(position);
            holder.setPost(post);
        }


    }

    @Override
    public int getItemCount() {

        return isHobby ? hobbies.size() : posts.size();
    }


    public class DetailsViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDescription;
        CardView detailsContainer;
        String hobbyId;
        String postId;
        Context context;

        public DetailsViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_details_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            detailsContainer = itemView.findViewById(R.id.details_card);

            this.context = context;
        }

        void setHobby(final GetUserQuery.Hobby hobby) {
            tvDescription.setVisibility(View.VISIBLE);
            tvTitle.setText(hobby.title());
            tvDescription.setText(hobby.description());
            hobbyId = hobby.id();
        }

        void setPost(final GetUserQuery.Post post){
            tvDescription.setVisibility(View.GONE);
            tvTitle.setText(post.comment());
            postId = post.id();


        }
    }
}
