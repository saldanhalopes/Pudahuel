package cl.euro.pudahuel.columnas.service;

import cl.euro.pudahuel.columnas.domain.Departamento;
import cl.euro.pudahuel.columnas.domain.Setor;
import cl.euro.pudahuel.columnas.model.DepartamentoDTO;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.repos.DepartamentoRepository;
import cl.euro.pudahuel.columnas.repos.SetorRepository;
import cl.euro.pudahuel.columnas.util.NotFoundException;
import cl.euro.pudahuel.columnas.util.WebUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final SetorRepository setorRepository;

    public Departamento findById(Integer id){
        return departamentoRepository.findById(id).orElse(null);
    }
    public SimplePage<DepartamentoDTO> findAll(final String filter, final Pageable pageable) {
        Page<Departamento> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = departamentoRepository.findAllById(integerFilter, pageable);
        } else {
            page = departamentoRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(departamento -> mapToDTO(departamento, new DepartamentoDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public DepartamentoDTO get(final Integer id) {
        return departamentoRepository.findById(id)
                .map(departamento -> mapToDTO(departamento, new DepartamentoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final DepartamentoDTO departamentoDTO) {
        final Departamento departamento = new Departamento();
        mapToEntity(departamentoDTO, departamento);
        return departamentoRepository.save(departamento).getId();
    }

    public void update(final Integer id, final DepartamentoDTO departamentoDTO) {
        final Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(departamentoDTO, departamento);
        departamentoRepository.save(departamento);
    }

    public void delete(final Integer id) {
        departamentoRepository.deleteById(id);
    }

    private DepartamentoDTO mapToDTO(final Departamento departamento,
            final DepartamentoDTO departamentoDTO) {
        departamentoDTO.setId(departamento.getId());
        departamentoDTO.setDepartamento(departamento.getDepartamento());
        departamentoDTO.setSiglaDepartamento(departamento.getSiglaDepartamento());
        departamentoDTO.setDescricao(departamento.getDescricao());
        departamentoDTO.setVersion(departamento.getVersion());
        return departamentoDTO;
    }

    private Departamento mapToEntity(final DepartamentoDTO departamentoDTO,
            final Departamento departamento) {
        departamento.setDepartamento(departamentoDTO.getDepartamento());
        departamento.setSiglaDepartamento(departamentoDTO.getSiglaDepartamento());
        departamento.setDescricao(departamentoDTO.getDescricao());
        return departamento;
    }

    public String getReferencedWarning(final Integer id) {
        final Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Setor departamentoSetor = setorRepository.findFirstByDepartamento(departamento);
        if (departamentoSetor != null) {
            return WebUtils.getMessage("entity.referenced", departamentoSetor.getId());
        }
        return null;
    }

}
