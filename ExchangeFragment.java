package uz.bipay.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.airbnb.lottie.parser.IntegerParser;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import uz.bipay.R;
import uz.bipay.application.MyApplication;
import uz.bipay.data.request.ExchangeRequest;
import uz.bipay.data.response.ExchangeResponse;

public class ExchangeFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            retrofit = MyApplication.getInstance().getMyApplicationComponent().getRetrofitApp();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private TextInputLayout inputFromAccount, inputToAccount, inputSumma, inputFirstName, inputLastName, inputPhoneNumber;
    Retrofit retrofit;
    String fromAccount, toAccount, fromSumma, firstName, lastName, phoneNumber;
    Button payment;
    TextView fromCardTV, toCardTV;
    ImageView fromCardImage, toCardImage;

    int reserveMoney;
    int fromCardId, toCardId,fromReserveSumma;
    String fromCardName = "", toCardName = "";
    String fromCardLogo = "", toCardLogo = "";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_exchange, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputFromAccount = view.findViewById(R.id.input_fromAccount);
        inputToAccount = view.findViewById(R.id.input_toAccount);
        inputSumma = view.findViewById(R.id.input_summa);
        inputFirstName = view.findViewById(R.id.input_firstName);
        inputLastName = view.findViewById(R.id.input_lastName);
        inputPhoneNumber = view.findViewById(R.id.input_phoneNumber);
        payment = view.findViewById(R.id.payment);

        fromCardImage = view.findViewById(R.id.fromCardImage);
        fromCardTV = view.findViewById(R.id.fromCardName);

        toCardTV = view.findViewById(R.id.toCardName);
        toCardImage = view.findViewById(R.id.toCardImage);

        if (getArguments() != null && getArguments().containsKey("fromId")) {
            fromCardId = getArguments().getInt("fromId");
        }

        if (getArguments() != null && getArguments().containsKey("fromLogo")) {
            fromCardLogo = getArguments().getString("fromLogo");
            Glide.with(getContext())
                    .load(fromCardLogo)
                    .into(fromCardImage);
        }

        if (getArguments() != null && getArguments().containsKey("fromName")) {
            fromCardName = getArguments().getString("fromName");
            fromCardTV.setText(fromCardName);
        }
        if (getArguments() != null && getArguments().containsKey("toId")) {
            toCardId = getArguments().getInt("toId");
        }

        if (getArguments() != null && getArguments().containsKey("toName")) {
            toCardName = getArguments().getString("toName");
            toCardTV.setText(toCardName);
        }

        if (getArguments() != null && getArguments().containsKey("toLogo")) {
            toCardLogo = getArguments().getString("toLogo");
            Glide.with(getContext())
                    .load(toCardLogo)
                    .into(toCardImage);
        }

        if(getArguments() != null && getArguments().containsKey("reserveMoney")){
            reserveMoney = getArguments().getInt("reserveMoney");
        }


        NavController navController = Navigation.findNavController(view);
        payment.setOnClickListener(v -> {
            boolean check = (isFromAccountValid() &
                    isToAccountValid() &
                    isSummaValid() &
                    isFirstNameValid() &
                    isLastNameValid() &
                    isPhoneNumberValid()
            );
            if (check)
                exchangeRequest();

        });

    }


    private void exchangeRequest() {
        ExchangeRequest request = new ExchangeRequest(
                fromSumma,
                fromAccount,
                1,
                toAccount,
                firstName,
                lastName,
                "Jahongir",
                "998" + phoneNumber,
                1,
                "oz"
        );

        retrofit.create(BiPayPlaceHolderApi.class)
                .exchangeRequest(request)
                .enqueue(new Callback<ExchangeResponse>() {
                    @Override
                    public void onResponse(Call<ExchangeResponse> call, Response<ExchangeResponse> response) {
                        if (response.isSuccessful()) {
                            Navigation.findNavController(getView())
                                    .navigate(R.id.exchangeFragment_to_finishingTransferenceFragment);
                        }else if (response.code() == 422){
                            //inputSumma.setError("pls enter more money");
                            try {
                                inputSumma.setError(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ExchangeResponse> call, Throwable t) {

                    }
                });
    }

    private boolean isFromAccountValid() {

        fromAccount = inputFromAccount.getEditText().getText().toString().trim();

        if (fromAccount.isEmpty()) {
            inputFromAccount.setError("Required*");
            return false;
        } else if (fromAccount.length() < 16) {
            inputFromAccount.setError("Less");
            return false;
        } else {
            inputFromAccount.setError(null);
            return true;
        }
    }

    private boolean isToAccountValid() {

        toAccount = inputToAccount.getEditText().getText().toString().trim();

        if (toAccount.isEmpty()) {
            inputToAccount.setError("Required*");
            return false;
        } else if (toAccount.length() < 16) {
            inputToAccount.setError("Less");
            return false;
        } else {
            inputToAccount.setError(null);
            return true;
        }
    }

    private boolean isSummaValid() {
        fromSumma = inputSumma.getEditText().getText().toString().trim();
        if (fromSumma.isEmpty()) {
            inputSumma.setError("Required*");
            return false;
        }
        fromReserveSumma = Integer.valueOf(fromSumma);
        inputSumma.setError(null);
        return true;
    }

    private boolean isFirstNameValid() {

        firstName = inputFirstName.getEditText().getText().toString().trim();

        if (firstName.isEmpty()) {
            inputFirstName.setError("Required*");
            return false;
        } else {
            inputFirstName.setError(null);
            return true;
        }
    }

    private boolean isLastNameValid() {

        lastName = inputLastName.getEditText().getText().toString().trim();

        if (lastName.isEmpty()) {
            inputLastName.setError("Required*");
            return false;
        } else {
            inputLastName.setError(null);
            return true;
        }
    }

    private boolean isPhoneNumberValid() {

        phoneNumber = inputPhoneNumber.getEditText().getText().toString().trim();

        if (phoneNumber.isEmpty()) {
            inputPhoneNumber.setError("Required*");
            return false;
        } else if (phoneNumber.length() < 9) {
            inputPhoneNumber.setError("Less");
            return false;
        } else {
            inputPhoneNumber.setError(null);
            return true;

        }
    }
}
