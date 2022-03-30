package com.bubnov.lab6base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> {

    ArrayList<User> userArrayList;

    @NonNull
    @Override
     public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerAdapter.UserViewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.name.setText(user.getFirstname());
        holder.email.setText(user.getEmail());
        holder.surname.setText(user.getSurname());
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }


    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextInputEditText name;
        private final TextInputEditText surname;
        private final TextInputEditText email;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.edit_name);
            surname = itemView.findViewById(R.id.edit_surname);
            email = itemView.findViewById(R.id.edit_email);
        }
    }
}
