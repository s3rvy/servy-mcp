package com.servy.mcp.tool;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for ToolRegistry.
 * Tests the ServiceLoader-based tool discovery mechanism.
 */
class ToolRegistryTest {

    @Test
    void shouldCreateRegistry() {
        ToolRegistry registry = new ToolRegistry();

        assertThat(registry).isNotNull();
    }

    @Test
    void shouldDiscoverEchoTool() {
        ToolRegistry registry = new ToolRegistry();
        Tool echoTool = registry.get("echo");

        assertThat(echoTool)
            .isNotNull()
            .extracting(Tool::name)
            .isEqualTo("echo");
    }

    @Test
    void shouldReturnNullForUnknownTool() {
        ToolRegistry registry = new ToolRegistry();
        Tool unknownTool = registry.get("non-existent-tool");

        assertThat(unknownTool).isNull();
    }

    @Test
    void shouldReturnAllTools() {
        ToolRegistry registry = new ToolRegistry();
        Map<String, Tool> tools = registry.getAll();

        assertThat(tools)
            .isNotNull()
            .isNotEmpty()
            .containsKey("echo");
    }

    @Test
    void shouldReturnUnmodifiableMap() {
        ToolRegistry registry = new ToolRegistry();
        Map<String, Tool> tools = registry.getAll();

        assertThatThrownBy(() -> tools.put("test", null))
            .isInstanceOf(UnsupportedOperationException.class);
    }
}
