package com.houseofai.backend.repository;

import com.houseofai.backend.model.VisitorCount;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VisitorCountRepository extends MongoRepository<VisitorCount, String> {
}
