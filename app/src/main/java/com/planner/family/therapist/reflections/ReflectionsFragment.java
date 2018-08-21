package com.planner.family.therapist.reflections;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planner.family.therapist.R;
import com.planner.family.therapist.data.Child;
import com.planner.family.therapist.home.ChildrenViewModel;

public class ReflectionsFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton mAddReflectionButton;
    private ViewPager mReflectionsViewPager;

    private ReflectionsViewModel mViewModel;
    private ChildrenViewModel mSharedViewModel;

    public ReflectionsFragment() {

    }

    public static ReflectionsFragment newInstance() {
        return new ReflectionsFragment();
    }

    public void setViewModel(@NonNull ReflectionsViewModel viewModel) {
        mViewModel = viewModel;
    }

    public void setSharedViewModel(@NonNull ChildrenViewModel viewModel) {
        mSharedViewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reflections_fragment, container, false);

        mReflectionsViewPager = rootView.findViewById(R.id.reflections_viewpager);

        ReflectionsFragmentPagerAdapter adapter =
                new ReflectionsFragmentPagerAdapter(getActivity(), getChildFragmentManager(), mViewModel);

        mReflectionsViewPager.setAdapter(adapter);

        TabLayout tabLayout = rootView.findViewById(R.id.reflections_tabs);
        tabLayout.setupWithViewPager(mReflectionsViewPager);

        mAddReflectionButton = rootView.findViewById(R.id.add_reflection_button);
        mAddReflectionButton.setOnClickListener(this);

        loadChild();

        return rootView;
    }

    private void loadChild() {
        mSharedViewModel.getSelectedChild().observe(this, child -> mViewModel.setChild(child));
    }

    @Override
    public void onClick(View view) {

        Child child = mSharedViewModel.getSelectedChild().getValue();
        if (child == null) {
            return;
        }

        Object fragment = mReflectionsViewPager
                .getAdapter()
                .instantiateItem(mReflectionsViewPager, mReflectionsViewPager.getCurrentItem());
        if (fragment instanceof SupportsFragment) {
            SupportsFragment supportsFragment = (SupportsFragment) fragment;
            supportsFragment.showAddSupportDialog();
        } else if (fragment instanceof StressorsFragment) {
            StressorsFragment stressorsFragment = (StressorsFragment) fragment;
            stressorsFragment.showAddStressorDialog();
        } else if (fragment instanceof GoalsFragment) {
            GoalsFragment goalsFragment = (GoalsFragment) fragment;
            goalsFragment.showAddGoalDialog();
        } else {
            AchievementsFragment achievementsFragment = (AchievementsFragment) fragment;
            achievementsFragment.showAddAchievementDialog();
        }
    }
}
