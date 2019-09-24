package com.cmdelivery.service;

import com.cmdelivery.model.Category;
import com.cmdelivery.model.Client;
import com.cmdelivery.model.Role;
import com.cmdelivery.repository.CategoryRepository;
import com.cmdelivery.repository.ClientRepository;
import com.cmdelivery.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> findByNameIn(List<String> names) {
        String localeName = LocaleContextHolder.getLocale().toString();
        if (localeName.equals("fr_FR")) {
            return categoryRepository.findByNameFrIn(names);
        } else {
            return categoryRepository.findByNameEnIn(names);
        }
    }
}
