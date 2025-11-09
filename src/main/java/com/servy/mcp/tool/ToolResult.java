package com.servy.mcp.tool;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ToolResult {
    String toolName;
    boolean success;
    String output;
    String error;
}
