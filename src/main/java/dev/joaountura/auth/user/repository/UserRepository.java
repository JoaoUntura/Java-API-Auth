package dev.joaountura.auth.user.repository;

import dev.joaountura.auth.user.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Users findByEmail(String email);

    Users findByExternalId(UUID externalId);

}
