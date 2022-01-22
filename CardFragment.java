package uz.bipay.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import uz.bipay.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment {

    CardView addingCardView;
    ImageView cardBack, cardCancel;
    public static CardFragment newInstance(String param1, String param2) {
        CardFragment fragment = new CardFragment();
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
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addingCardView = view.findViewById(R.id.addingCardView);
        cardBack = view.findViewById(R.id.addingCard_back);
        cardCancel = view.findViewById(R.id.cards_cancelIcon);

        cardBack.setOnClickListener(v -> Navigation.findNavController(getView()).popBackStack());
        cardCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).popBackStack(R.id.homeFragment,false);
            }
        });
        addingCardView.setOnClickListener(v ->
                Navigation.findNavController(getView())
                        .navigate(R.id.cardFragment_to_addingCardFragment));
    }
}