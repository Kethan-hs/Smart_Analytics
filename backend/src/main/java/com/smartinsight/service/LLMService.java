package com.smartinsight.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class LLMService {

    private final RestClient restClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public LLMService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public String generateInsights(String datasetSummary) {
        String prompt = """
            You are a data analyst. Analyze the following dataset summary and return output strictly in the following JSON format:
            {
              "insights": "3 key insights in a single string separated by newlines",
              "anomalyFlag": true or false,
              "executiveSummary": "A one-line executive summary"
            }
            
            Dataset:
            %s
            """.formatted(datasetSummary);

        var request = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", prompt)
                ))
            )
        );

        String fullUrl = apiUrl + apiKey;

        try {
            var response = restClient.post()
                .uri(fullUrl)
                .body(request)
                .retrieve()
                .body(Map.class);

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                return (String) parts.get(0).get("text");
            }
            return "{}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"insights\": \"Failed to analyze data\", \"anomalyFlag\": false, \"executiveSummary\": \"Error\"}";
        }
    }
}
