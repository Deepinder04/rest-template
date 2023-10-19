package guru.springframework.spring6resttemplate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static guru.springframework.spring6resttemplate.Utils.Constants.BASE_URL;

@Configuration
public class RestTemplateBuilderConfiguration {

    @Value("${rest.template.rootUrl:http://localhost:8080}")
    String baseURL;

    @Value("${rest.template.username}")
    String username;

    @Value("${rest.template.password}")
    String password;

    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer){

        return configurer.configure(new RestTemplateBuilder())
                .basicAuthentication(username,password)
                .uriTemplateHandler(new DefaultUriBuilderFactory(baseURL));
    }

}
