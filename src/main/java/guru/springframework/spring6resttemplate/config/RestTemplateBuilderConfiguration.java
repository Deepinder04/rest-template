package guru.springframework.spring6resttemplate.config;

import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static guru.springframework.spring6resttemplate.Utils.Constants.BASE_URL;

@Configuration
public class RestTemplateBuilderConfiguration {

    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer){
        RestTemplateBuilder restTemplateBuilder = configurer.configure(new RestTemplateBuilder());
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(BASE_URL);

        return restTemplateBuilder.uriTemplateHandler(factory);
    }

}
