package com.bslota.service;

import com.bslota.model.Contractor;
import com.bslota.model.Role;
import com.bslota.repository.ContractorRepository;
import com.bslota.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ContractorService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final ContractorRepository contractorRepository;

    @Transactional
    public Contractor registerNewContractor(Contractor contractor) {
        contractor.setPassword(bCryptPasswordEncoder.encode(contractor.getPassword()));
        contractor.setActive(1);
        Role contractorRole = roleRepository.findByRole("ROLE_CONTRACTOR");
        contractor.setRoles(new HashSet<>(Arrays.asList(contractorRole)));
        return contractorRepository.save(contractor);
    }
}
