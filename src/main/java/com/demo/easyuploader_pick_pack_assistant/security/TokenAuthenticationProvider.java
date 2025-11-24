package com.demo.easyuploader_pick_pack_assistant.security;

import com.demo.easyuploader_pick_pack_assistant.dto.AuthenticatedUser;
import com.demo.easyuploader_pick_pack_assistant.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@RequiredArgsConstructor
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getName();

        return userRepository.findByToken(token)
                .map(user -> {
                    AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                            user.getId(),
                            user.getLogin(),
                            user.getUserRole()
                    );

                    return new UsernamePasswordAuthenticationToken(
                            authenticatedUser,
                            token,
                            List.of(new SimpleGrantedAuthority(user.getUserRole()))
                    );
                })
                .orElseThrow(() -> new BadCredentialsException("Invalid token"));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}