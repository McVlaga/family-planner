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
import com.planner.family.therapist.data.Support;

public class SupportsFragment extends Fragment implements ListView.OnItemClickListener {

    private static final int ADD_SUPPORT_DIALOG_REQUEST_CODE = 0;
    private static final int EDIT_SUPPORT_DIALOG_REQUEST_CODE = 1;

    private ReflectionsViewModel mViewModel;

    private SupportsAdapter mSupportsAdapter;

    public SupportsFragment() {
    }

    public static SupportsFragment newInstance() {
        return new SupportsFragment();
    }

    public void setViewModel(@NonNull final ReflectionsViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSupports();
    }

    private void loadSupports() {
        mViewModel.getSupports().observe(this, supportList -> {
            mSupportsAdapter.setData(supportList);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.supports_fragment, container, false);

        ListView supportsListView = rootView.findViewById(R.id.supports_list_view);
        mSupportsAdapter = new SupportsAdapter(getActivity(),
                R.layout.reflections_item,
                new ArrayList<>());

        supportsListView.setOnItemClickListener(this);

        supportsListView.setAdapter(mSupportsAdapter);

        return rootView;
    }

    public void showAddSupportDialog() {
        FragmentManager fm = getFragmentManager();
        AddEditReflectionDialogFragment addEditReflectionDialogFragment =
                AddEditReflectionDialogFragment.newInstance("Add a support", -1, "");
        addEditReflectionDialogFragment.setTargetFragment(this, ADD_SUPPORT_DIALOG_REQUEST_CODE);
        addEditReflectionDialogFragment.show(fm, null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FragmentManager fm = getFragmentManager();
        Support support = mSupportsAdapter.getItem(i);
        AddEditReflectionDialogFragment addEditReflectionDialogFragment =
                AddEditReflectionDialogFragment.newInstance("Edit a support",
                        support.getId(),
                        support.getSupport());

        addEditReflectionDialogFragment.setTargetFragment(this, EDIT_SUPPORT_DIALOG_REQUEST_CODE);
        addEditReflectionDialogFragment.show(fm, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_SUPPORT_DIALOG_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String support = data.getStringExtra(AddEditReflectionDialogFragment.REFLECTION_KEY);
                mViewModel.saveSupport(support);
            }
        }
        if (requestCode == EDIT_SUPPORT_DIALOG_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String support = data.getStringExtra(AddEditReflectionDialogFragment.REFLECTION_KEY);
                int supportId = data.getIntExtra(AddEditReflectionDialogFragment.ID_KEY, -1);
                if (support.equals("")) {
                    mViewModel.deleteSupport(supportId);
                } else {
                    mViewModel.updateSupport(supportId, support);
                }
            }
        }
    }
}