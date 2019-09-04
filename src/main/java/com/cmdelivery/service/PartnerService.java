package com.cmdelivery.service;

import com.cmdelivery.model.Partner;
import com.cmdelivery.model.Role;
import com.cmdelivery.model.Section;
import com.cmdelivery.repository.PartnerRepository;
import com.cmdelivery.repository.RoleRepository;
import com.cmdelivery.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class PartnerService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final PartnerRepository partnerRepository;
    private final SectionService sectionService;
    private final SectionRepository sectionRepository;


    @Transactional
    public Partner registerNewPartner(Partner partner) {
        partner.setPassword(bCryptPasswordEncoder.encode(partner.getPassword()));
        partner.setActive(1);
        Role partnerRole = roleRepository.findByRole("ROLE_PARTNER");
        partner.setRoles(new HashSet<>(Arrays.asList(partnerRole)));
        Partner savedPartner = partnerRepository.save(partner);

        Section defaultSection = new Section();
        defaultSection.setPartner(partner);
        if (sectionService.registerNewDefaultSection(defaultSection) == null) {
            return null;
        }

        return savedPartner;
    }

    public Section getDefaultSection(long partnerId) {
        return sectionRepository.findByNameAndPartner(SectionService.defaultSectionName(), partnerId);
    }

}
