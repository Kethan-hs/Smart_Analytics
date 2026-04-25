package com.smartinsight.repository;

import com.smartinsight.model.Insight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InsightRepository extends JpaRepository<Insight, Long> {
    List<Insight> findByDatasetId(Long datasetId);
}
