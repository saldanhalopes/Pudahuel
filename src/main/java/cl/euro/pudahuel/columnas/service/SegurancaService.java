package cl.euro.pudahuel.columnas.service;

import cl.euro.pudahuel.columnas.domain.Seguranca;
import cl.euro.pudahuel.columnas.model.SegurancaDTO;
import cl.euro.pudahuel.columnas.repos.SegurancaRepository;
import cl.euro.pudahuel.columnas.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SegurancaService {

    private final SegurancaRepository segurancaRepository;

    public Seguranca findById(Integer id){
        return segurancaRepository.findById(id).orElse(null);
    }

    public SegurancaDTO get(final Integer id) {
        return segurancaRepository.findById(id)
                .map(turno -> mapToDTO(turno, new SegurancaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final SegurancaDTO turnoDTO) {
        final Seguranca turno = new Seguranca();
        mapToEntity(turnoDTO, turno);
        return segurancaRepository.save(turno).getId();
    }

    public void update(final Integer id, final SegurancaDTO turnoDTO) {
        final Seguranca turno = segurancaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(turnoDTO, turno);
        segurancaRepository.save(turno);
    }

    public void delete(final Integer id) {
        segurancaRepository.deleteById(id);
    }

    private SegurancaDTO mapToDTO(final Seguranca seguranca, final SegurancaDTO segurancaDTO) {
        segurancaDTO.setId(seguranca.getId());
        segurancaDTO.setSegurancaTipo(seguranca.getSegurancaTipo());
        segurancaDTO.setValue(seguranca.getValue());
        segurancaDTO.setVersion(seguranca.getVersion());
        return segurancaDTO;
    }

    private Seguranca mapToEntity(final SegurancaDTO segurancaDTO, final Seguranca seguranca) {
        seguranca.setSegurancaTipo(segurancaDTO.getSegurancaTipo());
        seguranca.setValue(segurancaDTO.getValue());
        return seguranca;
    }

}
