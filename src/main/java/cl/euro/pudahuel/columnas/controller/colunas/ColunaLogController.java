package cl.euro.pudahuel.columnas.controller.colunas;

import cl.euro.pudahuel.columnas.config.EntityRevision;
import cl.euro.pudahuel.columnas.config.GenericRevisionRepository;
import cl.euro.pudahuel.columnas.domain.Arquivos;
import cl.euro.pudahuel.columnas.domain.ColunaLog;
import cl.euro.pudahuel.columnas.domain.ColunaUtil;
import cl.euro.pudahuel.columnas.domain.Usuario;
import cl.euro.pudahuel.columnas.model.ColunaLogDTO;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.repos.ArquivosRepository;
import cl.euro.pudahuel.columnas.repos.ColunaUtilRepository;
import cl.euro.pudahuel.columnas.repos.UsuarioRepository;
import cl.euro.pudahuel.columnas.service.ColunaLogService;
import cl.euro.pudahuel.columnas.util.CustomCollectors;
import cl.euro.pudahuel.columnas.util.UserRoles;
import cl.euro.pudahuel.columnas.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/colunaLogs")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class ColunaLogController {

    private final ColunaLogService colunaLogService;
    private final ColunaUtilRepository colunaUtilRepository;
    private final UsuarioRepository usuarioRepository;
    private final ArquivosRepository arquivosRepository;

    @Autowired
    private GenericRevisionRepository genericRevisionRepository;

    public ColunaLogController(final ColunaLogService colunaLogService,
            final ColunaUtilRepository colunaUtilRepository,
            final UsuarioRepository usuarioRepository,
            final ArquivosRepository arquivosRepository) {
        this.colunaLogService = colunaLogService;
        this.colunaUtilRepository = colunaUtilRepository;
        this.usuarioRepository = usuarioRepository;
        this.arquivosRepository = arquivosRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("colunaUtilValues", colunaUtilRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(ColunaUtil::getId, ColunaUtil::getCodigoColuna)));
        model.addAttribute("usuarioInicioValues", usuarioRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Usuario::getId, Usuario::getCep)));
        model.addAttribute("usuarioFimValues", usuarioRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Usuario::getId, Usuario::getCep)));
        model.addAttribute("anexoValues", arquivosRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Arquivos::getId, Arquivos::getNome)));
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<ColunaLogDTO> colunaLogs = colunaLogService.findAll(filter, pageable);
        model.addAttribute("colunaLogs", colunaLogs);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(colunaLogs));
        return "colunaLog/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("colunaLog") final ColunaLogDTO colunaLogDTO) {
        return "colunaLog/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("colunaLog") @Valid final ColunaLogDTO colunaLogDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "colunaLog/add";
        }
        colunaLogService.create(colunaLogDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("colunaLog.create.success"));
        return "redirect:/colunaLogs";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("colunaLog", colunaLogService.get(id));
        return "colunaLog/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("colunaLog") @Valid final ColunaLogDTO colunaLogDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "colunaLog/edit";
        }
        colunaLogService.update(id, colunaLogDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("colunaLog.update.success"));
        return "redirect:/colunaLogs";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        colunaLogService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("colunaLog.delete.success"));
        return "redirect:/colunaLogs";
    }
    @RequestMapping("/audit")
    public String getRevisions(Model model) {
        List<EntityRevision<ColunaLog>> revisoes = genericRevisionRepository.listaRevisoes(ColunaLog.class);
        model.addAttribute("audits", revisoes);
        return "/colunaLog/audit";
    }

    @RequestMapping("/audit/{id}")
    public String getRevisions(Model model, @PathVariable final Integer id) {
        ColunaLog colunaLog = colunaLogService.findById(id);
        List<EntityRevision<ColunaLog>> revisoes = genericRevisionRepository.listaRevisoesById(colunaLog.getId(), ColunaLog.class);
        model.addAttribute("audits", revisoes);
        return "/colunaLog/audit";
    }

}
