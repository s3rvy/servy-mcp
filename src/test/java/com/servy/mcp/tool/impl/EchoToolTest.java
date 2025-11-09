package com.servy.mcp.tool.impl;

import com.servy.mcp.tool.ToolResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EchoTool implementation.
 */
class EchoToolTest {

    private EchoTool echoTool;

    @BeforeEach
    void setUp() {
        echoTool = new EchoTool();
    }

    @Test
    void testName() {
        assertEquals("echo", echoTool.name());
    }

    @Test
    void testDescription() {
        assertNotNull(echoTool.description());
        assertFalse(echoTool.description().isEmpty());
    }

    @Test
    void testExecuteWithValidInput() {
        String input = "Hello World";
        ToolResult result = echoTool.execute(input);

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("ECHO: Hello World", result.getOutput());
        assertEquals("echo", result.getToolName());
        assertNull(result.getError());
    }

    @Test
    void testExecuteWithEmptyInput() {
        String input = "";
        ToolResult result = echoTool.execute(input);

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("ECHO: ", result.getOutput());
    }

    @Test
    void testExecuteWithNullInput() {
        ToolResult result = echoTool.execute(null);

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("no input", result.getError());
        assertEquals("echo", result.getToolName());
        assertNull(result.getOutput());
    }

    @Test
    void testExecuteWithSpecialCharacters() {
        String input = "Test!@#$%^&*()";
        ToolResult result = echoTool.execute(input);

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("ECHO: Test!@#$%^&*()", result.getOutput());
    }
}
