package com.vitaly.dlmanager.security;
//  17-Feb-24
// gh crazym8nd

import com.vitaly.dlmanager.entity.Status;
import com.vitaly.dlmanager.exception.UnauthorizedException;
import com.vitaly.dlmanager.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserServiceImpl userServiceImpl;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

        return userServiceImpl.getById(principal.getId())
                .filter(userEntity -> userEntity.getStatus().equals(Status.ACTIVE))
                .switchIfEmpty(Mono.error(new UnauthorizedException("User deleted")))
                .map(user -> authentication);
    }
}
