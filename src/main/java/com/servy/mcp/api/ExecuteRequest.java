package com.servy.mcp.api;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExecuteRequest {
    List<String> tools;
    String input;
}
