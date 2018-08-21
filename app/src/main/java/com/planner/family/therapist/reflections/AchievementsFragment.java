package com.planner.family.therapist.reflections;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import com.planner.family.therapist.R;
import com.planner.family.therapist.data.Achievement;

public class AchievementsFragment extends Fragment implements ListView.OnItemClickListener {

    private static final int ADD_ACHIEVEMENT_DIALOG_REQUEST_CODE = 1;
    private static final int EDIT_ACHIEVEMENT_DIALOG_REQUEST_CODE = 0;

    private ReflectionsViewModel mViewModel;

    private AchievementsAdapter mAchievementsAdapter;

    public AchievementsFragment() {
    }

    public static AchievementsFragment newInstance() {
        return new AchievementsFragment();
    }

    public void setViewModel(@NonNull final ReflectionsViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAchievements();
    }

    private void loadAchievements() {
        mViewModel.getAchievements().observe(AchievementsFragment.this, achievementList -> {
            mAchievementsAdapter.setData(achievementList);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.achievements_fragment, container, false);

        ListView achievementsListView = rootView.findViewById(R.id.achievements_list_view);

        mAchievementsAdapter = new AchievementsAdapter(getActivity(),
                R.layout.reflections_item,
                new ArrayList<>());

        achievementsListView.setOnItemClickListener(this);

        achievementsListView.setAdapter(mAchievementsAdapter);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ACHIEVEMENT_DIALOG_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String achievement = data.getStringExtra(AddEditReflectionDialogFragment.REFLECTION_KEY);
                mViewModel.saveAchievement(achievement);
            }
        }
        if (requestCode == EDIT_ACHIEVEMENT_DIALOG_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String achievement = data.getStringExtra(AddEditReflectionDialogFragment.REFLECTION_KEY);
                int achievementId = data.getIntExtra(AddEditReflectionDialogFragment.ID_KEY, -1);
                if (achievement.equals("")) {
                    mViewModel.deleteAchievement(achievementId);
                } else {
                    mViewModel.updateAchievement(achievementId, achievement);
                }
            }
        }
    }

    public void showAddAchievementDialog() {
        FragmentManager fm = getFragmentManager();
        AddEditReflectionDialogFragment addEditReflectionDialogFragment =
                AddEditReflectionDialogFragment.newInstance("Add an achievement", -1, "");
        addEditReflectionDialogFragment.setTargetFragment(this, ADD_ACHIEVEMENT_DIALOG_REQUEST_CODE);
        addEditReflectionDialogFragment.show(fm, null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        FragmentManager fm = getFragmentManager();
        Achievement achievement = mAchievementsAdapter.getItem(i);
        AddEditReflectionDialogFragment addEditReflectionDialogFragment =
                AddEditReflectionDialogFragment.newInstance("Edit an achievement",
                        achievement.getId(),
                        achievement.getAchievement());

        addEditReflectionDialogFragment.setTargetFragment(this, EDIT_ACHIEVEMENT_DIALOG_REQUEST_CODE);
        addEditReflectionDialogFragment.show(fm, null);
    }
}
