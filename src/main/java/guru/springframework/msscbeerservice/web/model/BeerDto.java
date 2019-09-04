package guru.springframework.msscbeerservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto {

    // These needs to set by Persistence Layer
    @Null
    private UUID id;

    // These needs to set by Persistence Layer
    @Null
    private Integer version;

    @NotBlank
    @Size(min = 3, max=20)
    private String beerName;

    @NotNull
    private BeerStyleEnum beerStyle;

    @Positive
    @NotNull
    private BigDecimal price;

    @Positive
    @NotNull
    private Long upc;

    @Positive
    private Integer quantityOnHand;

    // These needs to set by Persistence Layer
    @Null
    private OffsetDateTime createdDate;

    // These needs to set by Persistence Layer
    @Null
    private OffsetDateTime lastModifiedDate;
}
