package cl.euro.pudahuel.columnas.service;

import cl.euro.pudahuel.columnas.domain.Coluna;
import cl.euro.pudahuel.columnas.domain.ColunaConfig;
import cl.euro.pudahuel.columnas.domain.ColunaUtil;
import cl.euro.pudahuel.columnas.model.ColunaDTO;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.repos.ColunaConfigRepository;
import cl.euro.pudahuel.columnas.repos.ColunaRepository;
import cl.euro.pudahuel.columnas.repos.ColunaUtilRepository;
import cl.euro.pudahuel.columnas.util.NotFoundException;
import cl.euro.pudahuel.columnas.util.WebUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ColunaService {

    private final ColunaRepository colunaRepository;
    private final ColunaConfigRepository colunaConfigRepository;
    private final ColunaUtilRepository colunaUtilRepository;

    public Coluna findById(Integer id){
        return colunaRepository.findById(id).orElse(null);
    }

    public ColunaService(final ColunaRepository colunaRepository,
            final ColunaConfigRepository colunaConfigRepository,
            final ColunaUtilRepository colunaUtilRepository) {
        this.colunaRepository = colunaRepository;
        this.colunaConfigRepository = colunaConfigRepository;
        this.colunaUtilRepository = colunaUtilRepository;

    }

    public SimplePage<ColunaDTO> findAll(final String filter, final Pageable pageable) {
        Page<Coluna> page;
        if (filter != null) {
            page = colunaRepository.findAllByKeyword(filter, pageable);
        } else {
            page = colunaRepository.findAllOfColuna(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(coluna -> mapToDTO(coluna, new ColunaDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public ColunaDTO get(final Integer id) {
        return colunaRepository.findById(id)
                .map(coluna -> mapToDTO(coluna, new ColunaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ColunaDTO colunaDTO) {
        final Coluna coluna = new Coluna();
        mapToEntity(colunaDTO, coluna);
        return colunaRepository.save(coluna).getId();
    }

    public void update(final Integer id, final ColunaDTO colunaDTO) {
        final Coluna coluna = colunaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(colunaDTO, coluna);
        colunaRepository.save(coluna);
    }

    public void delete(final Integer id) {
        colunaRepository.deleteById(id);
    }

    private ColunaDTO mapToDTO(final Coluna coluna, final ColunaDTO colunaDTO) {
        colunaDTO.setId(coluna.getId());
        colunaDTO.setCodigo(coluna.getCodigo());
        colunaDTO.setPartNumber(coluna.getPartNumber());
        colunaDTO.setObs(coluna.getObs());
        colunaDTO.setTipoColuna(coluna.getTipoColuna() == null ? null : coluna.getTipoColuna().getId());
        colunaDTO.setTipoColunaName(coluna.getTipoColuna() == null ? null : coluna.getTipoColuna().getConfiguracao());
        colunaDTO.setFabricanteColuna(coluna.getFabricanteColuna() == null ? null : coluna.getFabricanteColuna().getId());
        colunaDTO.setFabricanteColunaName(coluna.getFabricanteColuna() == null ? null : coluna.getFabricanteColuna().getConfiguracao());
        colunaDTO.setMarcaColuna(coluna.getMarcaColuna() == null ? null : coluna.getMarcaColuna().getId());
        colunaDTO.setMarcaColunaName(coluna.getMarcaColuna() == null ? null : coluna.getMarcaColuna().getConfiguracao());
        colunaDTO.setFaseColuna(coluna.getFaseColuna() == null ? null : coluna.getFaseColuna().getId());
        colunaDTO.setFaseColunaName(coluna.getFaseColuna() == null ? null : coluna.getFaseColuna().getConfiguracao());
        colunaDTO.setComprimentoColuna(coluna.getComprimentoColuna() == null ? null : coluna.getComprimentoColuna().getId());
        colunaDTO.setComprimentoColunaName(coluna.getComprimentoColuna() == null ? null : coluna.getComprimentoColuna().getConfiguracao());
        colunaDTO.setDiametroColuna(coluna.getDiametroColuna() == null ? null : coluna.getDiametroColuna().getId());
        colunaDTO.setDiametroColunaName(coluna.getDiametroColuna() == null ? null : coluna.getDiametroColuna().getConfiguracao());
        colunaDTO.setParticulaColuna(coluna.getParticulaColuna() == null ? null : coluna.getParticulaColuna().getId());
        colunaDTO.setParticulaColunaName(coluna.getParticulaColuna() == null ? null : coluna.getParticulaColuna().getConfiguracao());
        colunaDTO.setVersion(coluna.getVersion());
        return colunaDTO;
    }

    private Coluna mapToEntity(final ColunaDTO colunaDTO, final Coluna coluna) {
        coluna.setCodigo(colunaDTO.getCodigo());
        coluna.setPartNumber(colunaDTO.getPartNumber());
        coluna.setObs(colunaDTO.getObs());
        final ColunaConfig tipoColuna = colunaDTO.getTipoColuna() == null ? null : colunaConfigRepository.findById(colunaDTO.getTipoColuna())
                .orElseThrow(() -> new NotFoundException("tipoColuna not found"));
        coluna.setTipoColuna(tipoColuna);
        final ColunaConfig fabricanteColuna = colunaDTO.getFabricanteColuna() == null ? null : colunaConfigRepository.findById(colunaDTO.getFabricanteColuna())
                .orElseThrow(() -> new NotFoundException("fabricanteColuna not found"));
        coluna.setFabricanteColuna(fabricanteColuna);
        final ColunaConfig marcaColuna = colunaDTO.getMarcaColuna() == null ? null : colunaConfigRepository.findById(colunaDTO.getMarcaColuna())
                .orElseThrow(() -> new NotFoundException("marcaColuna not found"));
        coluna.setMarcaColuna(marcaColuna);
        final ColunaConfig faseColuna = colunaDTO.getFaseColuna() == null ? null : colunaConfigRepository.findById(colunaDTO.getFaseColuna())
                .orElseThrow(() -> new NotFoundException("faseColuna not found"));
        coluna.setFaseColuna(faseColuna);
        final ColunaConfig comprimentoColuna = colunaDTO.getComprimentoColuna() == null ? null : colunaConfigRepository.findById(colunaDTO.getComprimentoColuna())
                .orElseThrow(() -> new NotFoundException("comprimentoColuna not found"));
        coluna.setComprimentoColuna(comprimentoColuna);
        final ColunaConfig diametroColuna = colunaDTO.getDiametroColuna() == null ? null : colunaConfigRepository.findById(colunaDTO.getDiametroColuna())
                .orElseThrow(() -> new NotFoundException("diametroColuna not found"));
        coluna.setDiametroColuna(diametroColuna);
        final ColunaConfig particulaColuna = colunaDTO.getParticulaColuna() == null ? null : colunaConfigRepository.findById(colunaDTO.getParticulaColuna())
                .orElseThrow(() -> new NotFoundException("particulaColuna not found"));
        coluna.setParticulaColuna(particulaColuna);
        return coluna;
    }

    public String getReferencedWarning(final Integer id) {
        final Coluna coluna = colunaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final ColunaUtil colunaColunaUtil = colunaUtilRepository.findFirstByColuna(coluna);
        if (colunaColunaUtil != null) {
            return WebUtils.getMessage("coluna.colunaUtil.coluna.referenced", colunaColunaUtil.getId());
        }
        return null;
    }

}
