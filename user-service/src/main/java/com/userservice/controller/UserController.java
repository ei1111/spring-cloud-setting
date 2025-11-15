package com.userservice.controller;

import com.userservice.dto.UserDto;
import com.userservice.jpa.UserEntity;
import com.userservice.service.UserService;
import com.userservice.vo.Greeting;
import com.userservice.vo.RequestUser;
import com.userservice.vo.ResponseUser;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {
    private final Environment env;
    private final Greeting greeting;
    private final UserService userService;

    @GetMapping("/health-check") // http://localhost:60000/health-check
    public String status() {
        return String.format("It's Working in User Service"
                + ", port(local.server.port)=" + env.getProperty("local.server.port")
                + ", port(server.port)=" + env.getProperty("server.port"));
    }

    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request) {
        log.info("users.welcome ip: {}, {}, {}, {}", request.getRemoteAddr()
                , request.getRemoteHost(), request.getRequestURI(), request.getRequestURL());

        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        UserDto userDto = userService.createUser(user.toUserDto());
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto.toResponse());
    }


    @GetMapping("/users")
    public ResponseEntity getUsers() {
        Iterable<UserEntity> userList = userService.getUserByAll();

        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(v -> result.add(ResponseUser.from(v))
        );

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity getUser(@PathVariable("userId") String userId) {
        UserDto userDto = userService.getUserByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userDto.toResponse());
    }
}
