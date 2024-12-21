package gdil.produit.service.mapper;

import gdil.produit.domain.Categorie;
import gdil.produit.domain.Produit;
import gdil.produit.service.dto.CategorieDTO;
import gdil.produit.service.dto.ProduitDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Produit} and its DTO {@link ProduitDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProduitMapper extends EntityMapper<ProduitDTO, Produit> {
    @Mapping(target = "categorie", source = "categorie", qualifiedByName = "categorieId")
    ProduitDTO toDto(Produit s);

    @Named("categorieId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomCategorie", source = "nomCategorie")
    CategorieDTO toDtoCategorieId(Categorie categorie);
}
