package guru.springframework.msscbeerservice.mappers;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.services.inventory.BeerInventoryService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BeerMapperDecorator implements BeerMapper {

    @Autowired
    private BeerMapper beerMapper;

    @Autowired
    private BeerInventoryService beerInventoryService;

    @Override
    public Beer beerDtoToBeer(BeerDto beerDto) {
        return beerMapper.beerDtoToBeer(beerDto);
    }

    @Override
    public BeerDto beerToBeerDto(Beer beer) {
        return beerMapper.beerToBeerDto(beer);
    }

    @Override
    public BeerDto beerToBeerDtoWithInventory(Beer beer) {
        BeerDto beerDto = beerMapper.beerToBeerDto(beer);
        beerDto.setQuantityOnHand(beerInventoryService.getOnHandInventory(beerDto.getId()));
        return beerDto;
    }
}
