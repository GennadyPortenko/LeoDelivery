package com.cmdelivery.config.component;

import com.cmdelivery.model.Person;
import com.cmdelivery.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class PersonAuthenticationProvider implements AuthenticationProvider {

    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String phone = authentication.getName();
        String password = authentication.getCredentials().toString();

        if ( true /* shouldAuthenticateAgainstThirdPartySystem() */ ) {
            Person person = personRepository.findByPhone(phone);
            if (person == null)
                return null;
            String password_db = person.getPassword();
            if (bCryptPasswordEncoder.matches(password, password_db)) {
                return new UsernamePasswordAuthenticationToken(phone, password, new ArrayList<>());
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
