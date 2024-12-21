package gdil.commande.client;

import gdil.commande.service.dto.UserDTO;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gateway", path = "/api")
public interface UserFeignClient {
    @GetMapping("/users/{id}")
    Optional<UserDTO> getUserById(@PathVariable("id") Long id);
}
