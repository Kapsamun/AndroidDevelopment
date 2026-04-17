package com.mirea.kapsamunelenanikolaevna.employeedb;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mirea.kapsamunelenanikolaevna.employeedb.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppDatabase db = App.getInstance().getDatabase();
        EmployeeDao employeeDao = db.employeeDao();

        // Создание сотрудника и запись его в БД
        Employee employee = new Employee();
        employee.id = 1;
        employee.name = "John Smith";
        employee.salary = 10000;
        employeeDao.insert(employee);

        // Получение всех сотрудника из БД
        List<Employee> employees = employeeDao.getAll();

        // Получение определенного сотрудника с id = 1
        employee = employeeDao.getById(1);

        // Обновление полей записи
        employee.salary = 20000;
        employeeDao.update(employee);

        Log.d(TAG, employee.name + " " + employee.salary);
    }
}