package com.catalogservice.vo;

import com.catalogservice.jpa.CatalogEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCatalog {
    private String productId;
    private String productName;
    private Integer unitPrice;
    private Integer stock;
    private Date createdAt;

    public static ResponseCatalog from(CatalogEntity catalogEntity) {
        return ResponseCatalog.builder()
                .productId(catalogEntity.getProductId())
                .productName(catalogEntity.getProductName())
                .unitPrice(catalogEntity.getUnitPrice())
                .stock(catalogEntity.getStock())
                .createdAt(catalogEntity.getCreatedAt())
                .build();
    }
}
