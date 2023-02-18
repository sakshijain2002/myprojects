package com.learning.repository;

import com.learning.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
//	@Query(value = "select email from student" ,nativeQuery = true)
//	List<String> findAllEmails();

    @Query(value = "select name from student", nativeQuery = true)
    List<String> findAllName();

    @Query(value = "select s.contactDetails from StudentEntity s")
    List<Long> findAllContactDetails();

    @Query(value = "select s.email from StudentEntity s")
    List<String> findAllEmails();


}



