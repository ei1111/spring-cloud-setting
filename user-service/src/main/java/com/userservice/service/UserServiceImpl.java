package com.userservice.service;

import com.userservice.client.OrderServiceClient;
import com.userservice.dto.UserDto;
import com.userservice.jpa.UserEntity;
import com.userservice.jpa.UserRepository;
import com.userservice.vo.ResponseOrder;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final OrderServiceClient orderServiceClient;

    @Override
    public UserDto createUser(UserDto userDto) {
        String encodePw = passwordEncoder.encode(userDto.getPwd());
        UserEntity userEntity = userDto.toEntity(encodePw);
        return  userRepository.save(userEntity).toUserDto();
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        UserDto userDto = userEntity.toUserDto();

        // @LoadBalanced 사용전
        //String orderServiceUrl = "http://127.0.0.1:8000/order-service/" + userId + "/orders";
        //  @LoadBalanced 사용후
      //  String orderServiceUrl = "http://order-service/order-service/" + userId + "/orders";

        /* restTemplate 방식
       ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(
                orderServiceUrl,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ResponseOrder>>() {
                });

        List<ResponseOrder> orderList = orderListResponse.getBody();
        userDto.setOrders(orderList);*/

        List<ResponseOrder> orderList = orderServiceClient.getOrders(userDto.getUserId());
        userDto.setOrders(orderList);

        log.info("orderList={}", orderList);
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