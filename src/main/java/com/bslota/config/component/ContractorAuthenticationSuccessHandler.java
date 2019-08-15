package com.bslota.config.component;

import com.bslota.repository.ContractorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.Set;

@Component
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ContractorAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final HttpSession session;
    private final ContractorRepository repository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // TODO Auto-generated method stub
        String userName = "";
        if(authentication.getPrincipal() instanceof Principal) {
            userName = ((Principal)authentication.getPrincipal()).getName();

        }else {
            userName = ((User)authentication.getPrincipal()).getUsername();
        }
        // HttpSession session = request.getSession();
        session.setAttribute("username", userName);

        // Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        response.sendRedirect("/contractor/cabinet");

    }

}
