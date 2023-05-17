package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.validate.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.bind.annotation.PathVariable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDto {

    private String userId;

    @Size(min =3,max = 15,message = "invalid name cannot be null")
    private String name;

    @Pattern(regexp ="^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$" ,message = "invalid user email")
    @Email(message = "user email invalid")
    private String email;

    @NotBlank(message = "password required")
    private String password;

    @Size(min = 4,max = 6,message = "invalid gender")
    private String gender;

    @NotBlank(message = "tell me about yourself")
    private String about;

    @ImageNameValid
    private String imageName;



//    pattern
//    custom

//    custom logic implementation
//    we create annotation for custom logic writting


}
