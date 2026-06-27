package com.dmclothing.repository;

import com.dmclothing.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByUsername(String username);
    Optional<Owner> findByUsernameAndPassword(String username, String password);
}