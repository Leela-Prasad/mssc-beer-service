package guru.springframework.msscbeerservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto {

    private UUID id;
    private Integer version;

    private String beerName;
    private BeerStyleEnum beerStyle;

    private BigDecimal price;
    private String upc;
    private Integer quantityOnHand;

    private OffsetDateTime createdDate;
    private OffsetDateTime lastModifiedDate;
}
