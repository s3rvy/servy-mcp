package de.floydkretschmar.servy.mcp.tool;

import de.floydkretschmar.servy.mcp.plugin.PluginToolLoader;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * ToolRegistry discovers Tool implementations via ServiceLoader and exposes simple lookup by name.
 * Supports both built-in tools (from classpath) and dynamically loaded plugins.
 */
@ApplicationScoped
@Slf4j
public class ToolRegistry {

    private final Map<String, Tool> tools = new HashMap<>();

    public ToolRegistry(PluginToolLoader pluginLoader) {
        ServiceLoader.load(Tool.class).forEach(tool -> {
            tools.put(tool.name(), tool);
        });
        pluginLoader.getDynamicTools().forEach((name, tool) -> {
            if (tools.containsKey(name)) {
                log.warn("Tool '{}' conflicts with default tool - skipping", tool.name());
            }
            else {
                tools.put(name, tool);
            }
        });
    }

    /**
     * Get only built-in tools (from classpath).
     */
    public Map<String, Tool> getTools() {
        return Collections.unmodifiableMap(tools);
    }
}
