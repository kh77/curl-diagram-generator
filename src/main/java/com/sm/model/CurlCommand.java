package com.sm.model;

import java.util.ArrayList;
import java.util.List;

public class CurlCommand {
    private String method = "GET";
    private String url;
    private String host;
    private String path;
    private List<String> headers = new ArrayList<>();
    private String data;
    private String description;
    
    // Constructors
    public CurlCommand() {}
    
    public CurlCommand(String method, String url, String data) {
        this.method = method;
        this.url = url;
        this.data = data;
        parseUrl();
    }
    
    private void parseUrl() {
        if (url != null) {
            try {
                java.net.URL parsedUrl = new java.net.URL(url);
                this.host = parsedUrl.getHost();
                this.path = parsedUrl.getPath();
                if (path.isEmpty()) path = "/";
            } catch (Exception e) {
                this.host = "unknown";
                this.path = "/";
            }
        }
    }
    
    // Getters and Setters
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { 
        this.url = url; 
        parseUrl();
    }
    
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    
    public List<String> getHeaders() { return headers; }
    public void setHeaders(List<String> headers) { this.headers = headers; }
    public void addHeader(String header) { this.headers.add(header); }
    
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}