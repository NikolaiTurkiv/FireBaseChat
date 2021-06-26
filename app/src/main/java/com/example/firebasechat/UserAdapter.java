package com.example.firebasechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<User> users;
    private OnUserClickListener onUserClickListener;

    public UserAdapter(ArrayList<User> users) {
        this.users = users;
    }

    public interface OnUserClickListener{
        void onUserClick(int position);
    }
    public void setOnUserClickListener(OnUserClickListener onUserClickListener){
        this.onUserClickListener = onUserClickListener;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);

        return new UserViewHolder(view,onUserClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.imageViewAvatar.setImageResource(user.getAvatarMockUpResource());
        holder.textViewUserName.setText(user.getName());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewAvatar;
        TextView textViewUserName;

        public UserViewHolder(@NonNull View itemView,OnUserClickListener listener) {
            super(itemView);

            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onUserClick(position);
                        }

                    }
                }
            });
        }
    }

}
