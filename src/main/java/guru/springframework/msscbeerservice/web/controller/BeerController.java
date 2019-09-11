package guru.springframework.msscbeerservice.web.controller;

import guru.springframework.msscbeerservice.services.BeerService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class BeerController {

    private final BeerService beerService;
    private static final Integer DEFAULT_PAGE_NUMBER=0;
    private static final Integer DEFAULT_PAGE_SIZE=25;

    @ResponseStatus(HttpStatus.OK)
    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    @GetMapping("/beer")
    public BeerPagedList listBeers(@RequestParam(value="pageNumber", required = false) Integer pageNumber,
                                                   @RequestParam(value="pageSize", required = false) Integer pageSize,
                                                   @RequestParam(value="beerName", required = false) String beerName,
                                                   @RequestParam(value="beerStyle", required = false) BeerStyleEnum beerStyle,
                                                   @RequestParam(value="showInventoryOnHand", required = false) Boolean showInventoryOnHand
                                                    ) {

        System.out.println("List Beer Invoked");

        if(showInventoryOnHand == null) {
            showInventoryOnHand=false;
        }

        if(pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        if(pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        // Response Entity doesn't implements Serializable, so we have issues with Ehcache.
        //return new ResponseEntity<>(beerService.listBeers(beerName, beerStyle, PageRequest.of(pageNumber, pageSize), showInventoryOnHand), HttpStatus.OK);
        return beerService.listBeers(beerName, beerStyle, PageRequest.of(pageNumber, pageSize), showInventoryOnHand);
    }

    @PostMapping("/beer")
    public ResponseEntity<BeerDto> saveBeer(@Valid @RequestBody BeerDto beerDto) {
        return new ResponseEntity<>(beerService.saveBeer(beerDto), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.OK)
    @Cacheable(cacheNames = "beerCache",key = "#beerId",condition = "#showInventoryOnHand == false")
    @GetMapping("/beer/{beerId}")
    public BeerDto getBeerById(@PathVariable("beerId") UUID beerId,
                                               @RequestParam(value="showInventoryOnHand", required = false) Boolean showInventoryOnHand
                                               ) {

        System.out.println("Get Beer By Id Invoked");

        if(showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }
        //return new ResponseEntity<>(beerService.getBeerById(beerId, showInventoryOnHand), HttpStatus.OK);
        return beerService.getBeerById(beerId, showInventoryOnHand);
    }

    @ResponseStatus(HttpStatus.OK)
    @Cacheable(cacheNames = "beerUpcCache", key="#upc")
    @GetMapping("/beerUpc/{upc}")
    public BeerDto getBeerByUPC(@PathVariable("upc") String upc) {

        System.out.println("Beer By UPC invoked");

        return beerService.getBeerByUPC(upc);
    }

    @PutMapping("/beer/{beerId}")
    public ResponseEntity<BeerDto> updateBeerById(@PathVariable("beerId") UUID beerId, @Valid @RequestBody BeerDto beerDto) {
        return new ResponseEntity(beerService.updateBeer(beerId, beerDto), HttpStatus.NO_CONTENT);
    }

}
