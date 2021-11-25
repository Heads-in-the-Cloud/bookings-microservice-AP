package com.ss.utopia.restapi.services;

import com.ss.utopia.restapi.dao.UserRepository;
import com.ss.utopia.restapi.models.User;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public class GetUserFromAuthHeader {

    public static User getUserFromAuthHeader(UserRepository userDB) throws ResponseStatusException {
        return userDB
            .findByUsername(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()
                .toString()
            ).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED)
        );
    }
}
