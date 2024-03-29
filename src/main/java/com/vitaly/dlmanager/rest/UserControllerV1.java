package com.vitaly.dlmanager.rest;


import com.vitaly.dlmanager.dto.UserDto;
import com.vitaly.dlmanager.entity.user.UserRole;
import com.vitaly.dlmanager.mapper.UserMapper;
import com.vitaly.dlmanager.security.CustomPrincipal;
import com.vitaly.dlmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserControllerV1 {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> getUserById(@PathVariable Long id, Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        UserRole role = customPrincipal.getUserRole();

        if (UserRole.MODERATOR.equals(role) || UserRole.ADMIN.equals(role)) {
            return userService.getById(id)
                    .map(user -> {
                        UserDto userDto = userMapper.map(user);
                        return ResponseEntity.ok(userDto);
                    })
                    .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        } else {
            return Mono.just(new ResponseEntity<>(HttpStatus.FORBIDDEN));
        }
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<UserDto>>> getAllUsers(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        UserRole role = customPrincipal.getUserRole();

        Flux<UserDto> userDtoFlux;
        if (UserRole.MODERATOR.equals(role) || UserRole.ADMIN.equals(role)) {
            userDtoFlux = userService.getAll()
                    .map(userMapper::map);
        } else {
            return Mono.just(new ResponseEntity<>(HttpStatus.FORBIDDEN));
        }
        return Mono.just(ResponseEntity.ok(userDtoFlux));
    }

    @PostMapping
    public Mono<ResponseEntity<UserDto>> save(@RequestBody UserDto userDto, Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        UserRole role = customPrincipal.getUserRole();

        if (UserRole.MODERATOR.equals(role) || UserRole.ADMIN.equals(role)) {
            return userService.save(userMapper.map(userDto))
                    .map(savedUser -> new ResponseEntity<>(userMapper.map(savedUser), HttpStatus.CREATED))
                    .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        } else {
            return Mono.just(new ResponseEntity<>(HttpStatus.FORBIDDEN));
        }
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> update(@PathVariable Long id, @RequestBody UserDto userDto, Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        UserRole role = customPrincipal.getUserRole();

        if (UserRole.MODERATOR.equals(role) || UserRole.ADMIN.equals(role)) {
            return userService.update(userMapper.map(userDto))
                    .map(updatedUser -> new ResponseEntity<>(userMapper.map(updatedUser), HttpStatus.OK))
                    .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        } else {
            return Mono.just(new ResponseEntity<>(HttpStatus.FORBIDDEN));
        }
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable Long id, Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        UserRole role = customPrincipal.getUserRole();

        if (UserRole.MODERATOR.equals(role) || UserRole.ADMIN.equals(role)) {
            return userService.getById(id)
                    .flatMap(event -> userService.delete(event.getId()))
                    .then(Mono.just(new ResponseEntity<>(HttpStatus.OK)))
                    .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else {
            return Mono.just(new ResponseEntity<>(HttpStatus.FORBIDDEN));
        }
    }
}
