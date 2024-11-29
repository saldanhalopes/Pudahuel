package cl.euro.pudahuel.columnas.service;

import cl.euro.pudahuel.columnas.domain.Storage;
import cl.euro.pudahuel.columnas.domain.StorageTipo;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.model.StorageTipoDTO;
import cl.euro.pudahuel.columnas.repos.StorageRepository;
import cl.euro.pudahuel.columnas.repos.StorageTipoRepository;
import cl.euro.pudahuel.columnas.util.NotFoundException;
import cl.euro.pudahuel.columnas.util.WebUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StorageTipoService {

    private final StorageTipoRepository storageTipoRepository;
    private final StorageRepository storageRepository;

    public StorageTipo findById(Integer id){
        return storageTipoRepository.findById(id).orElse(null);
    }

    public SimplePage<StorageTipoDTO> findAll(final String filter, final Pageable pageable) {
        Page<StorageTipo> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = storageTipoRepository.findAllById(integerFilter, pageable);
        } else {
            page = storageTipoRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(storageTipo -> mapToDTO(storageTipo, new StorageTipoDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public StorageTipoDTO get(final Integer id) {
        return storageTipoRepository.findById(id)
                .map(storageTipo -> mapToDTO(storageTipo, new StorageTipoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final StorageTipoDTO storageTipoDTO) {
        final StorageTipo storageTipo = new StorageTipo();
        mapToEntity(storageTipoDTO, storageTipo);
        return storageTipoRepository.save(storageTipo).getId();
    }

    public void update(final Integer id, final StorageTipoDTO storageTipoDTO) {
        final StorageTipo storageTipo = storageTipoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(storageTipoDTO, storageTipo);
        storageTipoRepository.save(storageTipo);
    }

    public void delete(final Integer id) {
        storageTipoRepository.deleteById(id);
    }

    private StorageTipoDTO mapToDTO(final StorageTipo storageTipo,
                                    final StorageTipoDTO storageTipoDTO) {
        storageTipoDTO.setId(storageTipo.getId());
        storageTipoDTO.setTipo(storageTipo.getTipo());
        storageTipoDTO.setCondicoesArmazenamento(storageTipo.getCondicoesArmazenamento());
        storageTipoDTO.setVersion(storageTipo.getVersion());
        return storageTipoDTO;
    }

    private StorageTipo mapToEntity(final StorageTipoDTO storageTipoDTO,
                                    final StorageTipo storageTipo) {
        storageTipo.setTipo(storageTipoDTO.getTipo());
        storageTipo.setCondicoesArmazenamento(storageTipoDTO.getCondicoesArmazenamento());
        return storageTipo;
    }

    public String getReferencedWarning(final Integer id) {
        final StorageTipo storageTipo = storageTipoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Storage tipoStorage = storageRepository.findFirstByTipo(storageTipo);
        if (tipoStorage != null) {
            return WebUtils.getMessage("entity.referenced", tipoStorage.getId());
        }
        return null;
    }

}
