package com.planner.family.therapist.data;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

public class ChildrenRepository {

    private static volatile ChildrenRepository INSTANCE;

    private final ChildrenDatabase mChildrenDatabase;

    private ChildrenRepository(@NonNull ChildrenDatabase childrenDatabase) {
        mChildrenDatabase = childrenDatabase;
    }

    public static ChildrenRepository getInstance(@NonNull ChildrenDatabase childrenDatabase) {
        if (INSTANCE == null) {
            synchronized (ChildrenRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ChildrenRepository(childrenDatabase);
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<List<Child>> getChildren() {
        return mChildrenDatabase.childrenDao().getChildren();
    }

    public void saveChild(final Child child) {
        AsyncTask.execute(() -> mChildrenDatabase.childrenDao().insert(child));
    }

    public void updateChild(final Child child) {
        AsyncTask.execute(() -> mChildrenDatabase.childrenDao().update(child));
    }

    public void deleteChild(final Child child) {
        if (child == null) return;
        AsyncTask.execute(() -> mChildrenDatabase.childrenDao().delete(child));
    }

    public LiveData<List<Stressor>> getStressorsByChildId(String childId) {
        return mChildrenDatabase.stressorsDao().getStressorsByChildId(childId);
    }

    public void saveStressor(final Stressor stressor) {
        AsyncTask.execute(() -> mChildrenDatabase.stressorsDao().insert(stressor));
    }

    public void updateStressor(final Stressor stressor) {
        AsyncTask.execute(() -> mChildrenDatabase.stressorsDao().update(stressor));
    }

    public void deleteStressorById(int id) {
        AsyncTask.execute(() -> mChildrenDatabase.stressorsDao().deleteStressorById(id));
    }

    public LiveData<List<Support>> getSupportsByChildId(String childId) {
        return mChildrenDatabase.supportsDao().getSupportsByChildId(childId);
    }

    public void saveSupport(final Support support) {
        AsyncTask.execute(() -> mChildrenDatabase.supportsDao().insert(support));
    }

    public void updateSupport(final Support support) {
        AsyncTask.execute(() -> mChildrenDatabase.supportsDao().update(support));
    }

    public void deleteSupportById(int id) {
        AsyncTask.execute(() -> mChildrenDatabase.supportsDao().deleteSupportById(id));
    }

    public LiveData<List<Achievement>> getAchievementsByChildId(String childId) {
        return mChildrenDatabase.achievementsDao().getAchievementsByChildId(childId);
    }

    public void saveAchievement(final Achievement achievement) {
        AsyncTask.execute(() -> mChildrenDatabase.achievementsDao().insert(achievement));
    }

    public void updateAchievement(final Achievement achievement) {
        AsyncTask.execute(() -> mChildrenDatabase.achievementsDao().update(achievement));
    }

    public void deleteAchievementById(int id) {
        AsyncTask.execute(() -> mChildrenDatabase.achievementsDao().deleteAchievementById(id));
    }

    public LiveData<List<Goal>> getGoalsByChildId(String childId) {
        return mChildrenDatabase.goalsDao().getGoalsByChildId(childId);
    }

    public void saveGoal(final Goal goal) {
        AsyncTask.execute(() -> mChildrenDatabase.goalsDao().insert(goal));
    }

    public void updateGoal(final Goal goal) {
        AsyncTask.execute(() -> mChildrenDatabase.goalsDao().update(goal));
    }

    public void deleteGoalById(int id) {
        AsyncTask.execute(() -> mChildrenDatabase.goalsDao().deleteGoalById(id));
    }

    public void saveContact(final Contact contact) {
        AsyncTask.execute(() -> mChildrenDatabase.contactsDao().insert(contact));
    }

    public void updateContact(final Contact contact) {
        AsyncTask.execute(() -> mChildrenDatabase.contactsDao().update(contact));
    }

    public void deleteContactById(int id) {
        AsyncTask.execute(() -> mChildrenDatabase.contactsDao().deleteContactById(id));
    }

    public LiveData<List<Contact>> getContactsByChildId(String childId) {
        return mChildrenDatabase.contactsDao().getContactsByChildId(childId);
    }
}
