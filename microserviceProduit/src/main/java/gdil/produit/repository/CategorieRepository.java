package gdil.produit.repository;

import gdil.produit.domain.Categorie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Categorie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {}
