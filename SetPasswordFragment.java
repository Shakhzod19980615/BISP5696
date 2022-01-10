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
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import uz.bipay.R;
import uz.bipay.addition.MySettings;
import uz.bipay.application.MyApplication;
import uz.bipay.data.request.SetPasswordRequest;
import uz.bipay.data.response.SetPasswordResponse;


public class SetPasswordFragment extends Fragment {
    String phone = "";
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



    public SetPasswordFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SetPasswordFragment newInstance(String param1, String param2) {
        SetPasswordFragment fragment = new SetPasswordFragment();
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
        return inflater.inflate(R.layout.fragment_set_password, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText password = view.findViewById(R.id.password);
        EditText confirmPassword = view.findViewById(R.id.confirm_password);

        NavController navController = Navigation.findNavController(view);

        Button setPassword = view.findViewById(R.id.pass_continue_btn);

        setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalPassword = password.getText().toString();
                String finalConfirmPassword = confirmPassword.getText().toString();
                
                if (finalPassword.equals("") || finalConfirmPassword.equals("")){
                    Toast.makeText(getContext(),"Please enter password",Toast.LENGTH_LONG).show();

                }else{
                    setPassword(finalPassword);
                }

            }
        });
    }

    private void setPassword(String setPassword){
        retrofit.create(BiPayPlaceHolderApi.class)
                .setPasswordRequest(new SetPasswordRequest(setPassword,phone,"ru"))
                .enqueue(new Callback<SetPasswordResponse>() {
                    @Override
                    public void onResponse(Call<SetPasswordResponse> call, Response<SetPasswordResponse> response) {
                        if (response.isSuccessful()){
                            MySettings.getInstance().setToken(setPassword);
                            Bundle bundle = new Bundle();
                            bundle.putString("Password", setPassword);
                            Navigation.findNavController(requireView()).navigate(R.id.setPasswordFragment_to_login  , bundle);
                        }
                    }

                    @Override
                    public void onFailure(Call<SetPasswordResponse> call, Throwable t) {

                    }
                });
    }
}