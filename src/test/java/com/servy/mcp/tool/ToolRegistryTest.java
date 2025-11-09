package com.servy.mcp.tool;

import com.servy.mcp.tool.impl.EchoTool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ToolRegistry.
 * Tests the ServiceLoader-based tool discovery mechanism.
 */
class ToolRegistryTest {

    private ToolRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new ToolRegistry();
    }

    @Test
    void testRegistryLoadsTools() {
        Map<String, Tool> tools = registry.getAll();
        assertNotNull(tools);
        assertFalse(tools.isEmpty(), "Registry should discover at least one tool");
    }

    @Test
    void testGetEchoTool() {
        Tool echoTool = registry.get("echo");
        assertNotNull(echoTool, "Echo tool should be registered");
        assertEquals("echo", echoTool.name());
        assertTrue(echoTool instanceof EchoTool);
    }

    @Test
    void testGetNonExistentTool() {
        Tool tool = registry.get("nonexistent");
        assertNull(tool, "Non-existent tool should return null");
    }

    @Test
    void testGetAllReturnsUnmodifiableMap() {
        Map<String, Tool> tools = registry.getAll();
        assertThrows(UnsupportedOperationException.class, () -> {
            tools.put("test", new EchoTool());
        }, "getAll() should return unmodifiable map");
    }

    @Test
    void testToolsHaveUniqueNames() {
        Map<String, Tool> tools = registry.getAll();
        long uniqueNames = tools.values().stream()
            .map(Tool::name)
            .distinct()
            .count();

        assertEquals(tools.size(), uniqueNames, "All tools should have unique names");
    }
}
