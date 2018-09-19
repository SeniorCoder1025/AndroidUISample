package com.example.androidsample.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.androidsample.model.Deliver;

import java.util.List;

@Dao
public interface DeliverDao {

    @Query("SELECT * FROM Deliver ORDER BY id ASC")
    List<Deliver> getAll();

    @Query("SELECT * FROM Deliver ORDER BY id ASC LIMIT :limit OFFSET :offset")
    List<Deliver> getDelivers(int offset, int limit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Deliver... delivers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Deliver deliver);

    @Query("DELETE FROM Deliver")
    void deleteAll();
}
