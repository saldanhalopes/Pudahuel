package cl.euro.pudahuel.columnas.service;

import cl.euro.pudahuel.columnas.domain.Setor;
import cl.euro.pudahuel.columnas.domain.Storage;
import cl.euro.pudahuel.columnas.domain.StorageEndereco;
import cl.euro.pudahuel.columnas.domain.StorageTipo;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.model.StorageDTO;
import cl.euro.pudahuel.columnas.repos.SetorRepository;
import cl.euro.pudahuel.columnas.repos.StorageEnderecoRepository;
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
public class StorageService {

    private final StorageRepository storageRepository;
    private final SetorRepository setorRepository;
    private final StorageTipoRepository storageTipoRepository;
    private final StorageEnderecoRepository storageEnderecoRepository;

    public Storage findById(Integer id){
        return storageRepository.findById(id).orElse(null);
    }

    public SimplePage<StorageDTO> findAllOfStorages( final Pageable pageable) {
        Page<Storage> page = storageRepository.findAllOfStorages(pageable);
        return new SimplePage<>(page.getContent()
                .stream()
                .map(colunaStorage -> mapToDTO(colunaStorage, new StorageDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public StorageDTO get(final Integer id) {
        return storageRepository.findStorageById(id)
                .map(colunaStorage -> mapToDTO(colunaStorage, new StorageDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final StorageDTO storageDTO) {
        final Storage storage = new Storage();
        mapToEntity(storageDTO, storage);
        return storageRepository.save(storage).getId();
    }

    public void update(final Integer id, final StorageDTO storageDTO) {
        final Storage storage = storageRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(storageDTO, storage);
        storageRepository.save(storage);
    }

    public void delete(final Integer id) {
        storageRepository.deleteById(id);
    }

    private StorageDTO mapToDTO(final Storage storage,
                                final StorageDTO storageDTO) {
        storageDTO.setId(storage.getId());
        storageDTO.setStorage(storage.getStorage());
        storageDTO.setObs(storage.getObs());
        storageDTO.setSetor(storage.getSetor() == null ? null : storage.getSetor().getId());
        storageDTO.setSetorNome(storage.getSetor().getSetor() == null ? null : storage.getSetor().getSetor());
        storageDTO.setTipo(storage.getTipo() == null ? null : storage.getTipo().getId());
        storageDTO.setTipoNome(storage.getTipo().getTipo() == null ? null : storage.getTipo().getTipo());
        storageDTO.setVersion(storage.getVersion());
        return storageDTO;
    }

    private Storage mapToEntity(final StorageDTO storageDTO,
                                final Storage storage) {
        storage.setStorage(storageDTO.getStorage());
        storage.setObs(storageDTO.getObs());
        final Setor setor = storageDTO.getSetor() == null ? null : setorRepository.findById(storageDTO.getSetor())
                .orElseThrow(() -> new NotFoundException("setor not found"));
        storage.setSetor(setor);
        final StorageTipo tipo = storageDTO.getTipo() == null ? null : storageTipoRepository.findById(storageDTO.getTipo())
                .orElseThrow(() -> new NotFoundException("tipo not found"));
        storage.setTipo(tipo);
        return storage;
    }

    public String getReferencedWarning(final Integer id) {
        final Storage storage = storageRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final StorageEndereco colunaStorageStorageEndereco = storageEnderecoRepository.findFirstByStorage(storage);
        if (colunaStorageStorageEndereco != null) {
            return WebUtils.getMessage("entity.referenced", colunaStorageStorageEndereco.getId());
        }
        return null;
    }

}
