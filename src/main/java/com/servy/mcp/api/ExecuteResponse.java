package com.servy.mcp.api;

import com.servy.mcp.tool.ToolResult;
import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value
@Builder
public class ExecuteResponse {
    List<ToolResult> results;
}
