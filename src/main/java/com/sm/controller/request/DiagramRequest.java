package com.sm.controller.request;

public class DiagramRequest {
    private String curlCommands;
    private String diagramType; // sequence, flowchart, network, all
    private String title;
    
    // Constructors
    public DiagramRequest() {}
    
    public DiagramRequest(String curlCommands, String diagramType) {
        this.curlCommands = curlCommands;
        this.diagramType = diagramType;
    }
    
    // Getters and Setters
    public String getCurlCommands() { return curlCommands; }
    public void setCurlCommands(String curlCommands) { this.curlCommands = curlCommands; }
    
    public String getDiagramType() { return diagramType; }
    public void setDiagramType(String diagramType) { this.diagramType = diagramType; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
