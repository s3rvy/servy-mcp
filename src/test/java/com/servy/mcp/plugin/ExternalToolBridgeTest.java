package com.servy.mcp.plugin;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkiverse.mcp.server.ToolManager;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for ExternalToolBridge.
 * Tests that tools are properly registered with the Quarkus MCP ToolManager.
 */
@QuarkusTest
class ExternalToolBridgeTest {

    @Inject
    ToolManager toolManager;

    @Inject
    ExternalToolBridge bridge;

    @Test
    void testBridgeIsInjectable() {
        assertNotNull(bridge, "ExternalToolBridge should be injectable");
    }

    @Test
    void testToolManagerIsInjectable() {
        assertNotNull(toolManager, "ToolManager should be injectable");
    }

    @Test
    void testEchoToolIsRegistered() {
        // The bridge registers tools on startup
        // We can verify by checking that the ToolManager has the echo tool
        var tools = toolManager.getTools();
        assertNotNull(tools);

        var echoTool = tools.stream()
            .filter(t -> "echo".equals(t.getName()))
            .findFirst();

        assertTrue(echoTool.isPresent(), "Echo tool should be registered with ToolManager");
    }

    @Test
    void testEchoToolHasDescription() {
        var tools = toolManager.getTools();
        var echoTool = tools.stream()
            .filter(t -> "echo".equals(t.getName()))
            .findFirst();

        assertTrue(echoTool.isPresent());
        assertNotNull(echoTool.get().getDescription());
        assertFalse(echoTool.get().getDescription().isEmpty());
    }

    @Test
    void testEchoToolHasInputArgument() {
        var tools = toolManager.getTools();
        var echoTool = tools.stream()
            .filter(t -> "echo".equals(t.getName()))
            .findFirst();

        assertTrue(echoTool.isPresent());
        var arguments = echoTool.get().getArguments();
        assertNotNull(arguments);
        assertTrue(arguments.containsKey("input"), "Echo tool should have 'input' argument");
    }
}
