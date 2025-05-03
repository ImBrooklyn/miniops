package uk.org.brooklyn.miniops.confer.client;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@HttpExchange("/warehouse")
public interface WarehouseClient {
    @GetExchange("/application/{name}")
    ResponseEntity<JSONObject> getApp(@PathVariable String name);

    @GetExchange("/deployment/{name}")
    ResponseEntity<JSONObject> getDeployment(@PathVariable String name);
}
