package com.planner.family.therapist.resources;

import android.arch.lifecycle.ViewModel;

import com.planner.family.therapist.data.ChildrenRepository;

public class ResourcesViewModel extends ViewModel {

    private final ChildrenRepository mChildrenRepository;

    public ResourcesViewModel(ChildrenRepository childrenRepository) {
        mChildrenRepository = childrenRepository;
    }
}
