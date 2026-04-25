package com.smartinsight.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsightResponse {
    private Long id;
    private Long datasetId;
    private String content;
    private boolean anomalyFlag;
    private String executiveSummary;
}
