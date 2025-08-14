package br.fai.lds.medlink.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/cors")
    public Map<String, String> testCors() {
        return Map.of(
            "message", "CORS está funcionando!",
            "timestamp", String.valueOf(System.currentTimeMillis())
        );
    }

    @PostMapping("/cors")
    public Map<String, String> testCorsPost(@RequestBody(required = false) Map<String, Object> body) {
        return Map.of(
            "message", "POST com CORS funcionando!",
            "received", body != null ? body.toString() : "null"
        );
    }

    public void corsOptions() {
    }
}