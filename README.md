# servy-mcp

A Quarkus-based MCP server that supports external tool plugins.

## Architecture

This MCP server combines two powerful features:
1. Quarkus MCP Server SSE extension for the core MCP protocol implementation
2. A plugin system that allows tools to be developed externally and loaded via Java's ServiceLoader

## Quick Start

Build and run in dev mode:
```bash
./gradlew quarkusDev
```

The server exposes two MCP endpoints:
- `/mcp` - Streamable HTTP endpoint (recommended)
- `/mcp/sse` - Server-Sent Events endpoint

You can use the MCP Inspector to interact with the server:
```bash
npx @modelcontextprotocol/inspector
```

## External Tool Development

Tools can be developed as separate modules/JARs and added to the MCP server. Here's how:

1. Create a new Java project and add the Tool API dependency:
```groovy
dependencies {
    implementation 'com.servy:mcp-tool-api:1.0.0'  // The Tool interface
}
```

2. Implement the Tool interface:
```java
public class MyTool implements Tool {
    @Override
    public String name() {
        return "mytool";
    }

    @Override
    public String description() {
        return "Description of what my tool does";
    }

    @Override
    public ToolResult execute(String input) {
        return ToolResult.builder()
            .toolName(name())
            .success(true)
            .output("Result: " + input)
            .build();
    }
}
```

3. Register your tool via `META-INF/services`:
Create `src/main/resources/META-INF/services/com.servy.mcp.tool.Tool` containing:
```
com.example.MyTool
```

4. Build your tool as a JAR and add it to the MCP server's classpath.

## How it Works

1. On startup, the MCP server:
   - Discovers external tools via ServiceLoader
   - Registers each tool with Quarkus MCP's ToolManager
   - Handles proper lifecycle and execution context

2. When a tool is invoked:
   - Request comes in via MCP protocol
   - ToolManager routes to the appropriate handler
   - External tool is executed with proper error handling
   - Result is returned via MCP protocol

## Building

Build the project:
```bash
./gradlew build
```

Run tests:
```bash
./gradlew test
```

## Next Steps
- Add example external tool modules
- Add dynamic plugin loading (hot reload of JARs)
- Add metrics and monitoring
- Add authentication/authorization
