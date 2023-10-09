package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class BeerClientImpl implements BeerClient {
    @Override
    public Page<BeerDTO> listBeers() {
        return null;
    }
}
