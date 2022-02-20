package com.ariaramin.radioava.ui.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ariaramin.radioava.Adapters.Music.AllMusicPagerAdapter;
import com.ariaramin.radioava.MainActivity;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.FragmentAllMusicsBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AllMusicsFragment extends Fragment {

    FragmentAllMusicsBinding allMusicsBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
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
        allMusicsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_musics, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        mainActivity.bottomNavigationView.setVisibility(View.GONE);
        mainActivity.homeImageView.setVisibility(View.GONE);
        allMusicsBinding.backStackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        setupTabLayout();
        return allMusicsBinding.getRoot();
    }

    private void setupTabLayout() {
        AllMusicPagerAdapter adapter = new AllMusicPagerAdapter(this);
        allMusicsBinding.allMusicsViewPager.setAdapter(adapter);

        new TabLayoutMediator(allMusicsBinding.allMusicsTabLayout, allMusicsBinding.allMusicsViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                String title;
                if (position == 0) {
                    title = requireContext().getResources().getString(R.string.trending);
                } else if (position == 1) {
                    title = requireContext().getResources().getString(R.string.popular);
                } else {
                    title = requireContext().getResources().getString(R.string.albums);
                }
                tab.setText(title);
            }
        }).attach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
        mainActivity.homeImageView.setVisibility(View.VISIBLE);
        compositeDisposable.clear();
    }
}