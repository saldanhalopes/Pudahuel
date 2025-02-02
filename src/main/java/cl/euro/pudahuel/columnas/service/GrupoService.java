package cl.euro.pudahuel.columnas.service;

import cl.euro.pudahuel.columnas.domain.Grupo;
import cl.euro.pudahuel.columnas.domain.Usuario;
import cl.euro.pudahuel.columnas.model.GrupoDTO;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.repos.GrupoRepository;
import cl.euro.pudahuel.columnas.repos.UsuarioRepository;
import cl.euro.pudahuel.columnas.util.NotFoundException;
import cl.euro.pudahuel.columnas.util.WebUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;

    public Grupo findById(Integer id){
        return grupoRepository.findById(id).orElse(null);
    }

    public SimplePage<GrupoDTO> findAll(final String filter, final Pageable pageable) {
        Page<Grupo> page;
        if (filter != null) {
            Integer integerFilter = null;
            try {
                integerFilter = Integer.parseInt(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = grupoRepository.findAllById(integerFilter, pageable);
        } else {
            page = grupoRepository.findAll(pageable);
        }
        return new SimplePage<>(page.getContent()
                .stream()
                .map(grupo -> mapToDTO(grupo, new GrupoDTO()))
                .toList(),
                page.getTotalElements(), pageable);
    }

    public GrupoDTO get(final Integer id) {
        return grupoRepository.findById(id)
                .map(grupo -> mapToDTO(grupo, new GrupoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final GrupoDTO grupoDTO) {
        final Grupo grupo = new Grupo();
        mapToEntity(grupoDTO, grupo);
        return grupoRepository.save(grupo).getId();
    }

    public void update(final Integer id, final GrupoDTO grupoDTO) {
        final Grupo grupo = grupoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(grupoDTO, grupo);
        grupoRepository.save(grupo);
    }

    public void delete(final Integer id) {
        final Grupo grupo = grupoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        grupoRepository.delete(grupo);
    }

    private GrupoDTO mapToDTO(final Grupo grupo, final GrupoDTO grupoDTO) {
        grupoDTO.setId(grupo.getId());
        grupoDTO.setGrupo(grupo.getGrupo());
        grupoDTO.setTipo(grupo.getTipo());
        grupoDTO.setRegra(grupo.getRegra());
        grupoDTO.setVersion(grupo.getVersion());
        return grupoDTO;
    }

    private Grupo mapToEntity(final GrupoDTO grupoDTO, final Grupo grupo) {
        grupo.setGrupo(grupoDTO.getGrupo());
        grupo.setTipo(grupoDTO.getTipo());
        grupo.setRegra(grupoDTO.getRegra());
        return grupo;
    }

    public String getReferencedWarning(final Integer id) {
        final Grupo grupo = grupoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Usuario gruposUsuario = usuarioRepository.findFirstByGrupo(grupo);
        if (gruposUsuario != null) {
            return WebUtils.getMessage("grupo.usuario.grupos.referenced", gruposUsuario.getId());
        }
        return null;
    }

}
