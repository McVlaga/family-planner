package com.planner.family.therapist.reflections;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import com.planner.family.therapist.data.Achievement;
import com.planner.family.therapist.data.Child;
import com.planner.family.therapist.data.ChildrenRepository;
import com.planner.family.therapist.data.Goal;
import com.planner.family.therapist.data.Stressor;
import com.planner.family.therapist.data.Support;

public class ReflectionsViewModel extends ViewModel {

    private final ChildrenRepository mChildrenRepository;

    private MutableLiveData<Child> mChild = new MutableLiveData<>();

    private LiveData<List<Stressor>> mStressorsList = new MutableLiveData<>();
    private LiveData<List<Support>> mSupportsList = new MutableLiveData<>();
    private LiveData<List<Achievement>> mAchievementsList = new MutableLiveData<>();
    private LiveData<List<Goal>> mGoalsList = new MutableLiveData<>();

    public ReflectionsViewModel(ChildrenRepository childrenRepository) {
        mChildrenRepository = childrenRepository;
        mStressorsList = Transformations.switchMap(mChild, (Child newChild) ->
                mChildrenRepository.getStressorsByChildId(newChild.getId()));
        mSupportsList = Transformations.switchMap(mChild, (Child newChild) ->
                mChildrenRepository.getSupportsByChildId(newChild.getId()));
        mAchievementsList = Transformations.switchMap(mChild, (Child newChild) ->
                mChildrenRepository.getAchievementsByChildId(newChild.getId()));
        mGoalsList = Transformations.switchMap(mChild, (Child newChild) ->
                mChildrenRepository.getGoalsByChildId(newChild.getId()));
    }

    public LiveData<List<Stressor>> getStressors() {
        return mStressorsList;
    }

    public LiveData<List<Support>> getSupports() {
        return mSupportsList;
    }

    public LiveData<List<Achievement>> getAchievements() {
        return mAchievementsList;
    }

    public LiveData<List<Goal>> getGoals() {
        return mGoalsList;
    }

    public void saveStressor(String stressor) {
        mChildrenRepository.saveStressor(new Stressor(stressor, mChild.getValue().getId()));
    }

    public void updateStressor(int stressorId, String stressor) {
        mChildrenRepository.updateStressor(new Stressor(stressorId, stressor, mChild.getValue().getId()));
    }

    public void deleteStressor(int stressorId) {
        mChildrenRepository.deleteStressorById(stressorId);
    }

    public void saveSupport(String support) {
        mChildrenRepository.saveSupport(new Support(support, mChild.getValue().getId()));
    }

    public void updateSupport(int supportsId, String support) {
        mChildrenRepository.updateSupport(new Support(supportsId, support, mChild.getValue().getId()));
    }

    public void deleteSupport(int supportId) {
        mChildrenRepository.deleteSupportById(supportId);
    }

    public void saveAchievement(String achievement) {
        mChildrenRepository.saveAchievement(new Achievement(achievement, mChild.getValue().getId()));
    }

    public void updateAchievement(int achievementId, String achievement) {
        mChildrenRepository.updateAchievement(new Achievement(achievementId, achievement, mChild.getValue().getId()));
    }

    public void deleteAchievement(int achievementId) {
        mChildrenRepository.deleteAchievementById(achievementId);
    }

    public void saveGoal(String goal) {
        mChildrenRepository.saveGoal(new Goal(goal, mChild.getValue().getId()));
    }

    public void updateGoal(int goalId, String goal) {
        mChildrenRepository.updateGoal(new Goal(goalId, goal, mChild.getValue().getId()));
    }

    public void deleteGoal(int goalId) {
        mChildrenRepository.deleteGoalById(goalId);
    }

    public void setChild(Child child) {
        if (child != null)
            mChild.setValue(child);

    }

    public LiveData<Child> getChild() {
        return mChild;
    }
}
