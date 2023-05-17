package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exception.ResourcesNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.TypeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
public class UserServiceImpl implements UserService {


    @Autowired
    private ModelMapper mapper;
    @Autowired
    private UserRepository userRepository;
    @Value("${user.profile.image.path}")
    private String path;
    @Override
    public UserDto createUser(UserDto userDto) {



        //generate unique id in string format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);

        User user=dtoToEntity(userDto);//dto to entity

        User savedUser =userRepository.save(user);//isko chaiye user entity  hum laye hai user entity convert karo
        userDto=entityToDto(savedUser);//entity to dto
        return userDto;
    }


    @Override
    public UserDto updatedUser(UserDto userDto, String userId) {
        User user=
        userRepository.findById(userId).orElseThrow(()-> new ResourcesNotFoundException("this user does not exist with given id"));
        user.setName(userDto.getName());
        //we are not updating email
//        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        user.setImageName(userDto.getImageName());

        User updatedUser=userRepository.save(user);

        UserDto updatedDto=entityToDto(updatedUser);

        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user=
                userRepository.findById(userId).orElseThrow(()-> new ResourcesNotFoundException("this user does not exist with given id"));
        userRepository.delete(user);
        //        user.getImageName();
        //delete image profile path first
        String fullPath=path+user.getImageName();

      try {
          Path paths = Paths.get(fullPath);
          Files.delete(paths);
      }catch (NoSuchFileException ex){

      } catch (IOException e) {
          throw new RuntimeException(e);
      }


    }

    @Override
    public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {

        //sort by
//        Sort sort=Sort.by(sortBy);
        Sort sort=
        (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());

        //paging
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page=userRepository.findAll(pageable);
//        List<User> users=page.getContent();

//        List<User> users =userRepository.findAll();
//        List<UserDto> dtoList=users.stream().map((user -> entityToDto(user))).collect(Collectors.toList());
//        PageableResponse<UserDto> response=new PageableResponse<>();
//        response.setContent(dtoList);
//        response.setPageNumber(page.getNumber());
//        response.setPageSize(page.getSize());
//        response.setTotalElements(page.getTotalElements());
//        response.setTotalPages(page.getTotalPages());
//        response.setLastPage(page.isLast());
        //carry forward to helper class for creating function reusable code

        PageableResponse<UserDto> response=Helper.getPageableResponse(page,UserDto.class);

        return response;
//        return dtoList;
    }

    @Override
    public UserDto getUserById(String userId) {

        User user=userRepository.findById(userId).orElseThrow(()->new ResourcesNotFoundException("user not found with given id"));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user=
        userRepository.findByEmail(email).orElseThrow(()-> new ResourcesNotFoundException("this email does not exist"));
        return entityToDto(user);
    }


    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users=
        userRepository.findByNameContaining(keyword);

        List<UserDto> dtoList=users.stream().map((user -> entityToDto(user))).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<UserDto> searchUserByGender(String gender) {
        List<User> users=
                userRepository.findByGender(gender);
        List<UserDto> dtoList=users.stream().map((user -> entityToDto(user))).collect(Collectors.toList());
        return dtoList;

    }


    private UserDto entityToDto(User savedUser) {
//        UserDto userDto=UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .about(savedUser.getPassword())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName())
//                .build();
//        return userDto;

        return mapper.map(savedUser, UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
//        User user=User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();
//        return user;
        return mapper.map(userDto,User.class);
    }


}
