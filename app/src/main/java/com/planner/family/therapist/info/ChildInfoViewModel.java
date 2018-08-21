package com.planner.family.therapist.info;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.planner.family.therapist.data.Child;
import com.planner.family.therapist.data.ChildrenRepository;

public class ChildInfoViewModel extends ViewModel {

    private final ChildrenRepository mChildrenRepository;

    private MutableLiveData<Child> mChild = new MutableLiveData<>();

    public ChildInfoViewModel(ChildrenRepository childrenRepository) {
        mChildrenRepository = childrenRepository;
    }

    public void setChild(Child child) {
        if(child != null) mChild.setValue(child);
    }

    public LiveData<Child> getChild() {
        return mChild;
    }
}
