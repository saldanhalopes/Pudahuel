package cl.euro.pudahuel.columnas.service;

import cl.euro.pudahuel.columnas.domain.CategoriaMetodologia;
import cl.euro.pudahuel.columnas.domain.Metodologia;
import cl.euro.pudahuel.columnas.model.MetodologiaDTO;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.repos.CategoriaMetodologiaRepository;
import cl.euro.pudahuel.columnas.repos.MetodologiaRepository;
import cl.euro.pudahuel.columnas.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class MetodologiaService {

    private final MetodologiaRepository metodologiaRepository;
    private final CategoriaMetodologiaRepository categoriaMetodologiaRepository;


    public Metodologia findById(Integer id){
        return metodologiaRepository.findById(id).orElse(null);
    }

    public MetodologiaService(final MetodologiaRepository metodologiaRepository,
            final CategoriaMetodologiaRepository categoriaMetodologiaRepository) {
        this.metodologiaRepository = metodologiaRepository;
        this.categoriaMetodologiaRepository = categoriaMetodologiaRepository;
    }

    public SimplePage<MetodologiaDTO> findAll(final String filter, final Pageable pageable) {
        Page<Metodologia> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = metodologiaRepository.findAllById(integerFilter, pageable);
        } else {
            page = metodologiaRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(metodologia -> mapToDTO(metodologia, new MetodologiaDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public MetodologiaDTO get(final Integer id) {
        return metodologiaRepository.findById(id)
                .map(metodologia -> mapToDTO(metodologia, new MetodologiaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final MetodologiaDTO metodologiaDTO) {
        final Metodologia metodologia = new Metodologia();
        mapToEntity(metodologiaDTO, metodologia);
        return metodologiaRepository.save(metodologia).getId();
    }

    public void update(final Integer id, final MetodologiaDTO metodologiaDTO) {
        final Metodologia metodologia = metodologiaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(metodologiaDTO, metodologia);
        metodologiaRepository.save(metodologia);
    }

    public void delete(final Integer id) {
        metodologiaRepository.deleteById(id);
    }

    private MetodologiaDTO mapToDTO(final Metodologia metodologia,
            final MetodologiaDTO metodologiaDTO) {
        metodologiaDTO.setId(metodologia.getId());
        metodologiaDTO.setCodigo(metodologia.getCodigo());
        metodologiaDTO.setMetodo(metodologia.getMetodo());
        metodologiaDTO.setObs(metodologia.getObs());
        metodologiaDTO.setCategoriaMetodologia(metodologia.getCategoriaMetodologia() == null ? null : metodologia.getCategoriaMetodologia().getId());
        metodologiaDTO.setVersion(metodologia.getVersion());
        return metodologiaDTO;
    }

    private Metodologia mapToEntity(final MetodologiaDTO metodologiaDTO,
            final Metodologia metodologia) {
        metodologia.setCodigo(metodologiaDTO.getCodigo());
        metodologia.setMetodo(metodologiaDTO.getMetodo());
        metodologia.setObs(metodologiaDTO.getObs());
        final CategoriaMetodologia categoriaMetodologia = metodologiaDTO.getCategoriaMetodologia() == null ? null : categoriaMetodologiaRepository.findById(metodologiaDTO.getCategoriaMetodologia())
                .orElseThrow(() -> new NotFoundException("categoriaMetodologia not found"));
        metodologia.setCategoriaMetodologia(categoriaMetodologia);
        return metodologia;
    }

    public String getReferencedWarning(final Integer id) {
        final Metodologia metodologia = metodologiaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        return null;
    }

}
