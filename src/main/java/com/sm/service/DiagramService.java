package com.sm.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sm.controller.response.DiagramResponse;
import com.sm.model.CurlCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DiagramService {

    @Autowired
    DiagramGeneratorService diagramGeneratorService;

    public Map<String, DiagramResponse> generateDiagram(List<CurlCommand> commands, String title) {
        Map<String, DiagramResponse> responses = new HashMap<>();

        responses.put("sequence", diagramGeneratorService.generateSequenceDiagram(commands, title));
        responses.put("flowchart", diagramGeneratorService.generateFlowchartDiagram(commands, title));
        responses.put("network", diagramGeneratorService.generateNetworkDiagram(commands, title));

        return responses;
    }
}
