package com.example.webchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.webchat.adapters.UserAdapter;
import com.example.webchat.databinding.ActivityUsersBinding;
import com.example.webchat.databinding.ItemContainerUserBinding;
import com.example.webchat.listeners.UserListener;
import com.example.webchat.models.User;
import com.example.webchat.utilities.Constants;
import com.example.webchat.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsersActivity extends BaseActivity implements UserListener {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    private ItemContainerUserBinding userBinding;

    private List<User> receiverUsers = new ArrayList<>();
    private FirebaseFirestore database;
    private Boolean isReceiverAvailable = false;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        userBinding = ItemContainerUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        database = FirebaseFirestore.getInstance();
        getUsers();
        setListeners();
        userAdapter = new UserAdapter(new ArrayList<>(), this);
        binding.usersRecyclerView.setAdapter(userAdapter);
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .addSnapshotListener((value, error) -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (error != null || value == null) {
                        showErrorMessage();
                        return;
                    }
                    List<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : value) {
                        if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                            continue;
                        }
                        User user = new User();
                        user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                        user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                        user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                        user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                        user.id = queryDocumentSnapshot.getId();
                        user.isAvailable = queryDocumentSnapshot.getLong(Constants.KEY_AVAILABILITY) == 1;
                        users.add(user);
                    }
                    if (users.size() > 0) {
                        UserAdapter userAdapter = new UserAdapter(users, this);
                        binding.usersRecyclerView.setAdapter(userAdapter);
                        userAdapter.updateUserList(users);
                        binding.usersRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        showErrorMessage();
                    }
                });

    }

    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s", "No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else
            binding.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}