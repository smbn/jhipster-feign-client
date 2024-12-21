package gdil.produit.web.rest;

import static gdil.produit.domain.ProduitAsserts.*;
import static gdil.produit.web.rest.TestUtil.createUpdateProxyForBean;
import static gdil.produit.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import gdil.produit.IntegrationTest;
import gdil.produit.domain.Produit;
import gdil.produit.repository.ProduitRepository;
import gdil.produit.service.dto.ProduitDTO;
import gdil.produit.service.mapper.ProduitMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProduitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProduitResourceIT {

    private static final String DEFAULT_NOM_PRODUIT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PRODUIT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_PRODUIT = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_PRODUIT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRIX_PRODUIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRIX_PRODUIT = new BigDecimal(2);

    private static final String DEFAULT_IMAGE_PRODUIT = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_PRODUIT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/produits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private ProduitMapper produitMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProduitMockMvc;

    private Produit produit;

    private Produit insertedProduit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Produit createEntity() {
        return new Produit()
            .nomProduit(DEFAULT_NOM_PRODUIT)
            .descriptionProduit(DEFAULT_DESCRIPTION_PRODUIT)
            .prixProduit(DEFAULT_PRIX_PRODUIT)
            .imageProduit(DEFAULT_IMAGE_PRODUIT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Produit createUpdatedEntity() {
        return new Produit()
            .nomProduit(UPDATED_NOM_PRODUIT)
            .descriptionProduit(UPDATED_DESCRIPTION_PRODUIT)
            .prixProduit(UPDATED_PRIX_PRODUIT)
            .imageProduit(UPDATED_IMAGE_PRODUIT);
    }

    @BeforeEach
    public void initTest() {
        produit = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProduit != null) {
            produitRepository.delete(insertedProduit);
            insertedProduit = null;
        }
    }

    @Test
    @Transactional
    void createProduit() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Produit
        ProduitDTO produitDTO = produitMapper.toDto(produit);
        var returnedProduitDTO = om.readValue(
            restProduitMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produitDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProduitDTO.class
        );

        // Validate the Produit in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProduit = produitMapper.toEntity(returnedProduitDTO);
        assertProduitUpdatableFieldsEquals(returnedProduit, getPersistedProduit(returnedProduit));

        insertedProduit = returnedProduit;
    }

    @Test
    @Transactional
    void createProduitWithExistingId() throws Exception {
        // Create the Produit with an existing ID
        produit.setId(1L);
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProduitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomProduitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        produit.setNomProduit(null);

        // Create the Produit, which fails.
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        restProduitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produitDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrixProduitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        produit.setPrixProduit(null);

        // Create the Produit, which fails.
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        restProduitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produitDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProduits() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList
        restProduitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produit.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomProduit").value(hasItem(DEFAULT_NOM_PRODUIT)))
            .andExpect(jsonPath("$.[*].descriptionProduit").value(hasItem(DEFAULT_DESCRIPTION_PRODUIT)))
            .andExpect(jsonPath("$.[*].prixProduit").value(hasItem(sameNumber(DEFAULT_PRIX_PRODUIT))))
            .andExpect(jsonPath("$.[*].imageProduit").value(hasItem(DEFAULT_IMAGE_PRODUIT)));
    }

    @Test
    @Transactional
    void getProduit() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get the produit
        restProduitMockMvc
            .perform(get(ENTITY_API_URL_ID, produit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(produit.getId().intValue()))
            .andExpect(jsonPath("$.nomProduit").value(DEFAULT_NOM_PRODUIT))
            .andExpect(jsonPath("$.descriptionProduit").value(DEFAULT_DESCRIPTION_PRODUIT))
            .andExpect(jsonPath("$.prixProduit").value(sameNumber(DEFAULT_PRIX_PRODUIT)))
            .andExpect(jsonPath("$.imageProduit").value(DEFAULT_IMAGE_PRODUIT));
    }

    @Test
    @Transactional
    void getNonExistingProduit() throws Exception {
        // Get the produit
        restProduitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduit() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the produit
        Produit updatedProduit = produitRepository.findById(produit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProduit are not directly saved in db
        em.detach(updatedProduit);
        updatedProduit
            .nomProduit(UPDATED_NOM_PRODUIT)
            .descriptionProduit(UPDATED_DESCRIPTION_PRODUIT)
            .prixProduit(UPDATED_PRIX_PRODUIT)
            .imageProduit(UPDATED_IMAGE_PRODUIT);
        ProduitDTO produitDTO = produitMapper.toDto(updatedProduit);

        restProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, produitDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produitDTO))
            )
            .andExpect(status().isOk());

        // Validate the Produit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProduitToMatchAllProperties(updatedProduit);
    }

    @Test
    @Transactional
    void putNonExistingProduit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produit.setId(longCount.incrementAndGet());

        // Create the Produit
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, produitDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produit.setId(longCount.incrementAndGet());

        // Create the Produit
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(produitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produit.setId(longCount.incrementAndGet());

        // Create the Produit
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produitDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Produit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProduitWithPatch() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the produit using partial update
        Produit partialUpdatedProduit = new Produit();
        partialUpdatedProduit.setId(produit.getId());

        partialUpdatedProduit.nomProduit(UPDATED_NOM_PRODUIT).prixProduit(UPDATED_PRIX_PRODUIT).imageProduit(UPDATED_IMAGE_PRODUIT);

        restProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduit))
            )
            .andExpect(status().isOk());

        // Validate the Produit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProduitUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProduit, produit), getPersistedProduit(produit));
    }

    @Test
    @Transactional
    void fullUpdateProduitWithPatch() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the produit using partial update
        Produit partialUpdatedProduit = new Produit();
        partialUpdatedProduit.setId(produit.getId());

        partialUpdatedProduit
            .nomProduit(UPDATED_NOM_PRODUIT)
            .descriptionProduit(UPDATED_DESCRIPTION_PRODUIT)
            .prixProduit(UPDATED_PRIX_PRODUIT)
            .imageProduit(UPDATED_IMAGE_PRODUIT);

        restProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduit))
            )
            .andExpect(status().isOk());

        // Validate the Produit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProduitUpdatableFieldsEquals(partialUpdatedProduit, getPersistedProduit(partialUpdatedProduit));
    }

    @Test
    @Transactional
    void patchNonExistingProduit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produit.setId(longCount.incrementAndGet());

        // Create the Produit
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, produitDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(produitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produit.setId(longCount.incrementAndGet());

        // Create the Produit
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(produitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produit.setId(longCount.incrementAndGet());

        // Create the Produit
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(produitDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Produit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduit() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the produit
        restProduitMockMvc
            .perform(delete(ENTITY_API_URL_ID, produit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return produitRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Produit getPersistedProduit(Produit produit) {
        return produitRepository.findById(produit.getId()).orElseThrow();
    }

    protected void assertPersistedProduitToMatchAllProperties(Produit expectedProduit) {
        assertProduitAllPropertiesEquals(expectedProduit, getPersistedProduit(expectedProduit));
    }

    protected void assertPersistedProduitToMatchUpdatableProperties(Produit expectedProduit) {
        assertProduitAllUpdatablePropertiesEquals(expectedProduit, getPersistedProduit(expectedProduit));
    }
}
