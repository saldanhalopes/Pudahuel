package cl.euro.pudahuel.columnas.service;

import cl.euro.pudahuel.columnas.domain.Grandeza;
import cl.euro.pudahuel.columnas.domain.UnidadeMedida;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.model.UnidadeMedidaDTO;
import cl.euro.pudahuel.columnas.repos.GrandezaRepository;
import cl.euro.pudahuel.columnas.repos.UnidadeMedidaRepository;
import cl.euro.pudahuel.columnas.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UnidadeMedidaService {

    private final UnidadeMedidaRepository unidadeMedidaRepository;
    private final GrandezaRepository grandezaRepository;


    public UnidadeMedida findById(Integer id){
        return unidadeMedidaRepository.findById(id).orElse(null);
    }

    public SimplePage<UnidadeMedidaDTO> findAll(final String filter, final Pageable pageable) {
        Page<UnidadeMedida> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = unidadeMedidaRepository.findAllById(integerFilter, pageable);
        } else {
            page = unidadeMedidaRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(unidadeMedida -> mapToDTO(unidadeMedida, new UnidadeMedidaDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public UnidadeMedidaDTO get(final Integer id) {
        return unidadeMedidaRepository.findById(id)
                .map(unidadeMedida -> mapToDTO(unidadeMedida, new UnidadeMedidaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final UnidadeMedidaDTO unidadeMedidaDTO) {
        final UnidadeMedida unidadeMedida = new UnidadeMedida();
        mapToEntity(unidadeMedidaDTO, unidadeMedida);
        return unidadeMedidaRepository.save(unidadeMedida).getId();
    }

    public void update(final Integer id, final UnidadeMedidaDTO unidadeMedidaDTO) {
        final UnidadeMedida unidadeMedida = unidadeMedidaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(unidadeMedidaDTO, unidadeMedida);
        unidadeMedidaRepository.save(unidadeMedida);
    }

    public void delete(final Integer id) {
        unidadeMedidaRepository.deleteById(id);
    }

    private UnidadeMedidaDTO mapToDTO(final UnidadeMedida unidadeMedida,
            final UnidadeMedidaDTO unidadeMedidaDTO) {
        unidadeMedidaDTO.setId(unidadeMedida.getId());
        unidadeMedidaDTO.setUnidade(unidadeMedida.getUnidade());
        unidadeMedidaDTO.setGrandeza(unidadeMedida.getGrandeza() == null ? null : unidadeMedida.getGrandeza().getId());
        unidadeMedidaDTO.setVersion(unidadeMedida.getVersion());
        return unidadeMedidaDTO;
    }

    private UnidadeMedida mapToEntity(final UnidadeMedidaDTO unidadeMedidaDTO,
            final UnidadeMedida unidadeMedida) {
        unidadeMedida.setUnidade(unidadeMedidaDTO.getUnidade());
        final Grandeza grandeza = unidadeMedidaDTO.getGrandeza() == null ? null : grandezaRepository.findById(unidadeMedidaDTO.getGrandeza())
                .orElseThrow(() -> new NotFoundException("grandeza not found"));
        unidadeMedida.setGrandeza(grandeza);
        return unidadeMedida;
    }

    public String getReferencedWarning(final Integer id) {
        final UnidadeMedida unidadeMedida = unidadeMedidaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        return null;
    }

}
