package com.mirea.kapsamunelenanikolaevna.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class MyTimeDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Получаем текущее время
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Создаем TimePickerDialog и возвращаем его
        return new TimePickerDialog(requireActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Показываем Toast с выбранным временем
        String time = hourOfDay + ":" + String.format("%02d", minute);
        Toast.makeText(getActivity(), "Вы выбрали время: " + time, Toast.LENGTH_LONG).show();
    }
}