package uk.org.brooklyn.miniops.warehouse.model.request;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

/**
 * @author ImBrooklyn
 * @since 01/05/2025
 */
@Data
public class PageReq {

    @Positive
    private Integer pageNum;

    @Positive
    private Integer pageSize;

    private List<String> orderBy;

    private List<Boolean> isDesc;
}
