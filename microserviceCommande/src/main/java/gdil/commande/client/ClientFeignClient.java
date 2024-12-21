package gdil.commande.client;

import gdil.commande.service.dto.ClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gateway", path = "/api")
public interface ClientFeignClient {
    @GetMapping("/clients/{id}")
    ClientDTO getClientById(@PathVariable("id") Long id);
}
