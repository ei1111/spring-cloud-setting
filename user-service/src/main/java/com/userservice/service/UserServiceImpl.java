package com.userservice.service;

import com.userservice.dto.UserDto;
import com.userservice.jpa.UserEntity;
import com.userservice.jpa.UserRepository;
import com.userservice.security.WebSecurity;
import com.userservice.vo.ResponseOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
}