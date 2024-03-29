package com.vitaly.dlmanager.security;



import com.vitaly.dlmanager.entity.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomPrincipal implements Principal {

    private Long id;
    private String username;
    private UserRole userRole;


    @Override
    public String getName() {
        return username;
    }

}
