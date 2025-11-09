# servy-mcp

Minimal Quarkus-based MCP server skeleton.

Quick start

1. Build and run in dev mode:

```bash
mvn quarkus:dev
```

2. Example request (POST http://localhost:8080/mcp/execute):

```json
{
  "tools": ["echo"],
  "input": "hello world"
}
```

You should get a JSON response with one ToolResult from the built-in echo tool.

Next steps
- Add unit tests
- Add docs for creating external plugin jars (ServiceLoader + META-INF/services)
- Add dynamic plugin loading if you want to load jars at runtime
