package uk.org.brooklyn.miniops.busyboxj.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author ImBrooklyn
 * @since 01/05/2025
 */
@RestController
@RequestMapping("heartbeat")
@Slf4j
public class HeartbeatController {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${server.port}")
    private Integer serverPort;

    @GetMapping("/ping")
    public ResponseEntity<JSONObject> pingGet(Map<String, Object> queries) {
        log.info("ping get");
        return ResponseEntity.ok(toBody("GET", queries));
    }

    @PutMapping("/ping")
    public ResponseEntity<JSONObject> pingPut(Map<String, Object> queries) {
        log.info("ping put");
        return ResponseEntity.ok(toBody("PUT", queries));
    }

    @PostMapping("/ping")
    public ResponseEntity<JSONObject> pingPost(Map<String, Object> queries) {
        log.info("ping post");
        return ResponseEntity.ok(toBody("POST", queries));
    }

    private JSONObject toBody(String method, Map<String, Object> queries) {
        return new JSONObject()
                .fluentPut("msg", "pong")
                .fluentPut("method", method)
                .fluentPut("appName", appName)
                .fluentPut("serverPort", serverPort)
                .fluentPut("payload", queries);
    }
}
