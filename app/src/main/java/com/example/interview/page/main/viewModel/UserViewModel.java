package com.example.interview.page.main.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.interview.api.ApiInstance;
import com.example.interview.api.ResponseModel;
import com.example.interview.db.user.UserRepository;
import com.example.interview.page.main.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends AndroidViewModel {


    private UserRepository mUserRepository;
    private LiveData<List<UserModel>> allUserList;
    private MutableLiveData<String> errorMessage;

    public UserViewModel(@NonNull Application application) {
        super(application);
        getAllUserFromServer(1);
        mUserRepository = new UserRepository(application);
        allUserList = mUserRepository.getAllUserList();

    }


    public LiveData<List<UserModel>> getAllUserList() {
        return allUserList;
    }

    public void deleteUser(UserModel user){
        mUserRepository.deleteUser(user);
    }


    public MutableLiveData<String> getErrorMessage() {
        if (errorMessage == null) {
            errorMessage = new MutableLiveData<>();
        }
        return errorMessage;
    }

    public void insertUserList(List<UserModel> userList){
        mUserRepository.insertUserList(userList);
    }


    public void getAllUserFromServer(int page) {

        ApiInstance.apiService().getUserList(page).enqueue(new Callback<ResponseModel<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<UserModel>> call, Response<ResponseModel<UserModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getUserList() != null && response.body().getUserList().size() > 0) {
                        insertUserList(response.body().getUserList());
                    }
                } else {
                    getErrorMessage().setValue("" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<UserModel>> call, Throwable t) {
                getErrorMessage().setValue("" + t);
            }
        });

    }
}
