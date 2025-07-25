package com.sm.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sm.model.HarEntry;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class HarParserService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<HarEntry> parseHarFile(MultipartFile harFile) throws IOException {
        List<HarEntry> entries = new ArrayList<>();
        
        try {
            // Parse JSON from uploaded file
            JsonNode root = objectMapper.readTree(harFile.getInputStream());
            JsonNode harNode = root.get("log");
            
            if (harNode == null) {
                throw new IllegalArgumentException("Invalid HAR file format - missing 'log' node");
            }
            
            JsonNode entriesNode = harNode.get("entries");
            
            if (entriesNode == null || !entriesNode.isArray()) {
                throw new IllegalArgumentException("Invalid HAR file format - missing or invalid 'entries' node");
            }
            
            // Process each entry
            for (JsonNode entryNode : entriesNode) {
                HarEntry harEntry = parseHarEntry(entryNode);
                if (harEntry != null) {
                    entries.add(harEntry);
                }
            }
            
        } catch (Exception e) {
            throw new IOException("Failed to parse HAR file: " + e.getMessage(), e);
        }
        
        return entries;
    }

    private HarEntry parseHarEntry(JsonNode entryNode) {
        try {
            JsonNode requestNode = entryNode.get("request");
            JsonNode responseNode = entryNode.get("response");
            
            if (requestNode == null || responseNode == null) {
                return null; // Skip invalid entries
            }
            
            HarEntry entry = new HarEntry();
            
            // Parse request data
            entry.setMethod(requestNode.get("method").asText());
            entry.setUrl(requestNode.get("url").asText());
            entry.setHeaders(parseHeaders(requestNode.get("headers")));
            entry.setRequestBody(parseRequestBody(requestNode));
            
            // Parse response data
            entry.setResponseStatus(responseNode.get("status").asInt());
            entry.setResponseBody(parseResponseBody(responseNode));
            
            // Parse timing
            JsonNode timingsNode = entryNode.get("timings");
            if (timingsNode != null) {
                entry.setTime(timingsNode.get("wait").asLong(0));
            }
            
            // Parse server IP
            JsonNode serverIPNode = entryNode.get("serverIPAddress");
            if (serverIPNode != null) {
                entry.setServerIPAddress(serverIPNode.asText());
            }
            
            return entry;
            
        } catch (Exception e) {
            // Log error and skip this entry
            System.err.println("Error parsing HAR entry: " + e.getMessage());
            return null;
        }
    }
    
    private Map<String, String> parseHeaders(JsonNode headersNode) {
        Map<String, String> headers = new HashMap<>();
        
        if (headersNode != null && headersNode.isArray()) {
            for (JsonNode headerNode : headersNode) {
                String name = headerNode.get("name").asText();
                String value = headerNode.get("value").asText();
                headers.put(name, value);
            }
        }
        
        return headers;
    }
    
    private String parseRequestBody(JsonNode requestNode) {
        JsonNode postDataNode = requestNode.get("postData");
        if (postDataNode != null) {
            JsonNode textNode = postDataNode.get("text");
            if (textNode != null) {
                return textNode.asText();
            }
        }
        return null;
    }
    
    private String parseResponseBody(JsonNode responseNode) {
        JsonNode contentNode = responseNode.get("content");
        if (contentNode != null) {
            JsonNode textNode = contentNode.get("text");
            if (textNode != null) {
                return textNode.asText();
            }
        }
        return null;
    }

    /**
     * Convert HAR entries to curl-like commands for diagram generation
     */
    public List<String> convertToCurlCommands(List<HarEntry> entries) {
        List<String> curlCommands = new ArrayList<>();
        
        for (HarEntry entry : entries) {
            StringBuilder curl = new StringBuilder();
            curl.append("curl -X ").append(entry.getMethod());
            curl.append(" '").append(entry.getUrl()).append("'");
            
            // Add headers
            if (entry.getHeaders() != null) {
                for (Map.Entry<String, String> header : entry.getHeaders().entrySet()) {
                    // Skip some common headers that might cause issues
                    if (isImportantHeader(header.getKey())) {
                        curl.append(" -H '").append(header.getKey())
                            .append(": ").append(header.getValue()).append("'");
                    }
                }
            }
            
            // Add request body if present
            if (entry.getRequestBody() != null && !entry.getRequestBody().trim().isEmpty()) {
                curl.append(" -d '").append(entry.getRequestBody().replace("'", "\\'")).append("'");
            }
            
            curlCommands.add(curl.toString());
        }
        
        return curlCommands;
    }
    
    private boolean isImportantHeader(String headerName) {
        String lowerName = headerName.toLowerCase();
        return lowerName.equals("content-type") || 
               lowerName.equals("authorization") ||
               lowerName.equals("accept") ||
               lowerName.equals("user-agent") ||
               lowerName.startsWith("x-");
    }
}