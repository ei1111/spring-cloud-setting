package com.userservice.service;

import com.userservice.dto.UserDto;
import com.userservice.jpa.UserEntity;
import com.userservice.jpa.UserRepository;
import com.userservice.security.PasswordConfig;
import com.userservice.vo.ResponseOrder;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

       private final PasswordEncoder passwordEncoder;
        private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        String encodePw = passwordEncoder.encode(userDto.getPwd());
        UserEntity userEntity = userDto.toEntity(encodePw);
        return  userRepository.save(userEntity).toUserDto();
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null)
            throw new UsernameNotFoundException("User not found");

        UserDto userDto = userEntity.toUserDto();
        userDto.setOrders(new ArrayList<ResponseOrder>());

        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        return userEntity.toUserDto();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username + ": not found");
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());
    }
}