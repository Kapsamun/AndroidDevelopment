package com.mirea.kapsamunelenanikolaevna.retrofitapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    private static final String TAG = "TodoAdapter";

    private final LayoutInflater layoutInflater;
    private final Context context;
    private final ApiService apiService;
    private List<ToDo> todos;

    public TodoAdapter(Context context, List<ToDo> todoList, ApiService apiService) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.todos = todoList;
        this.apiService = apiService;
    }

    public void setTodos(List<ToDo> todos) {
        this.todos = todos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        ToDo todo = todos.get(position);

        holder.textViewTitle.setText(todo.getTitle());
        String imageUrl = "https://picsum.photos/200?random=" + todo.getId();

        Picasso.get()
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .resize(100, 100)
                .centerCrop()
                .into(holder.imageView);

        // Снимаем старый слушатель, чтобы не срабатывал при переиспользовании View
        holder.checkBoxCompleted.setOnCheckedChangeListener(null);
        holder.checkBoxCompleted.setChecked(Boolean.TRUE.equals(todo.getCompleted()));

        holder.checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Обновляем локально
            todo.setCompleted(isChecked);

            // Отправляем PUT на сервер
            Call<ToDo> call = apiService.updateTodo(todo.getId(), todo);
            call.enqueue(new Callback<ToDo>() {
                @Override
                public void onResponse(Call<ToDo> call, Response<ToDo> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context,
                                "Обновлено: id=" + todo.getId() + ", completed=" + isChecked,
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "PUT success: " + response.code());
                    } else {
                        Toast.makeText(context,
                                "Ошибка обновления: " + response.code(),
                                Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "PUT error: " + response.code());
                        // откат чекбокса
                        holder.checkBoxCompleted.setOnCheckedChangeListener(null);
                        holder.checkBoxCompleted.setChecked(!isChecked);
                        todo.setCompleted(!isChecked);
                    }
                }

                @Override
                public void onFailure(Call<ToDo> call, Throwable t) {
                    Toast.makeText(context,
                            "Сеть: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "PUT failure: " + t.getMessage());
                    // откат чекбокса
                    holder.checkBoxCompleted.setOnCheckedChangeListener(null);
                    holder.checkBoxCompleted.setChecked(!isChecked);
                    todo.setCompleted(!isChecked);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return todos == null ? 0 : todos.size();
    }
}