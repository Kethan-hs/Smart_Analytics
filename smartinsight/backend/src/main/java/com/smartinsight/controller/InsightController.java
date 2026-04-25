package com.smartinsight.controller;

import com.smartinsight.dto.InsightResponse;
import com.smartinsight.model.Insight;
import com.smartinsight.service.InsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/insights")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class InsightController {

    private final InsightService insightService;

    @GetMapping("/{datasetId}")
    public ResponseEntity<List<InsightResponse>> getInsights(@PathVariable Long datasetId) {
        List<Insight> insights = insightService.getInsightsForDataset(datasetId);
        List<InsightResponse> responses = insights.stream().map(insight -> new InsightResponse(
            insight.getId(),
            insight.getDataset().getId(),
            insight.getContent(),
            insight.isAnomalyFlag(),
            insight.getExecutiveSummary()
        )).collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }
}
