package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;


import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;

    @Test
    void testListBeers(){
        beerClient.listBeers(null, BeerStyle.LAGER, null, null, null);
    }

    @Test
    void testListBeersNoName(){
        beerClient.listBeers(null, null, null, null, null);
    }

    @Test
    void testGetBeerById(){
        UUID beerId = beerClient.listBeers(null, null, null, null, null).get().findFirst().get().getId();

        BeerDTO beer = beerClient.getBeerById(beerId);
        assertNotNull(beer);
    }

    @Test
    void testSaveBeer(){
        BeerDTO newBeer = BeerDTO.builder()
                .beerName("test beer")
                .beerStyle(BeerStyle.PORTER)
                .upc("123456789")
                .price(new BigDecimal(12))
                .quantityOnHand(12)
                .build();

        BeerDTO savedBeer = beerClient.saveBeer(newBeer);
        assertNotNull(savedBeer);
    }

    @Test
    void testUpdateBeer(){
        BeerDTO fetchedBeer = beerClient.listBeers().toList().get(0);
        final String beerName = "updated beer";
        fetchedBeer.setBeerName(beerName);
        fetchedBeer.setQuantityOnHand(0);

        BeerDTO updatedBeer = beerClient.updateBeer(fetchedBeer,fetchedBeer.getId());
        assertEquals(beerName,updatedBeer.getBeerName());
    }

    @Test
    void testDeleteById()  {
        BeerDTO beer = beerClient.listBeers().toList().get(0);
        beerClient.deleteById(beer.getId());

        assertThrows(HttpClientErrorException.class, () -> {
            beerClient.getBeerById(beer.getId());
        });
    }
}