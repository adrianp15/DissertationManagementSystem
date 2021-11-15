package com.university.dms.repository;

import com.university.dms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUserName(String userName);

    List<User> findAll();

    User findUserById(int id);

    @Query("FROM User u WHERE u.firstName LIKE %:firstName%")
    List<User> findByFirstNameLike(@Param("firstName") String firstName);

    @Query("FROM User u WHERE u.lastName LIKE %:lastName%")
    List<User> findByLastNameLike(@Param("lastName") String lastName);

    @Query("FROM User u WHERE u.email LIKE %:email%")
    List<User> findByEmailLike(@Param("email") String email);

    @Query("FROM User u WHERE u.userName LIKE %:userName%")
    List<User> findByUserNameLike(@Param("userName") String userName);


}