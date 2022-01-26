package uz.bipay.ui.fragment;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class LanguageBottomSheetFragment extends BottomSheetDialog {
    public LanguageBottomSheetFragment(@NonNull Context context) {
        super(context);
    }

    public LanguageBottomSheetFragment(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected LanguageBottomSheetFragment(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
