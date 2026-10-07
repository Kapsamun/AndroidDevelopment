package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mirea.kapsamunelenanikolaevna.celebritybase.R;
import com.squareup.picasso.Picasso;

public class CelebrityDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_PHOTO = "extra_photo";
    public static final String EXTRA_BIO = "extra_bio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrity_detail);

        ImageView imageView = findViewById(R.id.imageViewDetailPhoto);
        TextView textName = findViewById(R.id.textViewDetailName);
        TextView textBio = findViewById(R.id.textViewDetailBio);
        Button buttonBack = findViewById(R.id.buttonBack);

        String name = getIntent().getStringExtra(EXTRA_NAME);
        String photo = getIntent().getStringExtra(EXTRA_PHOTO);
        String bio = getIntent().getStringExtra(EXTRA_BIO);

        textName.setText(name != null ? name : "");
        textBio.setText(bio != null ? bio : "");

        if (photo != null && !photo.isEmpty()) {
            Picasso.get()
                    .load(photo)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_foreground);
        }

        buttonBack.setOnClickListener(v -> finish());
    }
}