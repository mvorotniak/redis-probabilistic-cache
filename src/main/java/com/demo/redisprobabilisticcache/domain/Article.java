package com.demo.redisprobabilisticcache.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "articles")
@Entity
public class Article {
    
    @Id
    private Long id;
    
    private String name;
}
