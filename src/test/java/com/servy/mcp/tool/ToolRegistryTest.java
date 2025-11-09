package com.servy.mcp.tool;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ToolRegistry.
 * Tests the ServiceLoader-based tool discovery mechanism.
 */
class ToolRegistryTest {

    @Test
    void shouldCreateRegistry() {
        ToolRegistry registry = new ToolRegistry();
        assertNotNull(registry);
    }

    @Test
    void shouldDiscoverEchoTool() {
        ToolRegistry registry = new ToolRegistry();
        Tool echoTool = registry.get("echo");

        assertNotNull(echoTool, "Echo tool should be discovered via ServiceLoader");
        assertEquals("echo", echoTool.name());
    }

    @Test
    void shouldReturnNullForUnknownTool() {
        ToolRegistry registry = new ToolRegistry();
        Tool unknownTool = registry.get("non-existent-tool");

        assertNull(unknownTool);
    }

    @Test
    void shouldReturnAllTools() {
        ToolRegistry registry = new ToolRegistry();
        Map<String, Tool> tools = registry.getAll();

        assertNotNull(tools);
        assertFalse(tools.isEmpty(), "Should have at least one tool (echo)");
        assertTrue(tools.containsKey("echo"));
    }

    @Test
    void shouldReturnUnmodifiableMap() {
        ToolRegistry registry = new ToolRegistry();
        Map<String, Tool> tools = registry.getAll();

        assertThrows(UnsupportedOperationException.class, () -> {
            tools.put("test", null);
        }, "getAll() should return an unmodifiable map");
    }
}
