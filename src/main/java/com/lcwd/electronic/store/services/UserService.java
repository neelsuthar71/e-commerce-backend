package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    //create
    UserDto createUser(UserDto userDto);

    //update
    UserDto updatedUser (UserDto userDto,String userId);

    //delete
    void deleteUser (String userId);

    //getAll users
    PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

    //getSingle user by id
    UserDto getUserById(String UserId);

    //getSingle user by email
    UserDto getUserByEmail(String email);

    //search user
    List<UserDto> searchUser(String keyword);

    //find by gender
    List<UserDto> searchUserByGender(String gender);


    //other user specific  tasks
}
