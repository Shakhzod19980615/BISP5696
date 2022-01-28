package uz.bipay.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import uz.bipay.R;
import uz.bipay.addition.MySettings;

import static android.app.Activity.RESULT_OK;


public class PersonalProfilFragment extends Fragment {

    int SELECT_PHOTO = 1;
    Uri uri;
    ImageView profileImage, profileBack, profileCancel;
    TextInputLayout profileFirstName, profileLastName, profilePhoneNumber;
    TextInputEditText profileEditFirstName, profileEditLastName, profileEditPhoneNumber;
    Button btnSaveProfile;
    TextView profileEditedFirstName, profileEditedPhoneNumber, profileEditedLastName;


    private boolean checkOnOff;

    String firstName, lastName, phoneNumber, fullName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal_profil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileBack = view.findViewById(R.id.profil_back);
        profileCancel = view.findViewById(R.id.profil_cancelIcon);
        profileImage = view.findViewById(R.id.addingUserImage);
        profileEditedFirstName = view.findViewById(R.id.editedFirstName);
        profileEditedLastName = view.findViewById(R.id.editedLastName);
        profileEditedPhoneNumber = view.findViewById(R.id.settingPhoneNumber);
        profileFirstName = view.findViewById(R.id.profilFirstName);
        profileLastName = view.findViewById(R.id.profilLastName);
        profilePhoneNumber = view.findViewById(R.id.profil_phoneNumber);
        btnSaveProfile = view.findViewById(R.id.btn_saveProfile);
        profileEditFirstName = view.findViewById(R.id.edit_firstName);
        profileEditLastName = view.findViewById(R.id.edit_lastName);
        profileEditPhoneNumber = view.findViewById(R.id.edit_phoneNumber);

        loadData();

        profileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.personalProfilFragment_to_settingsFragment);
            }
        });

        profileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.personalProfilFragment_to_homeFragment);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PHOTO);

            }
        });

        btnSaveProfile.setOnClickListener(v -> {
            saveData();
//            onActivityResult(SELECT_PHOTO, RESULT_OK, null);
        });
    }

    public void saveData() {
        changeFirstName();
        changeLastName();
        changePhoneNumber();
        Navigation.findNavController(getView()).navigate(R.id.personalProfilFragment_to_settingsFragment);
    }

    public void loadData() {
        firstName = MySettings.getInstance().getFirstName();
        lastName = MySettings.getInstance().getLastName();
        phoneNumber = MySettings.getInstance().getPhone();
        updateData();
    }

    private void changeFirstName() {
        firstName = profileEditFirstName.getText().toString().trim();
        if (firstName.isEmpty()) {
            profileFirstName.setError("Empty");
        } else {
            MySettings.getInstance().setFirstName(firstName);
            profileFirstName.setError(null);
        }
    }

    private void changeLastName() {
        lastName = profileEditLastName.getText().toString().trim();
        if (lastName.isEmpty()) {
            profileLastName.setError("Empty");
        } else {
            MySettings.getInstance().setLastName(lastName);
            profileLastName.setError(null);
        }
    }

    private void changePhoneNumber() {
        phoneNumber = profileEditPhoneNumber.getText().toString().trim();
        if (phoneNumber.isEmpty()) {
            profilePhoneNumber.setError("Empty");
        } else if (phoneNumber.length() < 9) {
            profilePhoneNumber.setError("Wrong number");
        } else {
            profileEditPhoneNumber.setError(null);
            MySettings.getInstance().setPhone(phoneNumber);
        }
    }

    public void updateData() {
        profileEditFirstName.setText(firstName);
        profileEditLastName.setText(lastName);
        profileEditPhoneNumber.setText(phoneNumber);
        
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            final File file = new File(String.valueOf(data.getData()));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                profileImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}