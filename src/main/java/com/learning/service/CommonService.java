package com.learning.service;

import java.util.List;

public interface CommonService<T, ID> {

    List<T> getAllRecords();

    List<T> getLimitedRecords(int count);

    List<T> getSortedRecords(String sortBy);

    T saveRecord(T record);

    List<T> saveAll(List<T> recordList);

    T getRecordById(ID id);

    void deleteRecordById(ID id);

    T updateRecordById(ID id, T record);

}



