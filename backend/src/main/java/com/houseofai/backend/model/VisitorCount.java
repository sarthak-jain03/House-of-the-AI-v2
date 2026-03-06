package com.houseofai.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "visitorcounts")
public class VisitorCount {

    @Id
    private String id;
    private long count;

    public VisitorCount() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public long getCount() { return count; }
    public void setCount(long count) { this.count = count; }
}
