package com.sm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sm.controller.request.DiagramRequest;
import com.sm.controller.response.DiagramResponse;
import com.sm.model.CurlCommand;
import com.sm.model.HarEntry;
import com.sm.service.CurlParserService;
import com.sm.service.DiagramGeneratorService;
import com.sm.service.HarParserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/diagrams")
@CrossOrigin(origins = "*") // Allow AJAX calls from same domain
public class DiagramController {

    private final CurlParserService curlParserService;
    private final HarParserService harParserService;
    private final DiagramGeneratorService diagramGeneratorService;

    public DiagramController(CurlParserService curlParserService, HarParserService harParserService, DiagramGeneratorService diagramGeneratorService) {
        this.curlParserService = curlParserService;
        this.harParserService = harParserService;
        this.diagramGeneratorService = diagramGeneratorService;
    }

    @PostMapping("/generate")
    public ResponseEntity<DiagramResponse> generateDiagram(@RequestBody DiagramRequest request) {
        try {
            List<CurlCommand> commands = curlParserService.parseCurlCommands(request.getCurlCommands());

            if (commands.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            DiagramResponse response = diagramGeneratorService.generateDiagram(request.getDiagramType(), commands, request.getTitle());

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

    @PostMapping("/generate-from-har")
    public ResponseEntity<DiagramResponse> generateDiagramFromHar(
            @RequestParam("harFile") MultipartFile harFile,
            @RequestParam("diagramType") String diagramType,
            @RequestParam(value = "title", required = false, defaultValue = "API Diagram") String title) {

        try {
            // Validate input
            if (harFile == null || harFile.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new DiagramResponse("error", "No HAR file provided", null));
            }

            if (!isValidDiagramType(diagramType)) {
                return ResponseEntity.badRequest()
                        .body(new DiagramResponse("error", "Invalid diagram type. Use: sequence, flowchart, or network", null));
            }

            // Check file type
            String originalFilename = harFile.getOriginalFilename();
            if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".har")) {
                return ResponseEntity.badRequest()
                        .body(new DiagramResponse("error", "File must be a .har file", null));
            }

            // Parse HAR file
            List<HarEntry> harEntries = harParserService.parseHarFile(harFile);

            if (harEntries.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new DiagramResponse("error", "No valid entries found in HAR file", null));
            }

            // Convert HAR entries to curl commands
            List<String> curlCommands = harParserService.convertToCurlCommands(harEntries);

            // string to multiple curl commands
            List<CurlCommand> commands = curlParserService.parseCurlCommands(String.join(" \n ", curlCommands));

            if (commands.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            DiagramResponse response = diagramGeneratorService.generateDiagram(diagramType, commands, title);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            DiagramResponse response = new DiagramResponse("error",
                                                           "Failed to process HAR file: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Diagram service is running!");
    }

    private boolean isValidDiagramType(String diagramType) {
        return diagramType != null &&
                (diagramType.equals("sequence") ||
                        diagramType.equals("flowchart") ||
                        diagramType.equals("network"));
    }
}
