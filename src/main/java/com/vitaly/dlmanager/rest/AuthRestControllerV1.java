package com.vitaly.dlmanager.rest;


import com.vitaly.dlmanager.dto.AuthRequestDto;
import com.vitaly.dlmanager.dto.AuthResponseDto;
import com.vitaly.dlmanager.dto.UserDto;
import com.vitaly.dlmanager.entity.user.UserEntity;
import com.vitaly.dlmanager.mapper.UserMapper;
import com.vitaly.dlmanager.security.CustomPrincipal;
import com.vitaly.dlmanager.security.SecurityService;
import com.vitaly.dlmanager.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthRestControllerV1 {

    private final SecurityService securityService;
    private final UserServiceImpl userServiceImpl;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody UserDto dto) {
        UserEntity entity = userMapper.map(dto);
        return userServiceImpl.save(entity)
                .map(userMapper::map);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto dto) {
        return securityService.authenticate(dto.getUsername(), dto.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .build()
                ));
    }

    @GetMapping("/info")
    public Mono<UserDto> getUserInfo(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();

        return userServiceImpl.getById(customPrincipal.getId())
                .map(userMapper::map);
    }
}
