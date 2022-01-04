package uz.bipay.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import uz.bipay.R;
import uz.bipay.addition.MySettings;
import uz.bipay.application.MyApplication;
import uz.bipay.data.request.LoginRequest;
import uz.bipay.data.response.LoginResponse;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login<Textview> extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            retrofit = MyApplication.getInstance().getMyApplicationComponent().getRetrofitApp();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    TextView logPhoneNumber,logPassword;
    Button signIn;
    Retrofit retrofit;

    public Login() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logPhoneNumber = view.findViewById(R.id.login_phone_number);
        logPassword = view.findViewById(R.id.login_password);
        signIn = view.findViewById(R.id.signIn_continue_btn);
        String finalLogPhoneNumber = logPhoneNumber.getText().toString();
        String finalLogPassword = logPassword.getText().toString();

        TextView forgetPassword = view.findViewById(R.id.forgetPassword);
        String forgetPass = "Forget Password";
        SpannableString spannableString = new SpannableString(forgetPass);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(getContext(),"Code was sent to your phone number",Toast.LENGTH_LONG).show();
            }
        };

        spannableString.setSpan(clickableSpan,0,15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgetPassword.setMovementMethod(LinkMovementMethod.getInstance());
        forgetPassword.setText(spannableString);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.login_to_forgetPassword);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalLogPhoneNumber.equals("") && finalLogPassword.equals("")){
                    Toast.makeText(getContext(),"Please fill the field",Toast.LENGTH_LONG).show();
                }else{
                    signIn(finalLogPhoneNumber,finalLogPassword);
                }
            }
        });

    }


    private void signIn(String phoneNumber,String password){
        retrofit.create(BiPayPlaceHolderApi.class)
                .loginRequest(new LoginRequest(phoneNumber,password,"ru"))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if(response.isSuccessful()){
                            MySettings.getInstance().setPhone(phoneNumber);
                            Bundle bundle = new Bundle();
                            bundle.putString("phone number",phoneNumber);
                            Navigation.findNavController(getView()).navigate(R.id.setPasswordFragment_to_login,bundle);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {

                    }
                });
    }
}