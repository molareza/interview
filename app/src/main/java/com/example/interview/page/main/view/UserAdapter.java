package com.example.interview.page.main.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.interview.R;
import com.example.interview.databinding.AdapterUserBinding;
import com.example.interview.page.main.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private ArrayList<UserModel> items;
    private Context context;
    private OnClickAdapter onClickAdapter;

    public UserAdapter(ArrayList<UserModel> items, Context context, OnClickAdapter onClickAdapter) {
        this.items = items;
        this.context = context;
        this.onClickAdapter = onClickAdapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserModel item = items.get(position);
        if (item.getAvatar() != null) {
            Glide.with(context)
                    .load(item.getAvatar())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(holder.itemBinding.profileImage);
        }else {
            Glide.with(context)
                    .load(R.drawable.placeholder)
                    .centerCrop()
                    .into(holder.itemBinding.profileImage);
        }
        holder.itemBinding.txtName.setText("First Name: " + item.getFirstName());
        holder.itemBinding.txtLastName.setText("Last Name: " + item.getLastName());
        holder.itemBinding.txtEmail.setText("Email: " + item.getEmail());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AdapterUserBinding itemBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemBinding = AdapterUserBinding.bind(itemView);
            itemBinding.imgMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            PopupMenu menu = new PopupMenu(context, v);
            menu.getMenu().add(Menu.NONE, 1, 1, "Edit");
            menu.getMenu().add(Menu.NONE, 2, 2, "Delete");
            menu.show();

            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    int i = item.getItemId();
                    if (i == 1) {
                        onClickAdapter.onEditItem(getAdapterPosition(), items.get(getAdapterPosition()));
                        return true;
                    } else if (i == 2) {
                        onClickAdapter.onDeleteItem(getAdapterPosition(), items.get(getAdapterPosition()));
                        return true;
                    } else {
                        return false;
                    }
                }

            });


        }
    }

    public void updateAdapter(List<UserModel> items) {
        this.items = (ArrayList<UserModel>) items;
        notifyDataSetChanged();

    }

    public interface OnClickAdapter {
        void onDeleteItem(int po, UserModel item);
        void onEditItem(int po, UserModel item);
    }

}
