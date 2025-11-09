package com.servy.mcp.rest;

import com.servy.mcp.api.ExecuteRequest;
import com.servy.mcp.api.ExecuteResponse;
import com.servy.mcp.tool.Tool;
import com.servy.mcp.tool.ToolRegistry;
import com.servy.mcp.tool.ToolResult;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/mcp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class McpResource {

    private final ToolRegistry registry = new ToolRegistry();

    @POST
    @Path("/execute")
    public ExecuteResponse execute(ExecuteRequest request) {
        List<ToolResult> results = new ArrayList<>();
        if (request == null || request.getTools() == null) {
            return ExecuteResponse.builder().results(results).build();
        }

        for (String toolName : request.getTools()) {
            Tool t = registry.get(toolName);
            if (t == null) {
                results.add(ToolResult.builder()
                    .toolName(toolName)
                    .success(false)
                    .error("tool not found")
                    .build());
                continue;
            }
            try {
                ToolResult r = t.execute(request.getInput());
                results.add(r);
            } catch (Exception e) {
                results.add(ToolResult.builder()
                    .toolName(toolName)
                    .success(false)
                    .error(e.getMessage())
                    .build());
            }
        }

        return ExecuteResponse.builder().results(results).build();
    }
}
