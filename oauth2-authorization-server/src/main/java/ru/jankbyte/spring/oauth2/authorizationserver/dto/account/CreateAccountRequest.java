package ru.jankbyte.spring.oauth2.authorizationserver.dto.account;

import static ru.jankbyte.spring.oauth2.authorizationserver.validation.annotation.group.ValidationOrders.*;
import ru.jankbyte.spring.oauth2.authorizationserver.validation.annotation.IsUsernameExists;
import ru.jankbyte.spring.oauth2.authorizationserver.validation.annotation.Username;
import ru.jankbyte.spring.oauth2.authorizationserver.validation.annotation.Password;


import jakarta.validation.GroupSequence;

@GroupSequence({ 
  Level1.class, Level2.class,
  CreateAccountRequest.class
})
public record CreateAccountRequest(
    @Username(groups = Level1.class)
    @IsUsernameExists(groups = Level2.class)
        String name,
    @Password(groups = Level1.class) String password) {
}
