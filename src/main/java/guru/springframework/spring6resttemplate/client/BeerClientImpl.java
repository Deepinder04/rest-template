package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static guru.springframework.spring6resttemplate.Utils.Constants.BASE_URL;
import static guru.springframework.spring6resttemplate.Utils.Constants.GET_BEER_PATH;


@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    private final RestTemplateBuilder restTemplateBuilder;

    @Override
    public Page<BeerDTO> listBeers() {
        RestTemplate restTemplate = restTemplateBuilder.build();

        ResponseEntity<BeerDTOPageImpl> pageResponse =
                restTemplate.getForEntity(GET_BEER_PATH, BeerDTOPageImpl.class);

        System.out.println(pageResponse.getBody());

        return null;
    }
}
