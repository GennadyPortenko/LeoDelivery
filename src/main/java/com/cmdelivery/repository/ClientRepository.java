package com.cmdelivery.repository;

import com.cmdelivery.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface ClientRepository extends JpaRepository<Client, Long> {
    void deleteAll();
    Client findByPhone(String phone);
}
