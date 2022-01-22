package uz.bipay.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import uz.bipay.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FinishingTransferenceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinishingTransferenceFragment extends Fragment {


    Button btn_backingHomePage;

    public FinishingTransferenceFragment() {
        // Required empty public constructor
    }


    public static FinishingTransferenceFragment newInstance(String param1, String param2) {
        FinishingTransferenceFragment fragment = new FinishingTransferenceFragment();
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
        return inflater.inflate(R.layout.fragment_finishing_transference, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_backingHomePage = view.findViewById(R.id.btn_backingHomePage);

        btn_backingHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backingHomePage();
            }
        });
    }

    private void backingHomePage() {
        Navigation.findNavController(getView()).navigate(R.id.finishingTransferenceFragment_to_homeFragment);
    }
}