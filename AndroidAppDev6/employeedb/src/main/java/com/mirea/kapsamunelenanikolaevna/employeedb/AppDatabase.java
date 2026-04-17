package com.mirea.kapsamunelenanikolaevna.employeedb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Employee.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EmployeeDao employeeDao();
}

// создание абстрактного класса базы данных,
// наследуемого от «RoomDatabase» и отвечающего за ведение самой базы данных и
//предоставление экземпляров DAO