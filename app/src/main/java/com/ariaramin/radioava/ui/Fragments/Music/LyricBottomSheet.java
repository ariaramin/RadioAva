package com.ariaramin.radioava.ui.Fragments.Music;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.LyricBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class LyricBottomSheet extends BottomSheetDialogFragment {

    LyricBottomSheetBinding lyricBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        lyricBinding = DataBindingUtil.inflate(inflater, R.layout.lyric_bottom_sheet, container, false);
        Bundle args = getArguments();
        String lyric = "";
        if (args != null) {
            lyric = args.getString("Lyric");
        } else {
            onDestroy();
            Toast.makeText(requireActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        lyricBinding.lyricTextView.setText(lyric);
        return lyricBinding.getRoot();
    }
}