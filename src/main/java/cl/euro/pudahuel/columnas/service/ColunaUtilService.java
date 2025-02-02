package cl.euro.pudahuel.columnas.service;


import cl.euro.pudahuel.columnas.domain.*;
import cl.euro.pudahuel.columnas.model.ColunaUtilDTO;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.repos.*;
import cl.euro.pudahuel.columnas.util.NotFoundException;
import cl.euro.pudahuel.columnas.util.WebUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class ColunaUtilService {

    private final ColunaUtilRepository colunaUtilRepository;
    private final ColunaRepository colunaRepository;
    private final SetorRepository setorRepository;
    private final MetodologiaRepository metodologiaRepository;
    private final ColunaVagaRepository colunaVagaRepository;
    private final ArquivosRepository arquivosRepository;
    private final ColunaLogRepository colunaLogRepository;

    public ColunaUtil findById(Integer id){
        return colunaUtilRepository.findById(id).orElse(null);
    }

    public ColunaUtilService(final ColunaUtilRepository colunaUtilRepository,
            final ColunaRepository colunaRepository, final SetorRepository setorRepository,
            final MetodologiaRepository metodologiaRepository,
            final ColunaVagaRepository colunaVagaRepository,
            final ArquivosRepository arquivosRepository,
            final ColunaLogRepository colunaLogRepository) {
        this.colunaUtilRepository = colunaUtilRepository;
        this.colunaRepository = colunaRepository;
        this.setorRepository = setorRepository;
        this.metodologiaRepository = metodologiaRepository;
        this.colunaVagaRepository = colunaVagaRepository;
        this.arquivosRepository = arquivosRepository;
        this.colunaLogRepository = colunaLogRepository;
    }

    public SimplePage<ColunaUtilDTO> findAll(final String filter, final Pageable pageable) {
        Page<ColunaUtil> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = colunaUtilRepository.findAllById(integerFilter, pageable);
        } else {
            page = colunaUtilRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(colunaUtil -> mapToDTO(colunaUtil, new ColunaUtilDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public ColunaUtilDTO get(final Integer id) {
        return colunaUtilRepository.findById(id)
                .map(colunaUtil -> mapToDTO(colunaUtil, new ColunaUtilDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ColunaUtilDTO colunaUtilDTO) {
        final ColunaUtil colunaUtil = new ColunaUtil();
        mapToEntity(colunaUtilDTO, colunaUtil);
        return colunaUtilRepository.save(colunaUtil).getId();
    }

    public void update(final Integer id, final ColunaUtilDTO colunaUtilDTO) {
        final ColunaUtil colunaUtil = colunaUtilRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(colunaUtilDTO, colunaUtil);
        colunaUtilRepository.save(colunaUtil);
    }

    public void delete(final Integer id) {
        colunaUtilRepository.deleteById(id);
    }

    private ColunaUtilDTO mapToDTO(final ColunaUtil colunaUtil, final ColunaUtilDTO colunaUtilDTO) {
        colunaUtilDTO.setId(colunaUtil.getId());
        colunaUtilDTO.setCodigoColuna(colunaUtil.getCodigoColuna());
        colunaUtilDTO.setSerialNumber(colunaUtil.getSerialNumber());
        colunaUtilDTO.setDataAtivacao(colunaUtil.getDataAtivacao());
        colunaUtilDTO.setDataVerificacao(colunaUtil.getDataVerificacao());
        colunaUtilDTO.setDataDescarte(colunaUtil.getDataDescarte());
        colunaUtilDTO.setEstoque(colunaUtil.getEstoque());
        colunaUtilDTO.setObs(colunaUtil.getObs());
        colunaUtilDTO.setColuna(colunaUtil.getColuna() == null ? null : colunaUtil.getColuna().getId());
        colunaUtilDTO.setSetor(colunaUtil.getSetor() == null ? null : colunaUtil.getSetor().getId());
        colunaUtilDTO.setMetodologia(colunaUtil.getMetodologia() == null ? null : colunaUtil.getMetodologia().getId());
        colunaUtilDTO.setColunaVaga(colunaUtil.getColunaVaga() == null ? null : colunaUtil.getColunaVaga().getId());
        colunaUtilDTO.setCertificado(colunaUtil.getCertificado() == null ? null : colunaUtil.getCertificado().getId());
        colunaUtilDTO.setVersion(colunaUtil.getVersion());
        colunaUtilDTO.setAnexos(colunaUtil.getAnexos().stream()
                .map(arquivos -> arquivos.getId())
                .toList());
        return colunaUtilDTO;
    }

    private ColunaUtil mapToEntity(final ColunaUtilDTO colunaUtilDTO, final ColunaUtil colunaUtil) {
        colunaUtil.setCodigoColuna(colunaUtilDTO.getCodigoColuna());
        colunaUtil.setSerialNumber(colunaUtilDTO.getSerialNumber());
        colunaUtil.setDataAtivacao(colunaUtilDTO.getDataAtivacao());
        colunaUtil.setDataVerificacao(colunaUtilDTO.getDataVerificacao());
        colunaUtil.setDataDescarte(colunaUtilDTO.getDataDescarte());
        colunaUtil.setEstoque(colunaUtilDTO.getEstoque());
        colunaUtil.setObs(colunaUtilDTO.getObs());
        final Coluna coluna = colunaUtilDTO.getColuna() == null ? null : colunaRepository.findById(colunaUtilDTO.getColuna())
                .orElseThrow(() -> new NotFoundException("coluna not found"));
        colunaUtil.setColuna(coluna);
        final Setor setor = colunaUtilDTO.getSetor() == null ? null : setorRepository.findById(colunaUtilDTO.getSetor())
                .orElseThrow(() -> new NotFoundException("setor not found"));
        colunaUtil.setSetor(setor);
        final Metodologia metodologia = colunaUtilDTO.getMetodologia() == null ? null : metodologiaRepository.findById(colunaUtilDTO.getMetodologia())
                .orElseThrow(() -> new NotFoundException("metodologiaVersao not found"));
        colunaUtil.setMetodologia(metodologia);
        final ColunaVaga colunaVaga = colunaUtilDTO.getColunaVaga() == null ? null : colunaVagaRepository.findById(colunaUtilDTO.getColunaVaga())
                .orElseThrow(() -> new NotFoundException("colunaVaga not found"));
        colunaUtil.setColunaVaga(colunaVaga);
        final Arquivos certificado = colunaUtilDTO.getCertificado() == null ? null : arquivosRepository.findById(colunaUtilDTO.getCertificado())
                .orElseThrow(() -> new NotFoundException("certificado not found"));
        colunaUtil.setCertificado(certificado);
        final List<Arquivos> anexos = arquivosRepository.findAllById(
                colunaUtilDTO.getAnexos() == null ? Collections.emptyList() : colunaUtilDTO.getAnexos());
        if (anexos.size() != (colunaUtilDTO.getAnexos() == null ? 0 : colunaUtilDTO.getAnexos().size())) {
            throw new NotFoundException("one of anexos not found");
        }
        colunaUtil.setAnexos(anexos.stream().collect(Collectors.toSet()));
        return colunaUtil;
    }

    public String getReferencedWarning(final Integer id) {
        final ColunaUtil colunaUtil = colunaUtilRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final ColunaLog colunaUtilColunaLog = colunaLogRepository.findFirstByColunaUtil(colunaUtil);
        if (colunaUtilColunaLog != null) {
            return WebUtils.getMessage("colunaUtil.colunaLog.colunaUtil.referenced", colunaUtilColunaLog.getId());
        }
        return null;
    }

}
