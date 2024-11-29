package cl.euro.pudahuel.columnas.service;

import cl.euro.pudahuel.columnas.domain.ColunaConfig;
import cl.euro.pudahuel.columnas.enums.ColunaConfigParametro;
import cl.euro.pudahuel.columnas.model.ColunaConfigDTO;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.repos.ColunaConfigRepository;
import cl.euro.pudahuel.columnas.repos.ColunaRepository;
import cl.euro.pudahuel.columnas.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ColunaConfigService {

    private final ColunaConfigRepository colunaConfigRepository;
    private final ColunaRepository colunaRepository;

    public ColunaConfig findById(Integer id){
        return colunaConfigRepository.findById(id).orElse(null);
    }

    public ColunaConfigService(final ColunaConfigRepository colunaConfigRepository,
            final ColunaRepository colunaRepository) {
        this.colunaConfigRepository = colunaConfigRepository;
        this.colunaRepository = colunaRepository;
    }

    public SimplePage<ColunaConfigDTO> findAll(final String filter, final Pageable pageable) {
        Page<ColunaConfig> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = colunaConfigRepository.findAllById(integerFilter, pageable);
        } else {
            page = colunaConfigRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(colunaConfig -> mapToDTO(colunaConfig, new ColunaConfigDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public ColunaConfigDTO get(final Integer id) {
        return colunaConfigRepository.findById(id)
                .map(colunaConfig -> mapToDTO(colunaConfig, new ColunaConfigDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ColunaConfigDTO colunaConfigDTO) {
        final ColunaConfig colunaConfig = new ColunaConfig();
        mapToEntity(colunaConfigDTO, colunaConfig);
        return colunaConfigRepository.save(colunaConfig).getId();
    }

    public void update(final Integer id, final ColunaConfigDTO colunaConfigDTO) {
        final ColunaConfig colunaConfig = colunaConfigRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(colunaConfigDTO, colunaConfig);
        colunaConfigRepository.save(colunaConfig);
    }

    public void delete(final Integer id) {
        colunaConfigRepository.deleteById(id);
    }

    private ColunaConfigDTO mapToDTO(final ColunaConfig colunaConfig,
            final ColunaConfigDTO colunaConfigDTO) {
        colunaConfigDTO.setId(colunaConfig.getId());
        colunaConfigDTO.setParametro(colunaConfig.getParametro().name());
        colunaConfigDTO.setConfiguracao(colunaConfig.getConfiguracao());
        colunaConfigDTO.setDescricao(colunaConfig.getDescricao());
        colunaConfigDTO.setVersion(colunaConfig.getVersion());
        return colunaConfigDTO;
    }

    private ColunaConfig mapToEntity(final ColunaConfigDTO colunaConfigDTO,
            final ColunaConfig colunaConfig) {
        colunaConfig.setParametro(ColunaConfigParametro.valueOf(colunaConfigDTO.getParametro()));
        colunaConfig.setConfiguracao(colunaConfigDTO.getConfiguracao());
        colunaConfig.setDescricao(colunaConfigDTO.getDescricao());
        return colunaConfig;
    }

    public String getReferencedWarning(final Integer id) {
        final ColunaConfig colunaConfig = colunaConfigRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        return null;
    }

}
