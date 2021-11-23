package com.university.dms.service.user;

import com.university.dms.model.AccountType;
import com.university.dms.model.user.Role;
import com.university.dms.model.user.User;
import com.university.dms.repository.user.RoleRepository;
import com.university.dms.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setAccountType(AccountType.STUDENT);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    public List<User> coordinatorSearchUser(String data) {
        Set<User> users = new HashSet<>();

        List<User> results = userRepository.findByFirstNameLike(data);

        if(results != null) {
            users.addAll(results);
        }

        results = userRepository.findByLastNameLike(data);

        if(results != null) {
            users.addAll(results);
        }

        results = userRepository.findByEmailLike(data);

        if(results != null) {
            users.addAll(results);
        }

        results = userRepository.findByUserNameLike(data);

        if(results != null) {
            users.addAll(results);
        }

        results = new ArrayList<>(users);

        return results;
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User findUserById(int id) {
        return userRepository.findUserById(id);
    }

    public List<User> getSupervisors() {
        return userRepository.findUserByAccountType(AccountType.SUPERVISOR);
    }
}