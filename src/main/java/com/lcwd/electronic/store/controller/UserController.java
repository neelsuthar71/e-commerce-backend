package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


    private Logger logger= LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;


    @Value("${user.profile.image.path}")
    private String imageUploadPath;


    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser( @Valid @RequestBody UserDto userDto){
//        return userDto;
        UserDto createUserDto = userService.createUser(userDto);
        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }


    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @PathVariable("userId") String userId,@RequestBody UserDto userDto){
        UserDto updatedUserDto=userService.updatedUser(userDto,userId);
        return new ResponseEntity<>(updatedUserDto,HttpStatus.OK);
    }


    //delete
    //we should never use string use we need to send JSON as file instead
//    @DeleteMapping("/{userId}")
//    public ResponseEntity<String> deleteUSer(@PathVariable("userId") String userId)
//    {
//        userService.deleteUser(userId);
//        return new ResponseEntity<>("user is deleted successfully",HttpStatus.OK);
//    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUSer(@PathVariable("userId") String userId)
    {
        userService.deleteUser(userId);
        ApiResponseMessage message=ApiResponseMessage
                .builder()
                .message("user deleted successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }


    //getAll
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
                @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
                @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
//        List<UserDto> allUserDto=userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir);
//        return new ResponseEntity<>(allUserDto,HttpStatus.FOUND);

        return new ResponseEntity<>(userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir),HttpStatus.FOUND);
    }

    //getSingleBy ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getSingleUserById(@PathVariable("userId") String userId){
        UserDto  userDtoById = userService.getUserById(userId);
        return new ResponseEntity<>(userDtoById,HttpStatus.FOUND);
    }

    //getSingleUser Email
    @GetMapping("email/{email}")
    public ResponseEntity<UserDto> getSingleUserByEmail(@PathVariable("email") String email){
        UserDto userDtoByEmail=userService.getUserByEmail(email);
        return new ResponseEntity<>(userDtoByEmail,HttpStatus.OK);
    }

    //search user
    @GetMapping("search/{Keywords}")
    public ResponseEntity<List<UserDto>> userSearch(@PathVariable("Keywords") String keywords){
        List<UserDto> userList =userService.searchUser(keywords);
        return new ResponseEntity<>(userList,HttpStatus.OK);
    }

    @GetMapping("searchby/{gender}")
    public ResponseEntity<List<UserDto>> userSearchByGender(@PathVariable("gender") String gender){
        List<UserDto> userList =userService.searchUserByGender(gender);
        return new ResponseEntity<>(userList,HttpStatus.OK);
    }




        //file upload and serve
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@PathVariable("userId") String userId, @RequestParam("userImage")MultipartFile image) throws IOException {
//         private String imageUploadPath;
        String imageName=fileService.uploadImage(image,imageUploadPath);

        UserDto user=userService.getUserById(userId);
        user.setImageName(imageName);
        UserDto userDto =userService.updatedUser(user,userId);

        ImageResponse imageResponse=ImageResponse.builder()
                .imageName(imageName)
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
        }
        //serve image
        @GetMapping("/image/{userId}")
        public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {

        UserDto user=userService.getUserById(userId);
        logger.info("user image is:{}",user.getImageName());
        InputStream resources=fileService.getResources(imageUploadPath,user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resources,response.getOutputStream());
        }

}
