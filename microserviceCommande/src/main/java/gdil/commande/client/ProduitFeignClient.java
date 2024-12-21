package gdil.commande.client;

import gdil.commande.service.dto.ProduitDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microserviceProduit", path = "/api")
public interface ProduitFeignClient {
    @GetMapping("/produits/{id}")
    ProduitDTO getProduitById(@PathVariable("id") Long id);
}
