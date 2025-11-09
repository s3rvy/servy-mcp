package com.servy.mcp.api;

import com.servy.mcp.tool.ToolResult;

import java.util.List;

public class ExecuteResponse {
    private List<ToolResult> results;

    public ExecuteResponse() {
    }

    public ExecuteResponse(List<ToolResult> results) {
        this.results = results;
    }

    public List<ToolResult> getResults() {
        return results;
    }

    public void setResults(List<ToolResult> results) {
        this.results = results;
    }
}
