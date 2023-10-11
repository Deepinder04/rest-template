package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static guru.springframework.spring6resttemplate.Utils.Constants.GET_BEER_BY_ID_PATH;
import static guru.springframework.spring6resttemplate.Utils.Constants.GET_BEER_PATH;


@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    private final RestTemplateBuilder restTemplateBuilder;

    @Override
    public Page<BeerDTO> listBeers() {
        return this.listBeers(null,null,null,null,null);
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String url = buildGetBeerUrl(beerName,beerStyle,showInventory,pageNumber,pageSize);

        System.out.println(url);

        ResponseEntity<BeerDTOPageImpl> response =
                restTemplate.getForEntity(url, BeerDTOPageImpl.class);

        return response.getBody();
    }

    @Override
    public BeerDTO getBeerById(UUID beerId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate.getForObject(GET_BEER_BY_ID_PATH,BeerDTO.class,beerId);
    }

    @Override
    public BeerDTO saveBeer(BeerDTO beer) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<BeerDTO> response = restTemplate.postForEntity(GET_BEER_PATH, beer, BeerDTO.class);

        String[] path = Objects.requireNonNull(response.getHeaders().get("Location")).get(0).split("/");
        BeerDTO savedBeer = getBeerById(UUID.fromString(path[4]));
        return savedBeer;
    }

    @Override
    public BeerDTO updateBeer(BeerDTO beer, UUID beerId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.put(GET_BEER_BY_ID_PATH,beer,beerId);
        return getBeerById(beerId);
    }

    public static String buildGetBeerUrl(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize){
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(GET_BEER_PATH);

        if(beerName!=null)
            uriComponentsBuilder.queryParam("beerName",beerName);

        if(beerStyle!=null)
            uriComponentsBuilder.queryParam("beerStyle",beerStyle);

        if(showInventory!=null)
            uriComponentsBuilder.queryParam("showInventory",showInventory);

        if(pageNumber!=null)
            uriComponentsBuilder.queryParam("pageNumber",pageNumber);

        if(pageSize!=null)
            uriComponentsBuilder.queryParam("pageSize",pageSize);

        return uriComponentsBuilder.toUriString();
    }
}
