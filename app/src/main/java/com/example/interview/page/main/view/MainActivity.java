package com.example.interview.page.main.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.interview.R;
import com.example.interview.databinding.ActivityMainBinding;
import com.example.interview.db.DatabaseInstance;
import com.example.interview.page.add_user.view.AddUserFragment;
import com.example.interview.page.main.model.UserModel;
import com.example.interview.page.main.viewModel.UserViewModel;
import com.example.interview.utill.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserViewModel userViewModel;
    private UserAdapter userAdapter;
    private DatabaseInstance databaseInstance;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setModel(this);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        initRecyclerView();
    }

    private void initRecyclerView() {

        userAdapter = new UserAdapter(new ArrayList<UserModel>(), this, new UserAdapter.OnClickAdapter() {
            @Override
            public void onDeleteItem(int po, UserModel item) {
                userViewModel.deleteUser(item);
            }

            @Override
            public void onEditItem(int po, UserModel item) {

                FragmentTransaction addUserFragment = getSupportFragmentManager().beginTransaction();
                addUserFragment.add(android.R.id.content, AddUserFragment.newInstance(item), "addUserFragment")
                        .addToBackStack("addUserFragment")
                        .commit();

            }
        });

        binding.rcvUser.setAdapter(userAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.rcvUser.setLayoutManager(gridLayoutManager);
        binding.rcvUser.setHasFixedSize(true);
        databaseInstance = DatabaseInstance.getDatabaseInstance(this);
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (page != 1)
                    userViewModel.getAllUserFromServer(page);
            }

        };

        binding.rcvUser.addOnScrollListener(scrollListener);

        userViewModel.getAllUserList().observe(this, new Observer<List<UserModel>>() {
            @Override
            public void onChanged(List<UserModel> userModels) {
                if (userModels != null && userModels.size() > 0) {
                    binding.progressBar.setVisibility(View.GONE);
                    userAdapter.updateAdapter(userModels);
                }
            }
        });
    }


    public void onClickAddUser() {

        FragmentTransaction addUserFragment = getSupportFragmentManager().beginTransaction();
        addUserFragment.add(android.R.id.content, AddUserFragment.newInstance(), "addUserFragment")
                .addToBackStack("addUserFragment")
                .commit();
    }
}
