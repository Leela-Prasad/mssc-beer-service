package guru.springframework.msscbeerservice.web.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public class BaseTest {

    @Autowired
    ObjectMapper objectMapper;

    public BeerDto getDto() {
        return BeerDto.builder()
                        .id(UUID.randomUUID())
                        .beerName("Beer Name")
                        .beerStyle(BeerStyleEnum.ALE)
                        .createdDate(OffsetDateTime.now())
                        .lastModifiedDate(OffsetDateTime.now())
                        .price(new BigDecimal("12.9"))
                        .upc(124948984L)
                        .myLocalDate(LocalDate.now())
                        .build();
    }
}
