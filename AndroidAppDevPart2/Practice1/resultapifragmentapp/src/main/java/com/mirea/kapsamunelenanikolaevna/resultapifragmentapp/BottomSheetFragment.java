package com.mirea.kapsamunelenanikolaevna.resultapifragmentapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener(
                "requestKey",
                this,
                (requestKey, bundle) -> {
                    String text = bundle.getString("key");
                    Log.d(BottomSheetFragment.class.getSimpleName(), "Get text " + text);

                    View view = getView();
                    if (view != null && text != null) {
                        TextView tv = view.findViewById(R.id.textViewResult);
                        tv.setText("Получено: " + text);
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }
}