package com.servy.mcp.plugin;

import com.servy.mcp.tool.Tool;
import com.servy.mcp.tool.ToolRegistry;
import io.quarkiverse.mcp.server.core.ToolManager;
import io.quarkiverse.mcp.server.core.ToolRef;
import io.quarkiverse.mcp.server.core.Value;
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
            toolManager.registerTool(ToolRef.of(name, tool.description())
                .blocking() // External tools are considered blocking by default
                .handler(ctx -> {
                    try {
                        String input = ctx.arguments().get("input").asString();
                        var result = tool.execute(input);
                        return CompletableFuture.completedFuture(
                            result.isSuccess() 
                                ? Value.of(result.getOutput())
                                : Value.error(result.getError())
                        );
                    } catch (Exception e) {
                        return CompletableFuture.completedFuture(
                            Value.error("Tool execution failed: " + e.getMessage())
                        );
                    }
                })
                .build());
        });
    }
}