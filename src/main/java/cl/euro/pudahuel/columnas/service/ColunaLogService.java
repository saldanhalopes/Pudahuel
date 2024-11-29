package cl.euro.pudahuel.columnas.service;

import cl.euro.pudahuel.columnas.domain.Arquivos;
import cl.euro.pudahuel.columnas.domain.ColunaLog;
import cl.euro.pudahuel.columnas.domain.ColunaUtil;
import cl.euro.pudahuel.columnas.domain.Usuario;
import cl.euro.pudahuel.columnas.model.ColunaLogDTO;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.repos.ArquivosRepository;
import cl.euro.pudahuel.columnas.repos.ColunaLogRepository;
import cl.euro.pudahuel.columnas.repos.ColunaUtilRepository;
import cl.euro.pudahuel.columnas.repos.UsuarioRepository;
import cl.euro.pudahuel.columnas.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ColunaLogService {

    private final ColunaLogRepository colunaLogRepository;
    private final ColunaUtilRepository colunaUtilRepository;
    private final UsuarioRepository usuarioRepository;
    private final ArquivosRepository arquivosRepository;

    public ColunaLog findById(Integer id){
        return colunaLogRepository.findById(id).orElse(null);
    }

    public ColunaLogService(final ColunaLogRepository colunaLogRepository,
            final ColunaUtilRepository colunaUtilRepository,
            final UsuarioRepository usuarioRepository,
            final ArquivosRepository arquivosRepository) {
        this.colunaLogRepository = colunaLogRepository;
        this.colunaUtilRepository = colunaUtilRepository;
        this.usuarioRepository = usuarioRepository;
        this.arquivosRepository = arquivosRepository;
    }

    public SimplePage<ColunaLogDTO> findAll(final String filter, final Pageable pageable) {
        Page<ColunaLog> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = colunaLogRepository.findAllById(integerFilter, pageable);
        } else {
            page = colunaLogRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(colunaLog -> mapToDTO(colunaLog, new ColunaLogDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public ColunaLogDTO get(final Integer id) {
        return colunaLogRepository.findById(id)
                .map(colunaLog -> mapToDTO(colunaLog, new ColunaLogDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ColunaLogDTO colunaLogDTO) {
        final ColunaLog colunaLog = new ColunaLog();
        mapToEntity(colunaLogDTO, colunaLog);
        return colunaLogRepository.save(colunaLog).getId();
    }

    public void update(final Integer id, final ColunaLogDTO colunaLogDTO) {
        final ColunaLog colunaLog = colunaLogRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(colunaLogDTO, colunaLog);
        colunaLogRepository.save(colunaLog);
    }

    public void delete(final Integer id) {
        colunaLogRepository.deleteById(id);
    }

    private ColunaLogDTO mapToDTO(final ColunaLog colunaLog, final ColunaLogDTO colunaLogDTO) {
        colunaLogDTO.setId(colunaLog.getId());
        colunaLogDTO.setDataIncio(colunaLog.getDataIncio());
        colunaLogDTO.setDataFim(colunaLog.getDataFim());
        colunaLogDTO.setSentido(colunaLog.getSentido());
        colunaLogDTO.setPrecoluna(colunaLog.getPrecoluna());
        colunaLogDTO.setPrefiltro(colunaLog.getPrefiltro());
        colunaLogDTO.setInjecoes(colunaLog.getInjecoes());
        colunaLogDTO.setColunaUtil(colunaLog.getColunaUtil() == null ? null : colunaLog.getColunaUtil().getId());
        colunaLogDTO.setUsuarioInicio(colunaLog.getUsuarioInicio() == null ? null : colunaLog.getUsuarioInicio().getId());
        colunaLogDTO.setUsuarioFim(colunaLog.getUsuarioFim() == null ? null : colunaLog.getUsuarioFim().getId());
        colunaLogDTO.setAnexo(colunaLog.getAnexo() == null ? null : colunaLog.getAnexo().getId());
        colunaLogDTO.setVersion(colunaLog.getVersion());
        return colunaLogDTO;
    }

    private ColunaLog mapToEntity(final ColunaLogDTO colunaLogDTO, final ColunaLog colunaLog) {
        colunaLog.setDataIncio(colunaLogDTO.getDataIncio());
        colunaLog.setDataFim(colunaLogDTO.getDataFim());
        colunaLog.setSentido(colunaLogDTO.getSentido());
        colunaLog.setPrecoluna(colunaLogDTO.getPrecoluna());
        colunaLog.setPrefiltro(colunaLogDTO.getPrefiltro());
        colunaLog.setInjecoes(colunaLogDTO.getInjecoes());
        final ColunaUtil colunaUtil = colunaLogDTO.getColunaUtil() == null ? null : colunaUtilRepository.findById(colunaLogDTO.getColunaUtil())
                .orElseThrow(() -> new NotFoundException("colunaUtil not found"));
        colunaLog.setColunaUtil(colunaUtil);
        final Usuario usuarioInicio = colunaLogDTO.getUsuarioInicio() == null ? null : usuarioRepository.findById(colunaLogDTO.getUsuarioInicio())
                .orElseThrow(() -> new NotFoundException("usuarioInicio not found"));
        colunaLog.setUsuarioInicio(usuarioInicio);
        final Usuario usuarioFim = colunaLogDTO.getUsuarioFim() == null ? null : usuarioRepository.findById(colunaLogDTO.getUsuarioFim())
                .orElseThrow(() -> new NotFoundException("usuarioFim not found"));
        colunaLog.setUsuarioFim(usuarioFim);
        final Arquivos anexo = colunaLogDTO.getAnexo() == null ? null : arquivosRepository.findById(colunaLogDTO.getAnexo())
                .orElseThrow(() -> new NotFoundException("anexo not found"));
        colunaLog.setAnexo(anexo);
        return colunaLog;
    }

}
