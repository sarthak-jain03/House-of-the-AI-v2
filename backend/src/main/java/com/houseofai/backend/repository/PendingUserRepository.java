package com.houseofai.backend.repository;

import com.houseofai.backend.model.PendingUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PendingUserRepository extends MongoRepository<PendingUser, String> {
    Optional<PendingUser> findByEmail(String email);
    void deleteByEmail(String email);
}
