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
import com.planner.family.therapist.data.Stressor;

public class StressorsFragment extends Fragment implements ListView.OnItemClickListener {

    private static final int ADD_STRESSOR_DIALOG_REQUEST_CODE = 1;
    private static final int EDIT_STRESSOR_DIALOG_REQUEST_CODE = 0;

    private ReflectionsViewModel mViewModel;

    private StressorsAdapter mStressorsAdapter;

    public StressorsFragment() {
    }

    public static StressorsFragment newInstance() {
        return new StressorsFragment();
    }

    public void setViewModel(@NonNull final ReflectionsViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStressors();
    }

    private void loadStressors() {
        mViewModel.getStressors().observe(StressorsFragment.this, stressorList -> {
            mStressorsAdapter.setData(stressorList);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stressors_fragment, container, false);

        ListView stressorsListView = rootView.findViewById(R.id.stressors_list_view);

        mStressorsAdapter = new StressorsAdapter(getActivity(),
                R.layout.reflections_item,
                new ArrayList<>());

        stressorsListView.setOnItemClickListener(this);

        stressorsListView.setAdapter(mStressorsAdapter);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_STRESSOR_DIALOG_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String stressor = data.getStringExtra(AddEditReflectionDialogFragment.REFLECTION_KEY);
                mViewModel.saveStressor(stressor);
            }
        }
        if (requestCode == EDIT_STRESSOR_DIALOG_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String stressor = data.getStringExtra(AddEditReflectionDialogFragment.REFLECTION_KEY);
                int stressorId = data.getIntExtra(AddEditReflectionDialogFragment.ID_KEY, -1);
                if (stressor.equals("")) {
                    mViewModel.deleteStressor(stressorId);
                } else {
                    mViewModel.updateStressor(stressorId, stressor);
                }
            }
        }
    }

    public void showAddStressorDialog() {
        FragmentManager fm = getFragmentManager();
        AddEditReflectionDialogFragment addEditReflectionDialogFragment =
                AddEditReflectionDialogFragment.newInstance("Add a stressor", -1, "");
        addEditReflectionDialogFragment.setTargetFragment(this, ADD_STRESSOR_DIALOG_REQUEST_CODE);
        addEditReflectionDialogFragment.show(fm, null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FragmentManager fm = getFragmentManager();
        Stressor stressor = mStressorsAdapter.getItem(i);
        AddEditReflectionDialogFragment addEditReflectionDialogFragment =
                AddEditReflectionDialogFragment.newInstance("Edit a stressor",
                        stressor.getId(),
                        stressor.getStressor());

        addEditReflectionDialogFragment.setTargetFragment(this, EDIT_STRESSOR_DIALOG_REQUEST_CODE);
        addEditReflectionDialogFragment.show(fm, null);
    }
}