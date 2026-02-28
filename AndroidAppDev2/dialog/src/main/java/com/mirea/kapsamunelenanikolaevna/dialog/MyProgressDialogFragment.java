package com.mirea.kapsamunelenanikolaevna.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class MyProgressDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Создаем ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setTitle("Загрузка");
        progressDialog.setMessage("Пожалуйста, подождите...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false); // Запрещаем закрытие диалога нажатием вне области

        // Используем Handler для автоматического закрытия диалога через 3 секунды
        new Handler().postDelayed(() -> {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }, 3000);

        return progressDialog;
    }
}
