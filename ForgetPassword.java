package uz.bipay.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import uz.bipay.R;
import uz.bipay.addition.MySettings;
import uz.bipay.application.MyApplication;
import uz.bipay.data.request.RegistrationRequest;
import uz.bipay.data.response.RegistrationResponse;

public class ForgetPassword extends Fragment {

    Retrofit retrofit;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            retrofit = MyApplication.getInstance().getMyApplicationComponent().getRetrofitApp();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forget_password, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText phoneCode = view.findViewById(R.id.forget_phone_code);
        EditText phoneNumber = view.findViewById(R.id.forget_phone_number);
        Button registrationPhoneNumber = view.findViewById(R.id.sendNumber_continue_btn);
        final EditText[] finalPhoneNumber = {phoneNumber};
        NavController navController = Navigation.findNavController(view);

        registrationPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                String finalPhoneNumber = phoneCode.getText().toString() + phoneNumber.getText().toString();
                if (finalPhoneNumber.equals("")){
                    Toast.makeText(getContext(),"Phone Number is Blank",Toast.LENGTH_LONG).show();
                }else if (finalPhoneNumber.length()< 13 || finalPhoneNumber.length() > 13 ){
                    Toast.makeText(getContext(),"You have entered More Or Less numbers ",Toast.LENGTH_LONG).show();
                }else  {
                    requestCode(finalPhoneNumber);
                }
            }

        });

    }

    private void requestCode(String phone){
        System.out.println("in a");
        retrofit.create(BiPayPlaceHolderApi.class)
                .registarationRequest(new RegistrationRequest(phone, "123456", "ru"))
                .enqueue(new Callback<RegistrationResponse>() {
                    @Override
                    public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                        System.out.println("in b");
                        if (response.isSuccessful()){
                            MySettings.getInstance().setPhone(phone);
                            Bundle bundle = new Bundle();
                            bundle.putString("phone",phone);
                            Navigation.findNavController(getView()).navigate(R.id.forgetPassword_to_setNewPassword,bundle);
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                        System.out.println("in c");
                    }
                });
    }

}
