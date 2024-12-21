package gdil.produit.service.mapper;

import static gdil.produit.domain.ProduitAsserts.*;
import static gdil.produit.domain.ProduitTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProduitMapperTest {

    private ProduitMapper produitMapper;

    @BeforeEach
    void setUp() {
        produitMapper = new ProduitMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProduitSample1();
        var actual = produitMapper.toEntity(produitMapper.toDto(expected));
        assertProduitAllPropertiesEquals(expected, actual);
    }
}
