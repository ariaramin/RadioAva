package com.ariaramin.radioava.ui.Fragments.Video;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ariaramin.radioava.Adapters.Video.AllVideosPagerAdapter;
import com.ariaramin.radioava.MainActivity;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.FragmentAllVideosBinding;
import com.google.android.material.tabs.TabLayoutMediator;


public class AllVideosFragment extends Fragment {

    FragmentAllVideosBinding videosBinding;
    MainViewModel mainViewModel;
    MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        videosBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_videos, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainActivity.bottomNavigationView.setVisibility(View.GONE);
        mainActivity.homeImageView.setVisibility(View.GONE);
        videosBinding.backStackButton.setOnClickListener(v -> requireActivity().onBackPressed());
        videosBinding.searchButton.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_allVideosFragment_to_searchFragment)
        );
        setupTabLayout();
        return videosBinding.getRoot();
    }

    private void setupTabLayout() {
        AllVideosPagerAdapter adapter = new AllVideosPagerAdapter(this);
        videosBinding.allVideosViewPager.setAdapter(adapter);

        new TabLayoutMediator(videosBinding.allVideosTabLayout, videosBinding.allVideosViewPager, (tab, position) -> {
            String title;
            if (position == 0) {
                title = requireContext().getResources().getString(R.string.trending);
            } else {
                title = requireContext().getResources().getString(R.string.popular);
            }
            tab.setText(title);
        }).attach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
        mainActivity.homeImageView.setVisibility(View.VISIBLE);
    }
}