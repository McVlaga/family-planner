package com.planner.family.therapist.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.planner.family.therapist.data.Child;
import com.planner.family.therapist.data.ChildrenRepository;

import java.util.List;

public class ChildrenViewModel extends ViewModel {

    private final ChildrenRepository mChildrenRepository;

    private LiveData<List<Child>> mChildrenList;

    private final MutableLiveData<Child> mSelectedChild = new MutableLiveData<>();

    public ChildrenViewModel(ChildrenRepository childrenRepository) {
        mChildrenRepository = childrenRepository;
        mChildrenList = mChildrenRepository.getChildren();
    }

    public LiveData<List<Child>> getChildren() {
        return mChildrenList;
    }

    public void selectChildAt(int index) {
        mSelectedChild.setValue(mChildrenList.getValue().get(index));
    }

    public LiveData<Child> getSelectedChild() {
        return mSelectedChild;
    }

    public void saveChild(Child child) {
        mChildrenRepository.saveChild(child);
    }

    public void updateChild(Child child) {
        mChildrenRepository.updateChild(child);
    }

    public void deleteChild(Child child){
        mChildrenRepository.deleteChild(child);
    }

    public void setSelectedChildEmpty() {
        mSelectedChild.setValue(null);
    }
}
