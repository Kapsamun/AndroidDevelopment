package com.mirea.kapsamunelenanikolaevna.employeedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Employee {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public int salary;
}

//Аннотация «@Entity» способствует созданию таблицы с именем, совпадающим с именем класса.