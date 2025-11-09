package com.servy.mcp.plugin;

import io.quarkiverse.mcp.server.test.McpAssured;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for ExternalToolBridge.
 * Tests that tools are properly registered and accessible via the MCP REST endpoint.
 */
@QuarkusTest
class ExternalToolBridgeIntegrationTest {

    @Test
    public void shouldExecuteRegisteredTools() {
        final McpAssured.McpStreamableTestClient client = McpAssured.newConnectedStreamableClient();

        client.when()
                .toolsCall("echo", Map.of("input", "test"), r -> {
                    assertEquals("ECHO: test", r.content().get(0).asText().text());
                })
                .toolsCall("echo", r -> {
                    assertThat(r.isError()).isTrue();
                })
                .thenAssertResults();
    }
}

