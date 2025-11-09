package com.servy.mcp.plugin;

import com.servy.mcp.tool.Tool;
import com.servy.mcp.tool.ToolRegistry;
import io.quarkiverse.mcp.server.ToolManager;
import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Bridges external tools (loaded via SPI) with the Quarkus MCP ToolManager.
 * This component discovers external tools at startup and registers them with
 * the MCP server, allowing tools to be developed and deployed as separate modules.
 */
@ApplicationScoped
public class ExternalToolBridge {

    @Inject
    ToolManager toolManager;

    private final ToolRegistry toolRegistry;

    public ExternalToolBridge() {
        this.toolRegistry = new ToolRegistry();
    }

    /**
     * On startup, discover and register all external tools with the Quarkus MCP ToolManager.
     * Each tool is wrapped in a ToolRef that handles execution and error management.
     */
    void onStart(@Observes StartupEvent ev) {
        toolRegistry.getAll().forEach((name, tool) -> {
            toolManager.newTool(name) 
                .setDescription(tool.description())
                .addArgument("input", "Input value for tool execution", true, String.class)
                .setHandler(
                    arguments ->  {
                                 try {
                                     String input = arguments.args().get("input").toString();
                                     var result = tool.execute(input);
                                     return result.isSuccess()
                                             ? ToolResponse.success(result.getOutput())
                                             : ToolResponse.error(result.getError());
                                 } catch (Exception e) {
                                     return ToolResponse.error("Tool execution failed: " + e.getMessage());
                                 }
                             })
                .register();
        });
    }
}