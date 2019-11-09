package com.example.interview.db.user;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.interview.db.DatabaseInstance;
import com.example.interview.page.main.model.UserModel;

import java.util.List;

public class UserRepository {

    private UserDao mUserDao;
    private LiveData<List<UserModel>> allUserList;

    public UserRepository(Application application) {
        DatabaseInstance instance = DatabaseInstance.getDatabaseInstance(application);
        mUserDao = instance.userDao();
        allUserList = mUserDao.getAllUser();
    }

    public LiveData<List<UserModel>> getAllUserList() {
        return allUserList;
    }

    public void insertUser(UserModel user) {
        new insertUser(mUserDao).execute(user);
    }

    public void editUser(UserModel user) {
        new EditUser(mUserDao).execute(user);
    }

    public void insertUserList(List<UserModel> userList) {
        new InsertUserList(mUserDao).execute(userList);
    }

    public void deleteUser(UserModel user) {
        new DeleteUser(mUserDao).execute(user);
    }

    public static class insertUser extends AsyncTask<UserModel, Void, Void> {

        private UserDao userDao;

        insertUser(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(UserModel... params) {
            userDao.insertUser(params[0]);
            return null;
        }
    }

    public static class InsertUserList extends AsyncTask<List<UserModel>, Void, Void> {

        private UserDao userDao;

        InsertUserList(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(List<UserModel>... lists) {

            for (UserModel user : lists[0]) {
                UserModel us = userDao.getUser(user.getUserId());
                if (us != null){
                    us.setFirstName(user.getFirstName());
                    us.setLastName(user.getLastName());
                    us.setEmail(user.getEmail());
                    us.setAvatar(user.getAvatar());
                    Log.i("CCCCCC", "doInBackground: edit");
                    userDao.editUser(us);
                }else {
                    Log.i("CCCCCC", "doInBackground: insert");
                    userDao.insertUser(user);
                }
            }


            return null;
        }
    }

    private static class DeleteUser extends AsyncTask<UserModel, Void, Void> {

        private UserDao userDao;

        public DeleteUser(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(UserModel... userModels) {
            userDao.deleteUser(userModels[0]);
            return null;
        }
    }

    private static class EditUser extends AsyncTask<UserModel, Void, Void> {

        private UserDao userDao;

        public EditUser(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(UserModel... userModels) {
            userDao.deleteUser(userModels[0]);
            return null;
        }
    }
}
