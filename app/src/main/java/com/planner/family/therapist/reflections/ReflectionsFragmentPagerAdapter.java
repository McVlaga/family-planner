package com.planner.family.therapist.reflections;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ReflectionsFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final int NUMBER_OF_TABS = 4;

    private Context mContext;
    private ReflectionsViewModel mViewModel;

    public ReflectionsFragmentPagerAdapter(Context context, FragmentManager fm, ReflectionsViewModel viewModel) {
        super(fm);
        mContext = context;
        mViewModel = viewModel;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            SupportsFragment fragment = SupportsFragment.newInstance();
            fragment.setViewModel(mViewModel);
            return fragment;
        } else if (position == 1) {
            StressorsFragment fragment = StressorsFragment.newInstance();
            fragment.setViewModel(mViewModel);
            return fragment;
        } else if (position == 2) {
            GoalsFragment fragment = GoalsFragment.newInstance();
            fragment.setViewModel(mViewModel);
            return fragment;
        } else {
            AchievementsFragment fragment = AchievementsFragment.newInstance();
            fragment.setViewModel(mViewModel);
            return fragment;
        }
    }

    @Override
    public int getCount() {
        return NUMBER_OF_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Supports";
            case 1:
                return "Stressors";
            case 2:
                return "Goals";
            case 3:
                return "Achievements";
            default:
                return null;
        }
    }

}
