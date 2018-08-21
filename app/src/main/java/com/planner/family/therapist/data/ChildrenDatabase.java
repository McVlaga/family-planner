package com.planner.family.therapist.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

@Database(entities = {Child.class, Stressor.class, Support.class, Achievement.class, Goal.class, Contact.class}, version = 7)
public abstract class ChildrenDatabase extends RoomDatabase {

    private static ChildrenDatabase INSTANCE;

    public abstract ChildrenDao childrenDao();
    public abstract SupportsDao supportsDao();
    public abstract StressorsDao stressorsDao();
    public abstract AchievementsDao achievementsDao();
    public abstract GoalsDao goalsDao();
    public abstract ContactsDao contactsDao();

    static final Migration MIGRATION_5_6 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

        }
    };

    public static ChildrenDatabase getChildDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(),
                            ChildrenDatabase.class,
                            "Children.db")
                            //.addMigrations(MIGRATION_5_6)
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
