package com.smartinsight.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "insights")
@Data
@NoArgsConstructor
public class Insight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataset_id")
    private Dataset dataset;

    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean anomalyFlag;

    @Column(columnDefinition = "TEXT")
    private String executiveSummary;

    private LocalDateTime createdAt = LocalDateTime.now();
}
