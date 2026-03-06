package com.houseofai.backend.repository;

import com.houseofai.backend.model.Visitor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VisitorRepository extends MongoRepository<Visitor, String> {
    Optional<Visitor> findByVisitorId(String visitorId);
}
