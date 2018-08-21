package com.planner.family.therapist.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import com.planner.family.therapist.data.Child;
import com.planner.family.therapist.data.ChildrenRepository;
import com.planner.family.therapist.data.Contact;

public class NetworkViewModel extends ViewModel {

    private final ChildrenRepository mChildrenRepository;

    private MutableLiveData<Child> mChild = new MutableLiveData<>();

    private LiveData<List<Contact>> mContactsList = new MutableLiveData<>();

    public NetworkViewModel(ChildrenRepository childrenRepository) {
        mChildrenRepository = childrenRepository;
        mContactsList = Transformations.switchMap(mChild, (Child newChild) ->
                mChildrenRepository.getContactsByChildId(newChild.getId()));
    }

    public LiveData<List<Contact>> getContacts() {
        return mContactsList;
    }

    public void saveContact(String name, String number, int type) {
        mChildrenRepository.saveContact(new Contact(name, number, type, mChild.getValue().getId()));
    }

    public void updateContact(int contactId, String name, String number, int type) {
        mChildrenRepository.updateContact(new Contact(contactId, name, number, type, mChild.getValue().getId()));
    }

    public void deleteContact(int contactId) {
        mChildrenRepository.deleteContactById(contactId);
    }

    public void setChild(Child child) {
        if (child != null)
            mChild.setValue(child);
    }
}
