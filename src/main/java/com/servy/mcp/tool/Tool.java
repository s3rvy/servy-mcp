package com.servy.mcp.tool;

/**
 * SPI interface for MCP tools that can be developed externally
 * and loaded via ServiceLoader.
 */
public interface Tool {
    /**
     * Name used to select this tool.
     */
    String name();

    /**
     * Description of what the tool does.
     */
    String description();

    /**
     * Execute the tool with the provided input and return a result.
     */
    ToolResult execute(String input);
}
