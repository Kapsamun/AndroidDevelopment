package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mirea.kapsamunelenanikolaevna.celebritybase.R;
import com.squareup.picasso.Picasso;

public class CelebrityDetailFragment extends Fragment {

    private static final String ARG_ID = "id";
    private static final String ARG_NAME = "name";
    private static final String ARG_PHOTO = "photo";
    private static final String ARG_BIO = "bio";

    public static CelebrityDetailFragment newInstance(int id, String name, String photo, String bio) {
        CelebrityDetailFragment f = new CelebrityDetailFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_ID, id);
        b.putString(ARG_NAME, name);
        b.putString(ARG_PHOTO, photo);
        b.putString(ARG_BIO, bio);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_celebrity_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView image = view.findViewById(R.id.imageViewDetailPhoto);
        TextView name = view.findViewById(R.id.textViewDetailName);
        TextView bio = view.findViewById(R.id.textViewDetailBio);
        Button back = view.findViewById(R.id.buttonBack);

        Bundle args = getArguments();
        if (args != null) {
            name.setText(args.getString(ARG_NAME, ""));
            bio.setText(args.getString(ARG_BIO, ""));
            String photo = args.getString(ARG_PHOTO, "");
            if (photo != null && !photo.isEmpty()) {
                Picasso.get()
                        .load(photo)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(image);
            }
        }

        back.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
    }
}