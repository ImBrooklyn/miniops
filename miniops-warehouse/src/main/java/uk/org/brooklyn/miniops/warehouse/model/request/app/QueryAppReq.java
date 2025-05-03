package uk.org.brooklyn.miniops.warehouse.model.request.app;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.org.brooklyn.miniops.warehouse.model.request.PageReq;

/**
 * @author ImBrooklyn
 * @since 01/05/2025
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryAppReq extends PageReq {
    private String name;
}
