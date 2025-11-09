package com.servy.mcp.tool;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * ToolRegistry discovers Tool implementations via ServiceLoader and exposes simple lookup by name.
 */
public class ToolRegistry {
    private final Map<String, Tool> tools = new HashMap<>();

    public ToolRegistry() {
        ServiceLoader<Tool> loader = ServiceLoader.load(Tool.class);
        for (Tool t : loader) {
            tools.put(t.name(), t);
        }
    }

    public Tool get(String name) {
        return tools.get(name);
    }

    public Map<String, Tool> getAll() {
        return Collections.unmodifiableMap(tools);
    }
}
