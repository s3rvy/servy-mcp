package com.servy.mcp.tool.impl;

import com.servy.mcp.tool.ToolResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EchoTool.
 * Tests the tool implementation without any framework dependencies.
 */
class EchoToolTest {

    @Test
    void shouldReturnCorrectName() {
        EchoTool tool = new EchoTool();
        assertEquals("echo", tool.name());
    }

    @Test
    void shouldReturnNonEmptyDescription() {
        EchoTool tool = new EchoTool();
        assertNotNull(tool.description());
        assertFalse(tool.description().isEmpty());
    }

    @Test
    void shouldEchoValidInput() {
        EchoTool tool = new EchoTool();
        ToolResult result = tool.execute("Hello World");

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("ECHO: Hello World", result.getOutput());
        assertEquals("echo", result.getToolName());
        assertNull(result.getError());
    }

    @Test
    void shouldHandleNullInput() {
        EchoTool tool = new EchoTool();
        ToolResult result = tool.execute(null);

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("no input", result.getError());
        assertEquals("echo", result.getToolName());
        assertNull(result.getOutput());
    }

    @Test
    void shouldHandleEmptyInput() {
        EchoTool tool = new EchoTool();
        ToolResult result = tool.execute("");

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("ECHO: ", result.getOutput());
    }
}
