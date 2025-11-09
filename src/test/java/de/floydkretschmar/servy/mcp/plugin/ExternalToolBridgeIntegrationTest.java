package de.floydkretschmar.servy.mcp.plugin;

import io.quarkiverse.mcp.server.test.McpAssured;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

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
                    assertThat(r.content().get(0).asText().text())
                        .isEqualTo("ECHO: test");
                })
                .toolsCall("echo", r -> {
                    assertThat(r.isError())
                        .as("Echo tool should return error when called without input")
                        .isTrue();
                })
                .thenAssertResults();
    }
}

