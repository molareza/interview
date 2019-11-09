package com.example.interview.page.add_user.view;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.interview.R;
import com.example.interview.databinding.FragmentAddUserBinding;
import com.example.interview.page.add_user.viewModel.AddUserViewModel;
import com.example.interview.page.main.model.UserModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddUserFragment extends Fragment {

    AddUserViewModel addUserViewModel;
    FragmentAddUserBinding binding;
    String avatar = "";
    private final int CAMERA_PIC_REQUEST = 1000;
    private final int GALLERY_PIC_REQUEST = 2000;
    private static final String IMAGE_DIRECTORY = "/avatar";
    private boolean isEdit = false;
    private UserModel userModel;

    public AddUserFragment() {
        // Required empty public constructor
    }

    public static AddUserFragment newInstance() {
        AddUserFragment addUserFragment = new AddUserFragment();
        Bundle bundle = new Bundle();
        addUserFragment.setArguments(bundle);
        return addUserFragment;
    }

    public static AddUserFragment newInstance(UserModel user) {
        AddUserFragment addUserFragment = new AddUserFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER", user);
        addUserFragment.setArguments(bundle);
        return addUserFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_user, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            isEdit = true;
            userModel = (UserModel) getArguments().getSerializable("USER");
        }

        if (isEdit && userModel != null) {

            binding.txtToolbar.setText("Edit user");
            binding.edtFirstName.getEditText().setText(userModel.getFirstName());
            binding.edtLastName.getEditText().setText(userModel.getLastName());
            binding.edtEmail.getEditText().setText(userModel.getEmail());
            avatar = userModel.getAvatar();
            Glide.with(getActivity())
                    .load(avatar)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(binding.imgProfile);

        }

        addUserViewModel = ViewModelProviders.of(this).get(AddUserViewModel.class);
        binding.setModel(this);


    }

    public void onBackClick() {

        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
        }

    }

    public void onSaveClick() {
        String firstName = binding.edtFirstName.getEditText().getText().toString();
        String lastNmae = binding.edtLastName.getEditText().getText().toString();
        String email = binding.edtEmail.getEditText().getText().toString();
        if (firstName.isEmpty()) {
            binding.edtFirstName.setErrorEnabled(true);
            binding.edtFirstName.setError("please enter your first name");
            return;
        }
        if (lastNmae.isEmpty()) {
            binding.edtFirstName.setErrorEnabled(false);
            binding.edtLastName.setErrorEnabled(true);
            binding.edtLastName.setError("please enter your last name");
            return;
        }
        if (email.isEmpty()) {
            binding.edtLastName.setErrorEnabled(false);
            binding.edtEmail.setErrorEnabled(true);
            binding.edtEmail.setError("please enter your email name");
            return;
        }
        if (avatar.isEmpty()) {
            binding.edtEmail.setErrorEnabled(false);
            Toast.makeText(getActivity(), "please select a avatar", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEdit && userModel !=null){

            userModel.setFirstName(firstName);
            userModel.setLastName(lastNmae);
            userModel.setEmail(email);
            userModel.setAvatar(avatar);
            addUserViewModel.editUser(userModel);

        }
        addUserViewModel.insertUser(avatar, firstName, lastNmae, email);


        onBackClick();
    }

    public void onAddAvatarClick() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                showCustomDialog();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {


            }
        }).check();
    }

    private void showCustomDialog() {

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, GALLERY_PIC_REQUEST);
                                dialog.dismiss();
                                break;
                            case 1:
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                                dialog.dismiss();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getActivity(), "please try later", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == CAMERA_PIC_REQUEST) {
            try {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                avatar = saveImage(image);
                showImage(image);
            } catch (NullPointerException e) {
            }
        } else if (requestCode == GALLERY_PIC_REQUEST) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    avatar = saveImage(image);
                    showImage(image);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showImage(Bitmap image) {
        Glide.with(getActivity())
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(binding.imgProfile);
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        if (!wallpaperDirectory.exists()) {  // have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


}
