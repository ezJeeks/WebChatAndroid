package com.example.webchat.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.webchat.databinding.ItemContainerUserBinding;
import com.example.webchat.listeners.UserListener;
import com.example.webchat.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private List<User> users;
    private final UserListener userListener;
    public UserAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        ItemContainerUserBinding binding;
        User user;

        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;

        }
        void setUserData(User user){
            binding.textName.setText(user.name);
            binding.textEmail.setText(user.email);
            binding.imageProfile.setImageBitmap(getUserImage(user.image));
            if (user.isAvailable) {
                binding.textAvailability.setVisibility(View.VISIBLE);
            } else {
                binding.textAvailability.setVisibility(View.GONE);
            }
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));
        }
    }
    public void updateUserList(List<User> updatedUsers) {
        users = updatedUsers;
        notifyDataSetChanged();
    }

    private Bitmap getUserImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}
