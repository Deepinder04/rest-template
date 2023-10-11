package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

}