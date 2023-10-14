package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BeerClient {

    Page<BeerDTO> listBeers();
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize);
    BeerDTO getBeerById(UUID beerId) throws ChangeSetPersister.NotFoundException;
    BeerDTO saveBeer(BeerDTO beer) throws ChangeSetPersister.NotFoundException;
    BeerDTO updateBeer(BeerDTO beer, UUID beerId) throws ChangeSetPersister.NotFoundException;
    void deleteById(UUID beerId);
}
