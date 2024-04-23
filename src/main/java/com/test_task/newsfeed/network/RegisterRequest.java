package com.test_task.newsfeed.network;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest implements AuthenticationRequest {
        @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{4,19}$",
                message = "A valid login should start with an alphabet so, [A-Za-z].")
        @Size(min = 5, message = "Login must be at least 5 characters")
        @Size(max = 20, message = "Login must be less than 20 characters")
        String login;

        @Size(min = 8, message = "Password must be at least 8 characters")
        @Size(max = 20, message = "Password must be less than 20 characters")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
                message = "Password must include both upper and lowercase letters, plus at least one number (0-9)")
        String password;

        //Validation in authenticationService
        String confirmationPassword;

        //Optional
        @Pattern(regexp = "(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)",
                message = "Please provide a valid URL.")
        String avatarLink;

        @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{3,19}$",
                message = "A valid name should start with an alphabet so, [A-Za-z].")
        @Size(min = 4, message = "Name must be at least 4 characters")
        @Size(max = 20, message = "Name must be less than 20 characters")
        String name;

        @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{3,19}$",
                message = "A valid surname should start with an alphabet so, [A-Za-z].")
        @Size(min = 4, message = "Surname must be at least 4 characters")
        @Size(max = 20, message = "Surname must be less than 20 characters")
        String surname;

        //Optional
        @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{19}$",
                message = "A valid middle name should start with an alphabet so, [A-Za-z].")
        @Size(max = 20, message = "Middle Name must be less than 20 characters")
        String middleName;
}
