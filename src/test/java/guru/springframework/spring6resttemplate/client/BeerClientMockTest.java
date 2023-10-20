package guru.springframework.spring6resttemplate.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6resttemplate.Utils.Constants;
import guru.springframework.spring6resttemplate.config.RestTemplateBuilderConfiguration;
import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static guru.springframework.spring6resttemplate.Utils.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;


@RestClientTest
@Import(RestTemplateBuilderConfiguration.class)
public class BeerClientMockTest {

    BeerClient beerClient;

    MockRestServiceServer server;

    @Autowired
    RestTemplateBuilder restTemplateBuilderConfigured;

    @Autowired
    ObjectMapper objectMapper;

    @Mock
    RestTemplateBuilder mockRestTemplateBuilder = new RestTemplateBuilder(new MockServerRestTemplateCustomizer());

    BeerDTO beer;
    String payload;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestTemplate restTemplate = restTemplateBuilderConfigured.build();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        when(mockRestTemplateBuilder.build()).thenReturn(restTemplate);
        beerClient = new BeerClientImpl(mockRestTemplateBuilder);

        beer = getBeerDto();
        payload = objectMapper.writeValueAsString(beer);
    }

    @Test // TODO : find how to use this for a query param with space in the value
    void testListBeerWithQueryParams() throws JsonProcessingException {
        payload = objectMapper.writeValueAsString(getPage());

        URI url = UriComponentsBuilder.fromHttpUrl(BASE_URL + GET_BEER_PATH)
                .queryParam("beerName","mango")
                .build().toUri();

        server.expect(method(HttpMethod.GET))
                .andExpect(header(AUTHORIZATION_HEADER,BASIC_AUTH))
                .andExpect(requestTo(url))
                .andExpect(queryParam("beerName","mango"))
                .andRespond(withSuccess(payload,MediaType.APPLICATION_JSON));

        Page<BeerDTO> beerList = beerClient.listBeers("mango",null,null,null,null);

        assertThat(beerList.getContent().size()).isGreaterThan(0);
        server.verify();
    }

    @Test
    void testDeleteIdNotFound(){
        server.expect(method(HttpMethod.DELETE))
                .andExpect(header(AUTHORIZATION_HEADER,BASIC_AUTH))
                .andExpect(requestToUriTemplate(BASE_URL + GET_BEER_BY_ID_PATH, beer.getId()))
                .andRespond(withResourceNotFound());

        assertThrows(HttpClientErrorException.NotFound.class, () -> {
            beerClient.deleteById(beer.getId());
        });

        server.verify();
    }

    @Test
    void testDeleteBeer(){
        server.expect(method(HttpMethod.DELETE))
                .andExpect(header(AUTHORIZATION_HEADER,BASIC_AUTH))
                .andExpect(requestToUriTemplate(BASE_URL + GET_BEER_BY_ID_PATH, beer.getId()))
                .andRespond(withNoContent());

        beerClient.deleteById(beer.getId());

        server.verify();
    }

    @Test
    void testUpdateBeer() throws ChangeSetPersister.NotFoundException {
        server.expect(method(HttpMethod.PUT))
                .andExpect(header(AUTHORIZATION_HEADER,BASIC_AUTH))
                .andExpect(requestToUriTemplate(BASE_URL + GET_BEER_BY_ID_PATH, beer.getId()))
                .andRespond(withNoContent());

        mockGetOperation();

        BeerDTO updatedBeer = beerClient.updateBeer(beer,beer.getId());
        assertThat(updatedBeer.getId()).isEqualTo(beer.getId());
    }

    private void mockGetOperation() {
        server.expect(method(HttpMethod.GET))
                .andExpect(header(AUTHORIZATION_HEADER,BASIC_AUTH))
                .andExpect(requestToUriTemplate(BASE_URL + GET_BEER_BY_ID_PATH, beer.getId()))
                .andRespond(withSuccess(payload,MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateBeer() throws JsonProcessingException, ChangeSetPersister.NotFoundException {
        URI location = UriComponentsBuilder.fromPath(GET_BEER_BY_ID_PATH)
                .build(beer.getId());

        server.expect(method(HttpMethod.POST))
                .andExpect(header(AUTHORIZATION_HEADER,BASIC_AUTH))
                .andExpect(requestToUriTemplate(BASE_URL+GET_BEER_PATH))
                .andRespond(withCreatedEntity(location));

        mockGetOperation();

        BeerDTO savedBeer = beerClient.saveBeer(beer);
        assertThat(savedBeer.getBeerName()).isEqualTo(beer.getBeerName());
    }

    @Test
    void testGetBeerById() throws JsonProcessingException, ChangeSetPersister.NotFoundException {
        mockGetOperation();

        BeerDTO returnedBeer = beerClient.getBeerById(beer.getId());
        assertThat(returnedBeer.getId()).isEqualTo(beer.getId());
    }

    @Test
    void testListBeers() throws JsonProcessingException {
        payload = objectMapper.writeValueAsString(getPage());

        server.expect(method(HttpMethod.GET))
                .andExpect(header(AUTHORIZATION_HEADER,BASIC_AUTH))
                .andExpect(requestTo(BASE_URL + Constants.GET_BEER_PATH))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        Page<BeerDTO> dtos = beerClient.listBeers();
        assertThat(dtos.getContent().size()).isGreaterThan(0);
    }

    BeerDTO getBeerDto(){
        return BeerDTO.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123245")
                .build();
    }

    BeerDTOPageImpl getPage(){
        return new BeerDTOPageImpl(Arrays.asList(getBeerDto()), 1, 25, 1);
    }
}

