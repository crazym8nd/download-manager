package com.vitaly.dlmanager.security;
//  17-Feb-24
// gh crazym8nd

import com.vitaly.dlmanager.entity.UserStatus;
import com.vitaly.dlmanager.exception.UnauthorizedException;
import com.vitaly.dlmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserService userService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

        return userService.getUserById(principal.getId())
                .filter(userEntity -> userEntity.getStatus().equals(UserStatus.ACTIVE))
                .switchIfEmpty(Mono.error(new UnauthorizedException("User deleted")))
                .map(user -> authentication);
    }
}
