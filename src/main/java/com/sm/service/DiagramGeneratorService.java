package com.sm.service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sm.model.CurlCommand;
import com.sm.controller.response.DiagramResponse;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import org.springframework.stereotype.Service;

@Service
public class DiagramGeneratorService {

    public DiagramResponse generateSequenceDiagram(List<CurlCommand> commands, String title) {
        StringBuilder plantuml = new StringBuilder();
        plantuml.append("@startuml\n");

        if (title != null && !title.isEmpty()) {
            plantuml.append("title ").append(title).append("\n");
        }

        plantuml.append("participant Client\n");

        // Add unique participants
        commands.stream()
                .map(CurlCommand::getHost)
                .distinct()
                .forEach(host -> plantuml.append("participant \"").append(host).append("\" as ").append(sanitize(host)).append("\n"));

        plantuml.append("\n");

        // Add interactions
        for (int i = 0; i < commands.size(); i++) {
            CurlCommand cmd = commands.get(i);
            String hostVar = sanitize(cmd.getHost());

            plantuml.append("Client -> ").append(hostVar).append(": ")
                    .append(cmd.getMethod()).append(" ").append(cmd.getPath());

            if (cmd.getData() != null && !cmd.getData().isEmpty()) {
                plantuml.append("\\n").append(truncateData(cmd.getData()));
            }

            plantuml.append("\n");
            plantuml.append(hostVar).append(" --> Client: Response\n");

            if (i < commands.size() - 1) {
                plantuml.append("\n");
            }
        }

        plantuml.append("@enduml");

        String plantUmlSource = plantuml.toString();
        String base64Image = generatePlantUmlImage(plantUmlSource);

        DiagramResponse response = new DiagramResponse("sequence", plantUmlSource, base64Image);

        // Add metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("commandCount", commands.size());
        metadata.put("uniqueHosts", commands.stream().map(CurlCommand::getHost).distinct().count());
        response.setMetadata(metadata);

        return response;
    }

    public DiagramResponse generateFlowchartDiagram(List<CurlCommand> commands, String title) {
        StringBuilder mermaid = new StringBuilder();
        mermaid.append("flowchart TD\n");

        if (title != null && !title.isEmpty()) {
            mermaid.append("    title[\"").append(title).append("\"]\n");
        }

        mermaid.append("    Start([Start])\n");

        for (int i = 0; i < commands.size(); i++) {
            CurlCommand cmd = commands.get(i);
            String nodeId = "API" + i;

            mermaid.append("    ").append(nodeId).append("[\"")
                    .append(cmd.getMethod()).append(" ")
                    .append(cmd.getHost()).append(cmd.getPath())
                    .append("\"]\n");

            if (i == 0) {
                mermaid.append("    Start --> ").append(nodeId).append("\n");
            } else {
                mermaid.append("    API").append(i - 1).append(" --> ").append(nodeId).append("\n");
            }
        }

        mermaid.append("    API").append(commands.size() - 1).append(" --> End([End])\n");

        // Convert Mermaid to PlantUML for image generation
        StringBuilder plantuml = new StringBuilder();
        plantuml.append("@startuml\n");
        plantuml.append("start\n");

        for (int i = 0; i < commands.size(); i++) {
            CurlCommand cmd = commands.get(i);
            plantuml.append(":").append(cmd.getMethod()).append(" ")
                    .append(cmd.getHost()).append(cmd.getPath()).append(";\n");
        }

        plantuml.append("stop\n");
        plantuml.append("@enduml");

        String plantUmlSource = plantuml.toString();
        String base64Image = generatePlantUmlImage(plantUmlSource);

        DiagramResponse response = new DiagramResponse("flowchart", plantUmlSource, base64Image);
        response.setMermaidSource(mermaid.toString());

        return response;
    }

    public DiagramResponse generateNetworkDiagram(List<CurlCommand> commands, String title) {
        StringBuilder plantuml = new StringBuilder();
        plantuml.append("@startuml\n");

        if (title != null && !title.isEmpty()) {
            plantuml.append("title ").append(title).append("\n");
        }

        plantuml.append("!define RECTANGLE class\n");
        plantuml.append("skinparam rectangle {\n");
        plantuml.append("    BackgroundColor LightBlue\n");
        plantuml.append("    BorderColor Black\n");
        plantuml.append("}\n\n");

        // Add client
        plantuml.append("rectangle Client\n");

        // Add unique hosts
        commands.stream()
                .map(CurlCommand::getHost)
                .distinct()
                .forEach(host -> plantuml.append("rectangle \"").append(host).append("\" as ").append(sanitize(host)).append("\n"));

        plantuml.append("\n");

        // Add connections
        commands.forEach(cmd -> {
            String hostVar = sanitize(cmd.getHost());
            plantuml.append("Client --> ").append(hostVar)
                    .append(" : ").append(cmd.getMethod()).append(" ").append(cmd.getPath()).append("\n");
        });

        plantuml.append("@enduml");

        String plantUmlSource = plantuml.toString();
        String base64Image = generatePlantUmlImage(plantUmlSource);

        return new DiagramResponse("network", plantUmlSource, base64Image);
    }

    private String generatePlantUmlImage(String plantUmlSource) {
        try {
            SourceStringReader reader = new SourceStringReader(plantUmlSource);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            reader.outputImage(os, new FileFormatOption(FileFormat.PNG));
            return Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String sanitize(String input) {
        if (input == null) return "Unknown";
        return input.replaceAll("[^a-zA-Z0-9]", "");
    }

    private String truncateData(String data) {
        if (data == null) return "";
        return data.length() > 50 ? data.substring(0, 50) + "..." : data;
    }
}
