package com.smartinsight.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "datasets")
@Data
@NoArgsConstructor
public class Dataset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    
    private String fileType;
    
    @Column(columnDefinition = "TEXT")
    private String rawData;

    private LocalDateTime createdAt = LocalDateTime.now();
}
