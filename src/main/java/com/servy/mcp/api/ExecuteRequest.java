package com.servy.mcp.api;

import java.util.List;

public class ExecuteRequest {
    private List<String> tools;
    private String input;

    public List<String> getTools() {
        return tools;
    }

    public void setTools(List<String> tools) {
        this.tools = tools;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
