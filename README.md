# servy-mcp

Minimal Quarkus-based MCP server skeleton.

Quick start

1. Build and run in dev mode:

```bash
./gradlew quarkusDev
```

2. Example request (POST http://localhost:8080/mcp/execute):

```json
{
  "tools": ["echo"],
  "input": "hello world"
}
```

Example response:
```json
{
  "results": [{
    "toolName": "echo",
    "success": true,
    "output": "ECHO: hello world",
    "error": null
  }]
}
```

## Creating new Tools

Tools can be added by implementing the `Tool` interface and registering via `META-INF/services`. Here's an example:

```java
@Value
public class MyTool implements Tool {
    @Override
    public String name() {
        return "mytool";
    }

    @Override
    public ToolResult execute(String input) {
        return ToolResult.builder()
            .toolName(name())
            .success(true)
            .output("MyTool processed: " + input)
            .build();
    }
}
```

## Building and Testing

Build the project:
```bash
./gradlew build
```

Run tests:
```bash
./gradlew test
```

Next steps
- Add unit tests
- Add docs for creating external plugin jars (ServiceLoader + META-INF/services)
- Add dynamic plugin loading if you want to load jars at runtime
- Add more example tools
