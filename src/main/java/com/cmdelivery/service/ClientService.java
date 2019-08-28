package com.cmdelivery.service;

import com.cmdelivery.model.Client;
import com.cmdelivery.model.Role;
import com.cmdelivery.repository.ClientRepository;
import com.cmdelivery.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ClientService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;

    @Transactional
    public Client registerNewClient(Client client) {
        client.setActive(1);
        Role clientRole = roleRepository.findByRole("ROLE_USER");
        client.setRoles((new HashSet<>(Arrays.asList(clientRole))));
        return clientRepository.save(client);
    }
}
