package uk.org.brooklyn.miniops.warehouse.model.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ImBrooklyn
 * @since 01/05/2025
 */
@Data
public class PageResp<T extends Serializable> {

    private List<T> list;

    private Long total;

    private Integer totalPages;

    private Integer currentPage;

    private Boolean hasNext;

    private Boolean hasPrev;
}
