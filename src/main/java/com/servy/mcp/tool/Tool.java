package com.servy.mcp.tool;

/**
 * Simple SPI interface for MCP tools.
 */
public interface Tool {
    /**
     * Name used to select this tool.
     */
    String name();

    /**
     * Execute the tool with the provided input and return a result.
     */
    ToolResult execute(String input);
}
