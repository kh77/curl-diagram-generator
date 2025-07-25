package com.sm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sm.model.CurlCommand;
import com.sm.controller.request.DiagramRequest;
import com.sm.controller.response.DiagramResponse;
import com.sm.service.CurlParserService;
import com.sm.service.DiagramGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diagrams")
@CrossOrigin(origins = "*") // Allow AJAX calls from same domain
public class DiagramController {

    @Autowired
    private CurlParserService curlParserService;

    @Autowired
    private DiagramGeneratorService diagramGeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<DiagramResponse> generateDiagram(@RequestBody DiagramRequest request) {
        try {
            List<CurlCommand> commands = curlParserService.parseCurlCommands(request.getCurlCommands());

            if (commands.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            DiagramResponse response;

            switch (request.getDiagramType().toLowerCase()) {
                case "flowchart":
                    response = diagramGeneratorService.generateFlowchartDiagram(commands, request.getTitle());
                    break;
                case "network":
                    response = diagramGeneratorService.generateNetworkDiagram(commands, request.getTitle());
                    break;
                default: // "sequence":
                    response = diagramGeneratorService.generateSequenceDiagram(commands, request.getTitle());
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/generate-all")
    public ResponseEntity<Map<String, DiagramResponse>> generateAllDiagrams(@RequestBody DiagramRequest request) {
        try {
            List<CurlCommand> commands = curlParserService.parseCurlCommands(request.getCurlCommands());

            if (commands.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Map<String, DiagramResponse> responses = new HashMap<>();

            responses.put("sequence", diagramGeneratorService.generateSequenceDiagram(commands, request.getTitle()));
            responses.put("flowchart", diagramGeneratorService.generateFlowchartDiagram(commands, request.getTitle()));
            responses.put("network", diagramGeneratorService.generateNetworkDiagram(commands, request.getTitle()));

            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Diagram service is running!");
    }
}
