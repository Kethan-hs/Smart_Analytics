package com.smartinsight.controller;

import com.smartinsight.dto.AnalysisRequest;
import com.smartinsight.dto.InsightResponse;
import com.smartinsight.dto.UploadResponse;
import com.smartinsight.model.Dataset;
import com.smartinsight.model.Insight;
import com.smartinsight.repository.DatasetRepository;
import com.smartinsight.service.DataParserService;
import com.smartinsight.service.InsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // For local dev
@RequiredArgsConstructor
public class UploadController {

    private final DataParserService dataParserService;
    private final DatasetRepository datasetRepository;
    private final InsightService insightService;

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String rawData = dataParserService.parseData(file);

            Dataset dataset = new Dataset();
            dataset.setFileName(file.getOriginalFilename());
            dataset.setFileType(file.getContentType());
            dataset.setRawData(rawData);
            
            dataset = datasetRepository.save(dataset);

            return ResponseEntity.ok(new UploadResponse(dataset.getId(), "File uploaded successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UploadResponse(null, e.getMessage()));
        }
    }

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeDataset(@RequestBody AnalysisRequest request) {
        try {
            Insight insight = insightService.generateAndSaveInsights(request.getDatasetId());
            return ResponseEntity.ok(new InsightResponse(
                insight.getId(), 
                insight.getDataset().getId(),
                insight.getContent(),
                insight.isAnomalyFlag(),
                insight.getExecutiveSummary()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
