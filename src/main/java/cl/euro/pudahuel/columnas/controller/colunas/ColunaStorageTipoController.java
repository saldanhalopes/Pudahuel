package cl.euro.pudahuel.columnas.controller.colunas;

import cl.euro.pudahuel.columnas.config.EntityRevision;
import cl.euro.pudahuel.columnas.config.GenericRevisionRepository;
import cl.euro.pudahuel.columnas.domain.ColunaStorageTipo;
import cl.euro.pudahuel.columnas.model.ColunaStorageTipoDTO;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.service.ColunaStorageTipoService;
import cl.euro.pudahuel.columnas.util.UserRoles;
import cl.euro.pudahuel.columnas.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/colunaStorageTipos")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class ColunaStorageTipoController {

    private final ColunaStorageTipoService colunaStorageTipoService;

    public ColunaStorageTipoController(final ColunaStorageTipoService colunaStorageTipoService) {
        this.colunaStorageTipoService = colunaStorageTipoService;
    }

    @Autowired
    private GenericRevisionRepository genericRevisionRepository;

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<ColunaStorageTipoDTO> colunaStorageTipos = colunaStorageTipoService.findAll(filter, pageable);
        model.addAttribute("colunaStorageTipos", colunaStorageTipos);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(colunaStorageTipos));
        return "colunaStorageTipo/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("colunaStorageTipo") final ColunaStorageTipoDTO colunaStorageTipoDTO) {
        return "colunaStorageTipo/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("colunaStorageTipo") @Valid final ColunaStorageTipoDTO colunaStorageTipoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "colunaStorageTipo/add";
        }
        colunaStorageTipoService.create(colunaStorageTipoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("colunaStorageTipo.create.success"));
        return "redirect:/colunaStorageTipos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("colunaStorageTipo", colunaStorageTipoService.get(id));
        return "colunaStorageTipo/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("colunaStorageTipo") @Valid final ColunaStorageTipoDTO colunaStorageTipoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "colunaStorageTipo/edit";
        }
        colunaStorageTipoService.update(id, colunaStorageTipoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("colunaStorageTipo.update.success"));
        return "redirect:/colunaStorageTipos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = colunaStorageTipoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            colunaStorageTipoService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("colunaStorageTipo.delete.success"));
        }
        return "redirect:/colunaStorageTipos";
    }

    @RequestMapping("/audit")
    public String getRevisions(Model model) {
        List<EntityRevision<ColunaStorageTipo>> revisoes = genericRevisionRepository.listaRevisoes(ColunaStorageTipo.class);
        model.addAttribute("audits", revisoes);
        return "/colunaStorageTipo/audit";
    }

    @RequestMapping("/audit/{id}")
    public String getRevisions(Model model, @PathVariable final Integer id) {
        ColunaStorageTipo colunaStorageTipo = colunaStorageTipoService.findById(id);
        List<EntityRevision<ColunaStorageTipo>> revisoes = genericRevisionRepository.listaRevisoesById(colunaStorageTipo.getId(), ColunaStorageTipo.class);
        model.addAttribute("audits", revisoes);
        return "/colunaStorageTipo/audit";
    }

}
