package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
