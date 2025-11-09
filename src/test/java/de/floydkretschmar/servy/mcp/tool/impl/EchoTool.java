package de.floydkretschmar.servy.mcp.tool.impl;

import de.floydkretschmar.servy.mcp.tool.Tool;
import de.floydkretschmar.servy.mcp.tool.ToolResult;

/**
 * Example external Tool implementation that echoes back the input.
 */
public class EchoTool implements Tool {

    @Override
    public String name() {
        return "echo";
    }

    @Override
    public String description() {
        return "Echoes back the input with a prefix";
    }

    @Override
    public ToolResult execute(String input) {
        if (input == null) {
            return ToolResult.builder()
                .toolName(name())
                .success(false)
                .error("no input")
                .build();
        }
        return ToolResult.builder()
            .toolName(name())
            .success(true)
            .output("ECHO: " + input)
            .build();
    }
}
