package com.cmdelivery.service;

import com.cmdelivery.model.Contractor;
import com.cmdelivery.model.Role;
import com.cmdelivery.model.Section;
import com.cmdelivery.repository.ContractorRepository;
import com.cmdelivery.repository.RoleRepository;
import com.cmdelivery.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private final SectionService sectionService;
    private final SectionRepository sectionRepository;


    @Transactional
    public Contractor registerNewContractor(Contractor contractor) {
        contractor.setPassword(bCryptPasswordEncoder.encode(contractor.getPassword()));
        contractor.setActive(1);
        Role contractorRole = roleRepository.findByRole("ROLE_CONTRACTOR");
        contractor.setRoles(new HashSet<>(Arrays.asList(contractorRole)));
        Contractor savedContractor = contractorRepository.save(contractor);

        Section defaultSection = new Section();
        defaultSection.setContractor(contractor);
        if (sectionService.registerNewDefaultSection(defaultSection) == null) {
            return null;
        }

        return savedContractor;
    }

    public Section getDefaultSection(long contractorId) {
        return sectionRepository.findByNameAndContractor(SectionService.defaultSectionName(), contractorId);
    }

}
