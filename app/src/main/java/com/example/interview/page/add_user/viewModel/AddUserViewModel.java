package com.example.interview.page.add_user.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.interview.db.user.UserRepository;
import com.example.interview.page.main.model.UserModel;

public class AddUserViewModel extends AndroidViewModel {

    private UserRepository mUserRepository;


    public AddUserViewModel(@NonNull Application application) {
        super(application);
        mUserRepository = new UserRepository(application);
    }

    public void insertUser(String imagePath, String firstName, String lastName, String email) {

        UserModel userModel = new UserModel();
        userModel.setAvatar(imagePath);
        userModel.setFirstName(firstName);
        userModel.setLastName(lastName);
        userModel.setEmail(email);
        mUserRepository.insertUser(userModel);
    }
    public void editUser(UserModel user) {
        mUserRepository.editUser(user);
    }

}
