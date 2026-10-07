package com.mirea.kapsamunelenanikolaevna.retrofitapp;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // Получить весь список дел
    @GET("todos")
    Call<List<ToDo>> getTodos();

    // Обновление Todo (для задания с CheckBox)
    @PUT("todos/{id}")
    Call<ToDo> updateTodo(@Path("id") int id, @Body ToDo todo);
}