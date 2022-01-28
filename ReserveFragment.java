package uz.bipay.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.bipay.Adapter.ReserveCardAdapter;
import uz.bipay.R;
import uz.bipay.application.MyApplication;
import uz.bipay.data.model.PaymentServiceModel;
import uz.bipay.data.model.ReserveServiceModel;

public class ReserveFragment extends Fragment {

    private RecyclerView reserveCardRecyclerView;
    private ReserveCardAdapter reserveCardAdapter;
    private RecyclerView.LayoutManager reserveCardLayoutManager;
    private BiPayPlaceHolderApi api;

    ImageView selectedFromLogo,reservedToLogo;
    TextView selectedFromName,reservedPaymentName,reservedToMoney;
   // private final ArrayList<PaymentServiceModel> cardList;
    String fromCardName = "";
    String fromCardLogo = "";
    int fromCardId = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            api =  MyApplication.getInstance()
                    .getMyApplicationComponent()
                    .getRetrofitApp()
                    .create(BiPayPlaceHolderApi.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (getArguments() != null){
            if (getArguments().containsKey("fromId")){
                fromCardId =getArguments().getInt("fromId");
            }

            if(getArguments().containsKey("fromName")){
                fromCardName = getArguments().getString("fromName");
            }

            if (getArguments().containsKey("fromLogo")){
                fromCardLogo = getArguments().getString("fromLogo");
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reserve, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reservedPaymentName = view.findViewById(R.id.reservedCardName);
        reservedToMoney = view.findViewById(R.id.reservedMoney);
        reservedToLogo = view.findViewById(R.id.reservedCardImage);

        reserveCardRecyclerView = view.findViewById(R.id.recycleview_reservedMoney);
        reserveCardLayoutManager = new LinearLayoutManager(getContext());
        reserveCardRecyclerView.setHasFixedSize(true);
        reserveCardAdapter = new ReserveCardAdapter(getContext(),new ArrayList<ReserveServiceModel>());
        reserveCardAdapter.setFromCardData(fromCardId,fromCardName,fromCardLogo);
        reserveCardRecyclerView.setLayoutManager(reserveCardLayoutManager);
        reserveCardRecyclerView.setAdapter(reserveCardAdapter);

        selectedFromLogo = view.findViewById(R.id.selected_reservedCardImage);
        selectedFromName = view.findViewById(R.id.selected_reservedCardName);

        //reserveCardAdapter.setFromCardData();

        Glide.with(this)
                .load(fromCardLogo)
                .into(selectedFromLogo);
        selectedFromName.setText(fromCardName);
        if (fromCardId != -1)
            getReserveRequest(fromCardId);
    }

    private void getReserveRequest(int fromCardId){
        api.getreserveService(fromCardId).enqueue(new Callback<List<ReserveServiceModel>>() {
            @Override
            public void onResponse(Call<List<ReserveServiceModel>> call, Response<List<ReserveServiceModel>> response) {
                if(response.isSuccessful() || response.body() != null){
                    reserveCardAdapter.addItems(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ReserveServiceModel>> call, Throwable t) {

            }
        });
    }

}