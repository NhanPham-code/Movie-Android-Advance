package com.example.ojtaadaassignment12.presentation.views.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.ojtaadaassignment12.R;
import com.example.ojtaadaassignment12.databinding.FragmentEditProfileBinding;
import com.example.ojtaadaassignment12.di.MyApplication;
import com.example.ojtaadaassignment12.domain.models.UserProfile;
import com.example.ojtaadaassignment12.presentation.viewmodels.UserProfileViewModel;
import com.example.ojtaadaassignment12.util.Base64Helper;

import java.util.Calendar;

import javax.inject.Inject;


public class EditProfileFragment extends Fragment {

    private static final int CAMERA_REQUEST_CODE = 300;
    private static final int GALLERY_REQUEST_CODE = 400;

    private int currentAction; // to check current action (camera or gallery)

    Base64Helper helper;

    FragmentEditProfileBinding binding;

    String fullName;
    String email;
    String birthday;
    String gender;
    String avatarBase64;

    @Inject
    UserProfileViewModel userProfileViewModel;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new Base64Helper();

        // inject view model
        ((MyApplication) requireActivity().getApplication()).appComponent.injectEditProfileFragment(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);

        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_container);

        // fill user profile data to edit text
        UserProfile userProfile = userProfileViewModel.getUserProfileMutableLiveData().getValue();
        if (userProfile != null) {
            binding.fullName.setText(userProfile.getFullName());
            binding.email.setText(userProfile.getEmail());
            binding.birthday.setText(userProfile.getBirthday());

            if (userProfile.getGender().equalsIgnoreCase("Male")) {
                binding.maleRadioButton.setChecked(true);
            } else {
                binding.femaleRadioButton.setChecked(true);
            }

            binding.avatarImage.setImageBitmap(helper.convertBase64ToBitmap(userProfile.getAvatarBase64()));
            avatarBase64 = userProfile.getAvatarBase64();
        }

        // avatar click click
        binding.avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open menu to choose camera or gallery
                showPopupMenu(view);
            }
        });

        binding.birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open date picker
                showDatePickerDialog();
            }
        });

        // onclick cancel button
        binding.cancelButton.setOnClickListener(v -> {
            // navigate back to profile fragment
            navController.popBackStack();
        });

        // onclick save button
        binding.doneButton.setOnClickListener(v -> {
            fullName = binding.fullName.getText().toString();
            email = binding.email.getText().toString();
            birthday = binding.birthday.getText().toString();

            if (binding.maleRadioButton.isChecked()) {
                gender = "Male";
            } else {
                gender = "Female";
            }

            // save user profile to firebase
            UserProfile newUserProfile = new UserProfile(fullName, email, birthday, gender, avatarBase64);
            userProfileViewModel.setUserProfileToFirebase(newUserProfile);
            userProfileViewModel.setUserProfile(newUserProfile);

            // navigate back to profile fragment
            navController.popBackStack();
        });

        return binding.getRoot();
    }


    /**
     * Show DatePickerDialog for birthday
     */
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);  // Giá trị từ 0 đến 11
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                String selectedDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;
                binding.birthday.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    // Hiển thị Popup Menu
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        // Set OnMenuItemClickListener cho PopupMenu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.camera) {
                    openCamera();
                    return true;
                } else if (itemId == R.id.gallery) {
                    openGallery();
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show(); // Hiển thị PopupMenu
    }

    /**
     * Request permission to access camera or gallery
     */
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (currentAction == CAMERA_REQUEST_CODE) {
                        openCamera();
                    } else if (currentAction == GALLERY_REQUEST_CODE) {
                        openGallery();
                    }
                } else {
                    Toast.makeText(requireActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    /**
     * Receive data from camera
     */
    private ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        binding.avatarImage.setImageBitmap(photo);
                        avatarBase64 = helper.convertBitmapToBase64(photo); // Lưu ảnh dưới dạng Base64
                    }
                }
            });

    /**
     * Open camera to take a photo
     */
    private void openCamera() {
        if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(intent);
        } else {
            currentAction = CAMERA_REQUEST_CODE;
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    /**
     * Receive data from gallery
     */
    private ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri selectedImage = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
                            binding.avatarImage.setImageBitmap(bitmap);
                            avatarBase64 = helper.convertBitmapToBase64(bitmap); // Lưu ảnh dưới dạng Base64
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    /**
     * Open gallery to choose a photo
     */
    private void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 trở lên
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryLauncher.launch(intent);
            } else {
                currentAction = GALLERY_REQUEST_CODE;
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {
            // Android dưới 13
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryLauncher.launch(intent);
            } else {
                currentAction = GALLERY_REQUEST_CODE;
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }
}