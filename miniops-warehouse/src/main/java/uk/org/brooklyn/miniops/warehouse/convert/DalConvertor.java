package uk.org.brooklyn.miniops.warehouse.convert;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import uk.org.brooklyn.miniops.warehouse.model.request.PageReq;
import uk.org.brooklyn.miniops.warehouse.model.response.PageResp;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ImBrooklyn
 * @since 01/05/2025
 */
@Mapper(componentModel = "spring")
public abstract class DalConvertor {
    public Pageable toPageable(PageReq pageReq) {
        List<String> orderFields = Optional.of(pageReq)
                .map(PageReq::getOrderBy)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        Collections::unmodifiableList
                ));

        List<Boolean> directions = Optional.of(pageReq)
                .map(PageReq::getIsDesc)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        Collections::unmodifiableList
                ));

        Sort sort;
        if (CollectionUtils.isEmpty(orderFields)) {
            sort = Sort.by(Sort.Direction.DESC, "createdAt");
        } else {
            List<Sort.Order> orders = new ArrayList<>(orderFields.size());
            for (int i = 0; i < orderFields.size(); i++) {
                Sort.Direction direction;
                if (i >= directions.size()) {
                    direction = null;
                } else {
                    direction = directions.get(i) ? Sort.Direction.DESC : Sort.Direction.ASC;
                }
                orders.add(new Sort.Order(direction, orderFields.get(i)));
            }

            sort = Sort.by(orders);
        }

        return PageRequest.of(pageReq.getPageNum() - 1, pageReq.getPageSize(), sort);
    }

    public <E, R extends Serializable> PageResp<R> toPageResp(Page<E> page, Function<E, R> beanMapper) {
        PageResp<R> pageResp = new PageResp<>();
        pageResp.setTotalPages(page.getTotalPages());
        pageResp.setTotal(page.getTotalElements());
        pageResp.setCurrentPage(page.getNumber());
        pageResp.setHasNext(page.hasNext());
        pageResp.setHasPrev(page.hasPrevious());
        pageResp.setList(
                Optional.of(page)
                        .map(Page::getContent)
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .map(beanMapper)
                        .toList()
        );

        return pageResp;

    }
}
