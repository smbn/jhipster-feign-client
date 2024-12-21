package gdil.commande.service.impl;

import gdil.commande.client.ClientFeignClient;
import gdil.commande.client.ProduitFeignClient;
import gdil.commande.domain.Commande;
import gdil.commande.repository.CommandeRepository;
import gdil.commande.service.CommandeService;
import gdil.commande.service.dto.ClientDTO;
import gdil.commande.service.dto.CommandeDTO;
import gdil.commande.service.dto.CommandeWithDetailsDTO;
import gdil.commande.service.dto.ProduitDTO;
import gdil.commande.service.mapper.CommandeMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link gdil.commande.domain.Commande}.
 */
@Service
@Transactional
public class CommandeServiceImpl implements CommandeService {

    private static final Logger LOG = LoggerFactory.getLogger(CommandeServiceImpl.class);

    private final CommandeRepository commandeRepository;

    private final CommandeMapper commandeMapper;

    private final ClientFeignClient clientFeignClient;

    private final ProduitFeignClient produitFeignClient;

    public CommandeServiceImpl(
        CommandeRepository commandeRepository,
        CommandeMapper commandeMapper,
        ClientFeignClient clientFeignClient,
        ProduitFeignClient produitFeignClient
    ) {
        this.commandeRepository = commandeRepository;
        this.commandeMapper = commandeMapper;
        this.clientFeignClient = clientFeignClient;
        this.produitFeignClient = produitFeignClient;
    }

    @Override
    public CommandeDTO save(CommandeDTO commandeDTO) {
        LOG.debug("Request to save Commande : {}", commandeDTO);
        // Vérifier si le client existe
        ClientDTO client = clientFeignClient.getClientById(commandeDTO.getClientId());
        if (client == null) {
            throw new IllegalArgumentException("Client non trouvé avec l'ID : " + commandeDTO.getClientId());
        }
        // Vérifier si le produit existe
        ProduitDTO produit = produitFeignClient.getProduitById(commandeDTO.getProduitId());
        if (produit == null) {
            throw new IllegalArgumentException("Produit non trouvé avec l'ID : " + commandeDTO.getProduitId());
        }
        Commande commande = commandeMapper.toEntity(commandeDTO);
        commande = commandeRepository.save(commande);
        return commandeMapper.toDto(commande);
    }

    @Override
    public CommandeDTO update(CommandeDTO commandeDTO) {
        LOG.debug("Request to update Commande : {}", commandeDTO);
        Commande commande = commandeMapper.toEntity(commandeDTO);
        commande = commandeRepository.save(commande);
        return commandeMapper.toDto(commande);
    }

    @Override
    public Optional<CommandeDTO> partialUpdate(CommandeDTO commandeDTO) {
        LOG.debug("Request to partially update Commande : {}", commandeDTO);

        return commandeRepository
            .findById(commandeDTO.getId())
            .map(existingCommande -> {
                commandeMapper.partialUpdate(existingCommande, commandeDTO);

                return existingCommande;
            })
            .map(commandeRepository::save)
            .map(commandeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommandeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Commandes");
        return commandeRepository.findAll(pageable).map(commandeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommandeDTO> findOne(Long id) {
        LOG.debug("Request to get Commande : {}", id);
        return commandeRepository.findById(id).map(commandeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Commande : {}", id);
        commandeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeWithDetailsDTO> findAllWithDetails() {
        return commandeRepository
            .findAll()
            .stream()
            .map(commande -> {
                ClientDTO client = clientFeignClient.getClientById(commande.getClientId());
                ProduitDTO produit = produitFeignClient.getProduitById(commande.getProduitId());
                CommandeWithDetailsDTO detailsDTO = new CommandeWithDetailsDTO();
                detailsDTO.setId(commande.getId());
                detailsDTO.setDateCommande(commande.getDateCommande());
                detailsDTO.setClient(client);
                detailsDTO.setProduit(produit);
                return detailsDTO;
            })
            .collect(Collectors.toList());
    }
}
