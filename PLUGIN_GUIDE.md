# Plugin System Guide

This guide explains how to create and load external MCP tool plugins.

## Overview

The MCP server supports dynamic loading of external tools from JAR files. Tools are discovered using Java's ServiceLoader mechanism.

## Creating an External Tool Plugin

### 1. Create Your Tool Implementation

Create a new Java project with the following structure:

```
my-custom-tool/
├── build.gradle
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── mycompany/
        │           └── MyCustomTool.java
        └── resources/
            └── META-INF/
                └── services/
                    └── tool.de.floydkretschmar.servy.mcp.Tool
```

### 2. Add Dependency

In your `build.gradle`:

```gradle
dependencies {
    compileOnly 'de.floydkretschmar:servy-mcp:1.0.0-SNAPSHOT'
}
```

Or add the servy-mcp JAR to your classpath.

### 3. Implement the Tool Interface

```java
package com.mycompany;

import tool.de.floydkretschmar.servy.mcp.Tool;
import tool.de.floydkretschmar.servy.mcp.ToolResult;

public class MyCustomTool implements Tool {
    
    @Override
    public String name() {
        return "my-tool";
    }
    
    @Override
    public String description() {
        return "Does something useful";
    }
    
    @Override
    public ToolResult execute(String input) {
        if (input == null) {
            return ToolResult.builder()
                .toolName(name())
                .success(false)
                .error("Input is required")
                .build();
        }
        
        // Your tool logic here
        String result = processInput(input);
        
        return ToolResult.builder()
            .toolName(name())
            .success(true)
            .output(result)
            .build();
    }
    
    private String processInput(String input) {
        // Your processing logic
        return "Processed: " + input;
    }
}
```

### 4. Register Your Tool via ServiceLoader

Create the file `src/main/resources/META-INF/services/tool.de.floydkretschmar.servy.mcp.Tool` with the fully qualified class name:

```
com.mycompany.MyCustomTool
```

If you have multiple tools in the same JAR:

```
com.mycompany.MyCustomTool
com.mycompany.AnotherTool
com.mycompany.YetAnotherTool
```

### 5. Build Your Plugin JAR

```bash
./gradlew clean build
```

This produces a JAR file in `build/libs/my-custom-tool-1.0.0.jar`

## Loading External Plugins

### Option 1: Plugin Directory (Recommended)

1. **Configure the plugin directory** in `application.properties`:

```properties
mcp.plugin.directory=./plugins
```

2. **Copy your plugin JAR** to the plugins directory:

```bash
mkdir -p plugins
cp /path/to/my-custom-tool-1.0.0.jar plugins/
```

3. **Start the MCP server**:

```bash
./gradlew quarkusDev
```

The server will automatically discover and load all tools from JARs in the plugin directory.

### Option 2: Production Deployment

For production deployments, copy plugin JARs to the `lib` directory:

```bash
# After building with ./gradlew build
cp my-custom-tool.jar build/quarkus-app/lib/

# Run the application
java -jar build/quarkus-app/quarkus-run.jar
```

### Option 3: Docker Deployment

```dockerfile
FROM registry.access.redhat.com/ubi8/openjdk-21

COPY build/quarkus-app/lib/ /deployments/lib/
COPY build/quarkus-app/*.jar /deployments/
COPY build/quarkus-app/app/ /deployments/app/
COPY build/quarkus-app/quarkus/ /deployments/quarkus/

# Copy external plugins
COPY plugins/*.jar /deployments/lib/

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0"
ENTRYPOINT ["java", "-jar", "/deployments/quarkus-run.jar"]
```

## Configuration

### Application Properties

```properties
# Plugin directory path (default: ./plugins)
mcp.plugin.directory=/opt/mcp/plugins

# Or use environment variable
# MCP_PLUGIN_DIRECTORY=/opt/mcp/plugins
```

### Environment Variables

```bash
export MCP_PLUGIN_DIRECTORY=/opt/mcp/plugins
./gradlew quarkusDev
```

## Tool Discovery

The server discovers tools in the following order:

1. **Built-in tools** - Tools compiled into the application
2. **Plugin tools** - Tools loaded from JARs in the plugin directory

**Note:** If a plugin tool has the same name as a built-in tool, the built-in tool takes precedence.

## Logging

Enable debug logging to see plugin loading details:

```properties
quarkus.log.category."de.floydkretschmar.mcp.plugin".level=DEBUG
```

You'll see logs like:

```
INFO  [de.flo.mcp.plu.PluginToolLoader] Loading plugins from directory: ./plugins
INFO  [de.flo.mcp.plu.PluginToolLoader] Loaded external tool 'my-tool' from my-custom-tool-1.0.0.jar
INFO  [de.flo.mcp.plu.PluginToolLoader] Plugin loading complete. Loaded 3 external tools
```

## Best Practices

### 1. Tool Naming

- Use lowercase, hyphenated names: `my-tool`, `data-processor`
- Keep names short and descriptive
- Avoid conflicts with built-in tools

### 2. Error Handling

Always handle null/invalid input gracefully:

```java
@Override
public ToolResult execute(String input) {
    if (input == null || input.isBlank()) {
        return ToolResult.builder()
            .toolName(name())
            .success(false)
            .error("Input cannot be empty")
            .build();
    }
    
    try {
        // Your logic
        return ToolResult.builder()
            .toolName(name())
            .success(true)
            .output(result)
            .build();
    } catch (Exception e) {
        return ToolResult.builder()
            .toolName(name())
            .success(false)
            .error("Error: " + e.getMessage())
            .build();
    }
}
```

### 3. Dependencies

If your tool requires external dependencies:

- Include them in your JAR (fat JAR / uber JAR)
- Or ensure they're available on the classpath

Using Gradle shadowJar:

```gradle
plugins {
    id 'com.github.johnrengelman.shadow' version '8.1.1'
}

shadowJar {
    archiveClassifier.set('')
}
```

### 4. Testing

Test your tool independently before deploying:

```java
@Test
void testMyTool() {
    MyCustomTool tool = new MyCustomTool();
    ToolResult result = tool.execute("test input");
    
    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getOutput()).isEqualTo("expected output");
}
```

## Troubleshooting

### Plugin Not Loaded

**Check the logs:**
```
quarkus.log.category."de.floydkretschmar.mcp.plugin".level=DEBUG
```

**Common issues:**
1. ServiceLoader file missing or incorrect path
2. Class not found (wrong package name in ServiceLoader file)
3. JAR not in plugin directory
4. Plugin directory doesn't exist

### Tool Name Conflicts

If you see:
```
WARN  Tool 'my-tool' from plugin.jar conflicts with already loaded tool - skipping
```

Either:
- Rename your tool
- Remove the conflicting built-in tool
- Built-in tools always take precedence

### ClassLoader Issues

If you encounter `ClassNotFoundException` or `NoClassDefFoundError`:

1. Ensure all dependencies are packaged in the JAR
2. Use a fat/uber JAR
3. Check dependency compatibility with Quarkus version

## Example: Complete External Tool

See `docs/external-tool-example/` for a complete working example of an external tool plugin.

## Advanced: Hot Reload (Future Enhancement)

Currently, plugins are loaded once at startup. To add new plugins:
1. Stop the server
2. Add/update JAR files in plugin directory
3. Restart the server

Future versions may support hot-reloading via file watching.

