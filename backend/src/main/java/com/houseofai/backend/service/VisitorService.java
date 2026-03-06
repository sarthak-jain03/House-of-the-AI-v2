package com.houseofai.backend.service;

import com.houseofai.backend.model.Visitor;
import com.houseofai.backend.model.VisitorCount;
import com.houseofai.backend.repository.VisitorRepository;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final MongoTemplate mongoTemplate;

    public VisitorService(VisitorRepository visitorRepository, MongoTemplate mongoTemplate) {
        this.visitorRepository = visitorRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Returns the current visitor count.
     */
    public long getCurrentCount() {
        VisitorCount vc = mongoTemplate.findOne(new Query(), VisitorCount.class);
        return vc != null ? vc.getCount() : 0;
    }

    /**
     * Tracks a unique visitor and returns the updated count.
     * If the visitor already exists, just returns the current count.
     */
    public long trackVisitor(String visitorId) {
        // No visitorId → just return count
        if (visitorId == null || visitorId.isBlank()) {
            return getCurrentCount();
        }

        // Already tracked → just return count
        if (visitorRepository.findByVisitorId(visitorId).isPresent()) {
            return getCurrentCount();
        }

        // Try to create new visitor (handle duplicate key race condition)
        try {
            Visitor visitor = new Visitor();
            visitor.setVisitorId(visitorId);
            visitorRepository.save(visitor);
        } catch (DuplicateKeyException e) {
            return getCurrentCount();
        }

        // Atomic increment
        VisitorCount counter = mongoTemplate.findAndModify(
                new Query(),
                new Update().inc("count", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                VisitorCount.class
        );

        return counter != null ? counter.getCount() : 1;
    }
}
