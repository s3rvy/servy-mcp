package de.floydkretschmar.servy.mcp.plugin;

import de.floydkretschmar.servy.mcp.tool.Tool;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for PluginToolLoaderTest.
 * Tests the ServiceLoader-based tool discovery mechanism from a specified plugin directory.
 */
class PluginToolLoaderTest {
    final static String TEST_PLUGIN_DIR = "src/test/resources/test-plugins";

    @Test
    void shouldInjectPluginToolLoader() {
        PluginToolLoader pluginLoader = new PluginToolLoader(TEST_PLUGIN_DIR);
        assertThat(pluginLoader).isNotNull();
    }

    @Test
    void shouldReturnDynamicToolsMap() {
        PluginToolLoader pluginLoader = new PluginToolLoader(TEST_PLUGIN_DIR);
        Map<String, Tool> tools = pluginLoader.getDynamicTools();

        assertThat(tools).isNotNull();
    }

    @Test
    void shouldReturnUnmodifiableDynamicToolsMap() {
        PluginToolLoader pluginLoader = new PluginToolLoader(TEST_PLUGIN_DIR);
        Map<String, Tool> tools = pluginLoader.getDynamicTools();

        assertThatThrownBy(() -> tools.put("test", null))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldLoadPluginsFromConfiguredDirectory() {
        PluginToolLoader pluginLoader = new PluginToolLoader(TEST_PLUGIN_DIR);
        assertThatNoException().isThrownBy(pluginLoader::loadPlugins);
    }
}



