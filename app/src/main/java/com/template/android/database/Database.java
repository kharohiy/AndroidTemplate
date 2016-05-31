package com.template.android.database;

import android.database.Cursor;

import java.util.List;

public interface Database<T> {

    long addElement(T element);

    List<Long> addElements(List<T> elements);

    T getElement(long id);

    List<T> getElements();

    List<T> getElements(String where);

    void updateElement(T element);

    void updateElements(List<T> elements);

    long deleteElement(long id);

    void deleteElements(String where);

    void deleteElements();

    List<T> queryElements(String query);

    Cursor queryCursor(String query);

    boolean hasElement(long id);

    int getCount();
}
