package com.sm.controller.response;

import java.util.Map;

public class DiagramResponse {
    private String diagramType;
    private String plantUmlSource;
    private String base64Image;
    private String mermaidSource;
    private Map<String, Object> metadata;
    
    // Constructors
    public DiagramResponse() {}
    
    public DiagramResponse(String diagramType, String plantUmlSource, String base64Image) {
        this.diagramType = diagramType;
        this.plantUmlSource = plantUmlSource;
        this.base64Image = base64Image;
    }
    
    // Getters and Setters
    public String getDiagramType() { return diagramType; }
    public void setDiagramType(String diagramType) { this.diagramType = diagramType; }
    
    public String getPlantUmlSource() { return plantUmlSource; }
    public void setPlantUmlSource(String plantUmlSource) { this.plantUmlSource = plantUmlSource; }
    
    public String getBase64Image() { return base64Image; }
    public void setBase64Image(String base64Image) { this.base64Image = base64Image; }
    
    public String getMermaidSource() { return mermaidSource; }
    public void setMermaidSource(String mermaidSource) { this.mermaidSource = mermaidSource; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
