package com.vitaly.dlmanager.rest;
//  01-Mar-24
// gh crazym8nd

import com.vitaly.dlmanager.dto.UserDto;
import com.vitaly.dlmanager.entity.user.UserEntity;
import com.vitaly.dlmanager.mapper.UserMapper;
import com.vitaly.dlmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/users/")
public class UserControllerV1 {

    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    public UserControllerV1(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> getUserById(@PathVariable Long id){
        return userService.getById(id)
                .map(user -> {
                    UserDto userDto = userMapper.map(user);
                    return ResponseEntity.ok(userDto);
                })
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping("/name")
    public Mono<ResponseEntity<UserDto>> getUserByName(@PathVariable String name){
        return userService.getUserByUsername(name)
                .map(user -> {
                    UserDto userDto = userMapper.map(user);
                    return ResponseEntity.ok(userDto);
                })
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<UserDto>>> getAllUsers() {
        Flux<UserDto> userDtoFlux = userService.getAll()
                .map(userMapper::map);
        return Mono.just(ResponseEntity.ok(userDtoFlux));
    }

    @PostMapping
    public Mono<ResponseEntity<UserEntity>> save(@RequestBody UserEntity userEntity) {
        if (userEntity == null) {
            return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        return userService.save(userEntity)
                .map(savedUser -> new ResponseEntity<>(savedUser, HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserEntity>> update(@PathVariable Long id, @RequestBody UserEntity userEntity) {
        if (userEntity == null) {
            return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        return userService.update(userEntity)
                .map(updatedUser -> new ResponseEntity<>(updatedUser , HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return userService.getById(id)
                .flatMap(user -> userService.delete(user.getId())
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }
}
