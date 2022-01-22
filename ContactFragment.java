package uz.bipay.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import uz.bipay.R;
import uz.bipay.application.MyApplication;
import uz.bipay.data.request.ContactRequest;
import uz.bipay.data.response.ContactResponse;


public class ContactFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
             retrofit = MyApplication.getInstance().getMyApplicationComponent().getRetrofitApp();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    Retrofit retrofit;
    ImageView contactLeftBack, contactCancel;
    TextInputLayout inputContactName,inputContactEmail,inputContactMessage;
    TextInputEditText contactEditName,contactEditEmail;
    EditText contactEditMessage;
    String contactName, contactEmail, contactMessage;
    Button btnSendContact;



    public ContactFragment() {
        // Required empty public constructor
    }



    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contactLeftBack = view.findViewById(R.id.contact_back);
        contactCancel = view.findViewById(R.id.contact_cancelIcon);
        inputContactName = view.findViewById(R.id.contactName);
        contactEditName = view.findViewById(R.id.edit_Name);
        inputContactEmail = view.findViewById(R.id.email);
        contactEditEmail = view.findViewById(R.id.editEmail);
        inputContactMessage = view.findViewById(R.id.contactMessage);
        contactEditMessage = view.findViewById(R.id.editMessage);
        btnSendContact = view.findViewById(R.id.btn_sendContact);

        NavController navController = Navigation.findNavController(view);
        contactLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).popBackStack();
            }
        });

        contactCancel.setOnClickListener(v ->
                Navigation.findNavController(getView())
                        .popBackStack(R.id.fragmentHome,false)
        );

        btnSendContact.setOnClickListener(v-> {
            boolean check = (isNameValid() &
                    isEmailValid() &
                    isMessageValid()
            );

            if (check)
                contactRequest();
        });

    }

    private boolean isNameValid(){
        contactName = contactEditName.getText().toString().trim();

        if (contactName.isEmpty()){
            inputContactName.setError("Required*");
            return false;
        }else {
            inputContactName.setError(null);
            return true;
        }
    }

    private boolean isEmailValid(){

        contactEmail = contactEditEmail.getText().toString().trim();

        if (contactEmail.isEmpty()){
            inputContactEmail.setError("Required*");
            return false;

        }else {
            inputContactEmail.setError(null);
            return true;
        }
    }

    private boolean isMessageValid(){
        contactMessage = contactEditMessage.getText().toString().trim();

        if (contactMessage.isEmpty()){
            inputContactMessage.setError("Required*");
            return false;
        }else {
            inputContactMessage.setError(null);
            return true;
        }
    }
    private void contactRequest(){
        ContactRequest request = new ContactRequest(
                contactName,
                contactEmail,
                contactMessage,
                "123",
                "oz");


        retrofit.create(BiPayPlaceHolderApi.class)
                .contactRequest(request)
                .enqueue(new Callback<ContactResponse>() {
                    @Override
                    public void onResponse(Call<ContactResponse> call, Response<ContactResponse> response) {
                        if (response.isSuccessful()){
                            Navigation.findNavController(getView()).popBackStack();
                        }
                    }

                    @Override
                    public void onFailure(Call<ContactResponse> call, Throwable t) {

                    }
                });
    }
}