package com.example.tool;

import com.servy.mcp.tool.Tool;
import com.servy.mcp.tool.ToolResult;
import java.time.LocalDateTime;

/**
 * Example external tool that returns the current timestamp.
 */
public class TimestampTool implements Tool {
    @Override
    public String name() {
        return "timestamp";
    }

    @Override
    public String description() {
        return "Returns the current date and time";
    }

    @Override
    public ToolResult execute(String input) {
        return ToolResult.builder()
            .toolName(name())
            .success(true)
            .output(LocalDateTime.now().toString())
            .build();
    }
}