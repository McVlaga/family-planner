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
import com.planner.family.therapist.data.Goal;

public class GoalsFragment extends Fragment implements ListView.OnItemClickListener {

    private static final int ADD_GOAL_DIALOG_REQUEST_CODE = 1;
    private static final int EDIT_GOAL_DIALOG_REQUEST_CODE = 0;

    private ReflectionsViewModel mViewModel;

    private GoalsAdapter mGoalsAdapter;

    public GoalsFragment() {
    }

    public static GoalsFragment newInstance() {
        return new GoalsFragment();
    }

    public void setViewModel(@NonNull final ReflectionsViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadGoals();
    }

    private void loadGoals() {
        mViewModel.getGoals().observe(GoalsFragment.this, goalsList -> {
            mGoalsAdapter.setData(goalsList);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.goals_fragment, container, false);

        ListView goalsListView = rootView.findViewById(R.id.goals_list_view);

        mGoalsAdapter = new GoalsAdapter(getActivity(),
                R.layout.reflections_item,
                new ArrayList<>());

        goalsListView.setOnItemClickListener(this);

        goalsListView.setAdapter(mGoalsAdapter);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_GOAL_DIALOG_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String goal = data.getStringExtra(AddEditReflectionDialogFragment.REFLECTION_KEY);
                mViewModel.saveGoal(goal);
            }
        }
        if (requestCode == EDIT_GOAL_DIALOG_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String goal = data.getStringExtra(AddEditReflectionDialogFragment.REFLECTION_KEY);
                int goalId = data.getIntExtra(AddEditReflectionDialogFragment.ID_KEY, -1);
                if (goal.equals("")) {
                    mViewModel.deleteGoal(goalId);
                } else {
                    mViewModel.updateGoal(goalId, goal);
                }
            }
        }
    }

    public void showAddGoalDialog() {
        FragmentManager fm = getFragmentManager();
        AddEditReflectionDialogFragment addEditReflectionDialogFragment =
                AddEditReflectionDialogFragment.newInstance("Add a goal", -1, "");
        addEditReflectionDialogFragment.setTargetFragment(this, ADD_GOAL_DIALOG_REQUEST_CODE);
        addEditReflectionDialogFragment.show(fm, null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FragmentManager fm = getFragmentManager();
        Goal goal = mGoalsAdapter.getItem(i);
        AddEditReflectionDialogFragment addEditReflectionDialogFragment =
                AddEditReflectionDialogFragment.newInstance("Edit a goal",
                        goal.getId(),
                        goal.getGoal());

        addEditReflectionDialogFragment.setTargetFragment(this, EDIT_GOAL_DIALOG_REQUEST_CODE);
        addEditReflectionDialogFragment.show(fm, null);
    }
}
