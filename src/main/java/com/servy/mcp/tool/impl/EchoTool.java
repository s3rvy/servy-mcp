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
            return new ToolResult(name(), false, null, "no input");
        }
        String out = "ECHO: " + input;
        return new ToolResult(name(), true, out, null);
    }
}
