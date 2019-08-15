package com.cmdelivery.service;

import com.cmdelivery.model.Person;
import com.cmdelivery.model.Role;
import com.cmdelivery.repository.PersonRepository;
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
public class PersonService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;

    @Transactional
    public Person registerNewPerson(Person person) {
        person.setPassword(bCryptPasswordEncoder.encode(person.getPassword()));
        person.setActive(1);
        Role personRole = roleRepository.findByRole("ROLE_USER");
        person.setRoles((new HashSet<>(Arrays.asList(personRole))));
        return personRepository.save(person);
    }
}
