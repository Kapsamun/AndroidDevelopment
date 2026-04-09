package com.mirea.kapsamunelenanikolaevna.androidappdev4;

import static com.mirea.kapsamunelenanikolaevna.androidappdev4.databinding.FragmentMusicFragmentbindingBinding.*;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mirea.kapsamunelenanikolaevna.androidappdev4.databinding.FragmentMusicFragmentbindingBinding;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link music_fragmentbinding#newInstance} factory method to
 * create an instance of this fragment.
 */
public class music_fragmentbinding extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentMusicFragmentbindingBinding binding;

    public music_fragmentbinding() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment music_fragmentbinding.
     */
    // TODO: Rename and change types and number of parameters
    public static music_fragmentbinding newInstance(String param1, String param2) {
        music_fragmentbinding fragment = new music_fragmentbinding();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_music_fragmentbinding, container, false);
            binding = FragmentMusicFragmentbindingBinding.inflate(inflater, container, false);
            View root = binding.getRoot();
            final TextView textView = binding.MusicFragmenttextView;
            return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
