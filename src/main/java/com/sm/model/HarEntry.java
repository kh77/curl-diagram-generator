package com.sm.model;

import java.util.Map;

public class HarEntry {
    private String method;
    private String url;
    private Map<String, String> headers;
    private String requestBody;
    private int responseStatus;
    private String responseBody;
    private long time;
    private String serverIPAddress;

    // Constructors
    public HarEntry() {}

    public HarEntry(String method, String url, Map<String, String> headers, 
                   String requestBody, int responseStatus, String responseBody, 
                   long time, String serverIPAddress) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.requestBody = requestBody;
        this.responseStatus = responseStatus;
        this.responseBody = responseBody;
        this.time = time;
        this.serverIPAddress = serverIPAddress;
    }

    // Getters and Setters
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getServerIPAddress() {
        return serverIPAddress;
    }

    public void setServerIPAddress(String serverIPAddress) {
        this.serverIPAddress = serverIPAddress;
    }

    @Override
    public String toString() {
        return "HarEntry{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", responseStatus=" + responseStatus +
                ", time=" + time +
                '}';
    }
}