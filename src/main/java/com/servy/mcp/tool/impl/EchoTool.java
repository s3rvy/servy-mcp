package com.servy.mcp.tool.impl;

import com.servy.mcp.tool.Tool;
import com.servy.mcp.tool.ToolResult;

/**
 * A tiny example Tool that echoes back the input.
 */
public class EchoTool implements Tool {

    @Override
    public String name() {
        return "echo";
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
