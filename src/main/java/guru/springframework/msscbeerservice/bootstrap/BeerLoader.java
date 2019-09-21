package guru.springframework.msscbeerservice.bootstrap;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class BeerLoader implements CommandLineRunner {

    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";

    // We can't override Id properties Explicitly in JPA
     // private static final UUID BEER1_UUID = UUID.fromString("0a818933-087d-47f2-ad83-2f986ed087eb");
     // private static final UUID BEER2_UUID = UUID.fromString("a712d914-61ea-4623-8bd0-32c0f6545bfd");
     // private static final UUID BEER3_UUID = UUID.fromString("026cc3c8-3a0c-4083-a05b-e908048c1b08");

    private final BeerRepository beerRepository;

    public BeerLoader(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadBeerObjects();
    }

    private void loadBeerObjects() {
        if(beerRepository.count() == 0) {
            beerRepository.save(Beer.builder()
                    //.id(BEER1_UUID)
                    .beerName("Mango Robs")
                    .beerStyle("IPA")
                    .upc(BEER_1_UPC)
                    .price(new BigDecimal(12.5))
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .build());

            beerRepository.save(Beer.builder()
                    //.id(BEER2_UUID)
                    .beerName("Galaxy Cat")
                    .beerStyle("PALE_ALE")
                    .upc(BEER_2_UPC)
                    .price(new BigDecimal(12.5))
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .build());

            beerRepository.save(Beer.builder()
                    //.id(BEER3_UUID)
                    .beerName("Pinball Porter")
                    .beerStyle("PALE_ALE")
                    .upc(BEER_3_UPC)
                    .price(new BigDecimal(12.95))
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .build());
        }
    }
}
