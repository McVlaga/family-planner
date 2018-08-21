package com.planner.family.therapist.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity(tableName = "children")
public class Child {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private final String id;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "disability")
    private String disability;

    @ColumnInfo(name = "bio")
    private String bio;

    @ColumnInfo(name = "birthday_millis")
    private long birthdayMillis;

    @ColumnInfo(name = "image_path")
    private String imagePath;

    /**
     * Use this constructor to create a new child.
     */
    @Ignore
    public Child(String firstName, String lastName, String disability,
                 String bio, long birthdayMillis, String imagePath) {
        this(UUID.randomUUID().toString(), firstName, lastName,
                disability, bio, birthdayMillis, imagePath);
    }

    /**
     * Use this constructor to create a child if the child already has an id (copy of another
     * child).
     */
    public Child(@NonNull String id, String firstName, String lastName,
                 String disability, String bio, long birthdayMillis, String imagePath) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.disability = disability;
        this.bio = bio;
        this.birthdayMillis = birthdayMillis;
        this.imagePath = imagePath;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getBirthdayMillis() {
        return birthdayMillis;
    }

    public void setBirthdayMillis(long birthdayMillis) {
        this.birthdayMillis = birthdayMillis;
    }

    public String getDisability() {
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
