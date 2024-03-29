package guru.springframework.msscbeerservice.services.inventory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Slf4j
@ConfigurationProperties(prefix = "sfg.brewery")
@Service
public class BeerInventoryServiceRestTemplateImpl implements BeerInventoryService {

    private static final String INVENTORY_PATH="/api/v1/beer/{beerId}/inventory";

    private String beerInventoryServiceHost;

    private final RestTemplate restTemplate;

    public void setBeerInventoryServiceHost(String beerInventoryServiceHost) {
        this.beerInventoryServiceHost = beerInventoryServiceHost;
    }

    public BeerInventoryServiceRestTemplateImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Integer getOnHandInventory(UUID beerId) {
        log.info("Calling Inventory Service");

        ResponseEntity<List<BeerInventoryDto>> responseEntity = restTemplate.exchange(beerInventoryServiceHost + INVENTORY_PATH,
                                                                                HttpMethod.GET,
                                                                    null,
                                                                                new ParameterizedTypeReference<List<BeerInventoryDto>>() {},
                                                                                (Object) beerId);

        return responseEntity.getBody().stream()
                                .mapToInt(BeerInventoryDto::getQuantityOnHand)
                                .sum();

    }

}
