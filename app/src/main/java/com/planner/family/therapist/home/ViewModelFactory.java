package com.planner.family.therapist.home;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.planner.family.therapist.info.ChildInfoViewModel;
import com.planner.family.therapist.network.NetworkViewModel;
import com.planner.family.therapist.reflections.ReflectionsViewModel;
import com.planner.family.therapist.data.ChildrenRepository;
import com.planner.family.therapist.resources.ResourcesViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final ChildrenRepository mChildrenRepository;

    public ViewModelFactory(ChildrenRepository childrenRepository) {
        mChildrenRepository = childrenRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChildrenViewModel.class)) {
            return (T) new ChildrenViewModel(mChildrenRepository);
        } else if(modelClass.isAssignableFrom(ReflectionsViewModel.class)) {
            return (T) new ReflectionsViewModel(mChildrenRepository);
        } else if(modelClass.isAssignableFrom(ChildInfoViewModel.class)) {
            return (T) new ChildInfoViewModel(mChildrenRepository);
        } else if(modelClass.isAssignableFrom(ResourcesViewModel.class)) {
            return (T) new ResourcesViewModel(mChildrenRepository);
        } else if(modelClass.isAssignableFrom(NetworkViewModel.class)) {
            return (T) new NetworkViewModel(mChildrenRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}