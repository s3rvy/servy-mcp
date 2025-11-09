package de.floydkretschmar.servy.mcp.tool;

import de.floydkretschmar.servy.mcp.plugin.PluginToolLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ToolRegistry.
 * Tests the ServiceLoader-based tool discovery mechanism.
 */
@ExtendWith(MockitoExtension.class)
class ToolRegistryTest {

    @Mock
    PluginToolLoader pluginToolLoader;

    @Mock
    Logger logger;

    @Test
    void shouldCreateRegistry() {
        ToolRegistry registry = new ToolRegistry(pluginToolLoader);

        assertThat(registry).isNotNull();
    }

    @Test
    void shouldReturnAllTools() {
        ToolRegistry registry = new ToolRegistry(pluginToolLoader);
        Map<String, Tool> tools = registry.getTools();

        assertThat(tools)
            .isNotNull()
            .isNotEmpty()
            .containsKey("echo");
    }

    @Test
    void shouldReturnUnmodifiableMap() {
        ToolRegistry registry = new ToolRegistry(pluginToolLoader);
        Map<String, Tool> tools = registry.getTools();

        assertThatThrownBy(() -> tools.put("test", null))
            .isInstanceOf(UnsupportedOperationException.class);
    }
}
