package gdil.gateway.service.impl;

import gdil.gateway.repository.ClientRepository;
import gdil.gateway.service.ClientService;
import gdil.gateway.service.dto.ClientDTO;
import gdil.gateway.service.mapper.ClientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link gdil.gateway.domain.Client}.
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private static final Logger LOG = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public Mono<ClientDTO> save(ClientDTO clientDTO) {
        LOG.debug("Request to save Client : {}", clientDTO);
        return clientRepository.save(clientMapper.toEntity(clientDTO)).map(clientMapper::toDto);
    }

    @Override
    public Mono<ClientDTO> update(ClientDTO clientDTO) {
        LOG.debug("Request to update Client : {}", clientDTO);
        return clientRepository.save(clientMapper.toEntity(clientDTO)).map(clientMapper::toDto);
    }

    @Override
    public Mono<ClientDTO> partialUpdate(ClientDTO clientDTO) {
        LOG.debug("Request to partially update Client : {}", clientDTO);

        return clientRepository
            .findById(clientDTO.getId())
            .map(existingClient -> {
                clientMapper.partialUpdate(existingClient, clientDTO);

                return existingClient;
            })
            .flatMap(clientRepository::save)
            .map(clientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ClientDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Clients");
        return clientRepository.findAllBy(pageable).map(clientMapper::toDto);
    }

    public Mono<Long> countAll() {
        return clientRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ClientDTO> findOne(Long id) {
        LOG.debug("Request to get Client : {}", id);
        return clientRepository.findById(id).map(clientMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Client : {}", id);
        return clientRepository.deleteById(id);
    }
}
