package com.planner.family.therapist.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "contacts", foreignKeys = @ForeignKey(
        entity = Child.class,
        parentColumns = "id",
        childColumns = "child_id",
        onDelete = CASCADE))
public class Contact {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @NonNull
    @ColumnInfo(name = "number")
    public String number;

    @NonNull
    @ColumnInfo(name = "type")
    public int type;

    @NonNull
    @ColumnInfo(name = "child_id")
    public final String childId;

    @Ignore
    public Contact(@NonNull String name, @NonNull String number, @NonNull int type, @NonNull String childId) {
        this.name = name;
        this.number = number;
        this.type = type;
        this.childId = childId;
    }

    public Contact(@NonNull int id, @NonNull String name, @NonNull String number, @NonNull int type, @NonNull String childId) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.type = type;

        this.childId = childId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getNumber() {
        return number;
    }

    public void setNumber(@NonNull String number) {
        this.number = number;
    }

    @NonNull
    public int getType() {
        return type;
    }

    public void setType(@NonNull int type) {
        this.type = type;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public String getChildId() {
        return childId;
    }

    public String toString() {
        return name + " " + number;
    }
}
