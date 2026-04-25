package com.smartinsight.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;

@Service
public class DataParserService {

    public String parseData(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.endsWith(".csv") && !filename.endsWith(".json"))) {
            throw new IllegalArgumentException("Only CSV or JSON files are supported");
        }

        // Return a truncated version if file is too large to fit in prompt
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        if (content.length() > 5000) {
            return content.substring(0, 5000) + "\n...[TRUNCATED]";
        }
        return content;
    }
}
