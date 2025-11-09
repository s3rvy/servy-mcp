package com.servy.mcp.tool.impl;

import com.servy.mcp.tool.ToolResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for EchoTool.
 * Tests the tool implementation without any framework dependencies.
 */
class EchoToolTest {

    @Test
    void shouldReturnCorrectName() {
        EchoTool tool = new EchoTool();
        
        assertThat(tool.name()).isEqualTo("echo");
    }

    @Test
    void shouldReturnNonEmptyDescription() {
        EchoTool tool = new EchoTool();
        
        assertThat(tool.description())
            .isNotNull()
            .isNotEmpty();
    }

    @Test
    void shouldEchoValidInput() {
        EchoTool tool = new EchoTool();
        ToolResult result = tool.execute("Hello World");

        assertThat(result)
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.isSuccess()).isTrue();
                assertThat(r.getOutput()).isEqualTo("ECHO: Hello World");
                assertThat(r.getToolName()).isEqualTo("echo");
                assertThat(r.getError()).isNull();
            });
    }

    @Test
    void shouldHandleNullInput() {
        EchoTool tool = new EchoTool();
        ToolResult result = tool.execute(null);

        assertThat(result)
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.isSuccess()).isFalse();
                assertThat(r.getError()).isEqualTo("no input");
                assertThat(r.getToolName()).isEqualTo("echo");
                assertThat(r.getOutput()).isNull();
            });
    }

    @Test
    void shouldHandleEmptyInput() {
        EchoTool tool = new EchoTool();
        ToolResult result = tool.execute("");

        assertThat(result)
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.isSuccess()).isTrue();
                assertThat(r.getOutput()).isEqualTo("ECHO: ");
            });
    }
}
