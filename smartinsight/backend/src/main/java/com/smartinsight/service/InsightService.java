package com.smartinsight.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartinsight.model.Dataset;
import com.smartinsight.model.Insight;
import com.smartinsight.repository.DatasetRepository;
import com.smartinsight.repository.InsightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsightService {

    private final DatasetRepository datasetRepository;
    private final InsightRepository insightRepository;
    private final LLMService llmService;
    private final ObjectMapper objectMapper;

    public Insight generateAndSaveInsights(Long datasetId) throws Exception {
        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new IllegalArgumentException("Dataset not found"));

        String llmResponse = llmService.generateInsights(dataset.getRawData());
        
        // Remove markdown JSON code blocks if present
        llmResponse = llmResponse.replaceAll("```json", "").replaceAll("```", "").trim();

        JsonNode jsonNode = objectMapper.readTree(llmResponse);

        Insight insight = new Insight();
        insight.setDataset(dataset);
        insight.setContent(jsonNode.has("insights") ? jsonNode.get("insights").asText() : "");
        insight.setAnomalyFlag(jsonNode.has("anomalyFlag") && jsonNode.get("anomalyFlag").asBoolean());
        insight.setExecutiveSummary(jsonNode.has("executiveSummary") ? jsonNode.get("executiveSummary").asText() : "");

        return insightRepository.save(insight);
    }
    
    public List<Insight> getInsightsForDataset(Long datasetId) {
        return insightRepository.findByDatasetId(datasetId);
    }
}
