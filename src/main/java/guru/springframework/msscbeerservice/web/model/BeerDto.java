package guru.springframework.msscbeerservice.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto {

    // These needs to set by Persistence Layer
    @JsonProperty("beerId")
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
    @JsonFormat(shape= JsonFormat.Shape.STRING)
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

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate myLocalDate;
}
