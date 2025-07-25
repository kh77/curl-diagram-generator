package com.sm.service;


import com.sm.model.CurlCommand;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CurlParserService {
    
    public List<CurlCommand> parseCurlCommands(String curlInput) {
        List<CurlCommand> commands = new ArrayList<>();
        
        // Split by curl commands (each line starting with curl)
        String[] lines = curlInput.split("\\n");
        StringBuilder currentCurl = new StringBuilder();
        
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("curl ") && currentCurl.length() > 0) {
                // Process previous curl command
                commands.add(parseSingleCurl(currentCurl.toString()));
                currentCurl = new StringBuilder();
            }
            
            if (line.startsWith("curl ") || line.startsWith("-") || line.startsWith("\\")) {
                currentCurl.append(line).append(" ");
            }
        }
        
        // Process last command
        if (currentCurl.length() > 0) {
            commands.add(parseSingleCurl(currentCurl.toString()));
        }
        
        return commands;
    }
    
    private CurlCommand parseSingleCurl(String curlString) {
        CurlCommand command = new CurlCommand();
        
        // Clean up the curl string
        curlString = curlString.replaceAll("\\\\\\s*\\n\\s*", " ").trim();
        
        // Extract HTTP method
        Pattern methodPattern = Pattern.compile("-X\\s+(GET|POST|PUT|DELETE|PATCH)");
        Matcher methodMatcher = methodPattern.matcher(curlString);
        if (methodMatcher.find()) {
            command.setMethod(methodMatcher.group(1));
        }
        
        // Extract URL
        Pattern urlPattern = Pattern.compile("curl\\s+(?:-X\\s+\\w+\\s+)?['\"]?(https?://[^\\s'\"]+)");
        Matcher urlMatcher = urlPattern.matcher(curlString);
        if (urlMatcher.find()) {
            command.setUrl(urlMatcher.group(1));
        }
        
        // Extract headers
        Pattern headerPattern = Pattern.compile("-H\\s+['\"]([^'\"]+)['\"]");
        Matcher headerMatcher = headerPattern.matcher(curlString);
        while (headerMatcher.find()) {
            command.addHeader(headerMatcher.group(1));
        }
        
        // Extract data
        Pattern dataPattern = Pattern.compile("--data(?:-raw)?\\s+['\"]([^'\"]*)['\"]");
        Matcher dataMatcher = dataPattern.matcher(curlString);
        if (dataMatcher.find()) {
            command.setData(dataMatcher.group(1));
        }
        
        return command;
    }
}
