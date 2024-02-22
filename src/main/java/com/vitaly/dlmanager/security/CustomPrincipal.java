package com.vitaly.dlmanager.security;
//  17-Feb-24
// gh crazym8nd


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.security.auth.Subject;
import java.security.Principal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomPrincipal implements Principal {

    private Long id;
    private String username;

    @Override
    public String getName() {
        return username;
    }
}
