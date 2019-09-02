package guru.springframework.msscbeerservice.web.controller;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/v1/beer")
@RestController
public class BeerController {

    @PostMapping
    public ResponseEntity saveBeer(@Valid @RequestBody BeerDto beerDto) {
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") String beerId) {
        return new ResponseEntity<>(new BeerDto().builder().build(), HttpStatus.OK);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity updateBeerById(@PathVariable("beerId") String beerId, @Valid @RequestBody BeerDto beerDto) {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
