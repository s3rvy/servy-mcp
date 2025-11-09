package de.floydkretschmar.servy.mcp.plugin;

import de.floydkretschmar.servy.mcp.tool.Tool;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Dynamically loads Tool plugins from external JAR files.
 * Scans a configured directory for JAR files and uses ServiceLoader
 * to discover Tool implementations within them.
 */
@ApplicationScoped
@Slf4j
public class PluginToolLoader {
    final String pluginDirectory;

    private final Map<String, Tool> dynamicTools;

    public PluginToolLoader(@ConfigProperty(name = "mcp.plugin.directory", defaultValue = "./plugins") String pluginDirectory) {
        this.dynamicTools = new ConcurrentHashMap<>();
        this.pluginDirectory = pluginDirectory;
    }

    /**
     * Loads plugins from the configured directory on application startup.
     */
    void onStart(@Observes StartupEvent event) {
        loadPlugins();
    }

    /**
     * Scans the plugin directory and loads all JAR files containing Tool implementations.
     */
    public void loadPlugins() {
        Path pluginDir = Paths.get(pluginDirectory);

        if (!Files.exists(pluginDir)) {
            log.info("Plugin directory does not exist: {} - skipping plugin loading", pluginDir);
            return;
        }

        if (!Files.isDirectory(pluginDir)) {
            log.warn("Plugin path is not a directory: {} - skipping plugin loading", pluginDir);
            return;
        }

        log.info("Loading plugins from directory: {}", pluginDir.toAbsolutePath());

        try (Stream<Path> files = Files.list(pluginDir)) {
            files.filter(this::isJarFile)
                .forEach(this::loadToolsFromJar);

            log.info("Plugin loading complete. Loaded {} external tools", dynamicTools.size());
        } catch (IOException e) {
            log.error("Failed to list files in plugin directory: %s".formatted(pluginDir), e);
        }
    }

    /**
     * Checks if a path represents a JAR file.
     */
    private boolean isJarFile(Path path) {
        return Files.isRegularFile(path) && path.toString().toLowerCase().endsWith(".jar");
    }

    /**
     * Loads Tool implementations from a single JAR file using ServiceLoader.
     */
    private void loadToolsFromJar(Path jarPath) {
        try {
            URL jarUrl = jarPath.toUri().toURL();
            try (URLClassLoader classLoader = new URLClassLoader(
                new URL[]{jarUrl},
                Thread.currentThread().getContextClassLoader()
            )){
                ServiceLoader.load(Tool.class, classLoader).stream()
                        .map(ServiceLoader.Provider::get)
                        .filter(tool -> {
                            if (dynamicTools.containsKey(tool.name())) {
                                log.warn("Tool '{}' from {} conflicts with already loaded tool - skipping",
                                        tool.name(), jarPath.getFileName());
                                return true;
                            }
                            return false;
                        })
                        .forEach(tool -> {
                            dynamicTools.put(tool.name(), tool);
                            log.info("Loaded external tool '{}' from {}", tool.name(), jarPath.getFileName());
                        });
            }
        } catch (Exception e) {
            log.error("Failed to load tools from JAR: %s".formatted(jarPath), e);
        }
    }

    /**
     * Returns all dynamically loaded tools.
     */
    public Map<String, Tool> getDynamicTools() {
        return Collections.unmodifiableMap(dynamicTools);
    }
}

