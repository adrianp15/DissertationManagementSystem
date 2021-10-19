package com.university.dms.repositories;

import com.university.dms.models.User;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface UserRepository extends Repository<User, Integer> {

    // Return all users
    List<User> findAll();

    // Find user by last name
    List<User> findByLastNameContaining(String name);

    //find user by id
    User findClientById(Integer id);

    // Add user to database
    void save(User client);

    // delete user by id
    void deleteById(Integer id);
}
