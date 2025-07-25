package com.sm.controller.request;

import org.springframework.web.multipart.MultipartFile;

public class HarUploadRequest {
    private MultipartFile harFile;
    private String diagramType; // sequence|flowchart|network
    private String title;

    // Constructors
    public HarUploadRequest() {}

    public HarUploadRequest(MultipartFile harFile, String diagramType, String title) {
        this.harFile = harFile;
        this.diagramType = diagramType;
        this.title = title;
    }

    // Getters and Setters
    public MultipartFile getHarFile() {
        return harFile;
    }

    public void setHarFile(MultipartFile harFile) {
        this.harFile = harFile;
    }

    public String getDiagramType() {
        return diagramType;
    }

    public void setDiagramType(String diagramType) {
        this.diagramType = diagramType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Validation method
    public boolean isValid() {
        return harFile != null && 
               !harFile.isEmpty() &&
               diagramType != null && 
               !diagramType.trim().isEmpty() &&
               (diagramType.equals("sequence") || 
                diagramType.equals("flowchart") || 
                diagramType.equals("network"));
    }
}