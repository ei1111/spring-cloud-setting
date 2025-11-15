package com.userservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.userservice.jpa.UserEntity;
import java.util.List;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser {
    private String email;

    private String name;

    private String userId;

    private List<ResponseOrder> orders;

    public static ResponseUser from(UserEntity userEntity) {
        return ResponseUser.builder()
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .userId(userEntity.getUserId())
                .orders(userEntity.toUserDto().getOrders())
                .build();
    }
}
