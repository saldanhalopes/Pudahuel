package cl.euro.pudahuel.columnas.controller.colunas;

import cl.euro.pudahuel.columnas.config.EntityRevision;
import cl.euro.pudahuel.columnas.config.GenericRevisionRepository;
import cl.euro.pudahuel.columnas.domain.ColunaStorage;
import cl.euro.pudahuel.columnas.domain.ColunaVaga;
import cl.euro.pudahuel.columnas.model.ColunaVagaDTO;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.repos.ColunaStorageRepository;
import cl.euro.pudahuel.columnas.service.ColunaVagaService;
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
@RequestMapping("/colunaVagas")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class ColunaVagaController {

    private final ColunaVagaService colunaVagaService;
    private final ColunaStorageRepository colunaStorageRepository;

    @Autowired
    private GenericRevisionRepository genericRevisionRepository;

    public ColunaVagaController(final ColunaVagaService colunaVagaService,
            final ColunaStorageRepository colunaStorageRepository) {
        this.colunaVagaService = colunaVagaService;
        this.colunaStorageRepository = colunaStorageRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("colunaStorageValues", colunaStorageRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(ColunaStorage::getId, ColunaStorage::getCodigo)));
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<ColunaVagaDTO> colunaVagas = colunaVagaService.findAll(filter, pageable);
        model.addAttribute("colunaVagas", colunaVagas);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(colunaVagas));
        return "colunaVaga/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("colunaVaga") final ColunaVagaDTO colunaVagaDTO) {
        return "colunaVaga/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("colunaVaga") @Valid final ColunaVagaDTO colunaVagaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "colunaVaga/add";
        }
        colunaVagaService.create(colunaVagaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("colunaVaga.create.success"));
        return "redirect:/colunaVagas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("colunaVaga", colunaVagaService.get(id));
        return "colunaVaga/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("colunaVaga") @Valid final ColunaVagaDTO colunaVagaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "colunaVaga/edit";
        }
        colunaVagaService.update(id, colunaVagaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("colunaVaga.update.success"));
        return "redirect:/colunaVagas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = colunaVagaService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            colunaVagaService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("colunaVaga.delete.success"));
        }
        return "redirect:/colunaVagas";
    }

    @RequestMapping("/audit")
    public String getRevisions(Model model) {
        List<EntityRevision<ColunaVaga>> revisoes = genericRevisionRepository.listaRevisoes(ColunaVaga.class);
        model.addAttribute("audits", revisoes);
        return "/colunaVaga/audit";
    }

    @RequestMapping("/audit/{id}")
    public String getRevisions(Model model, @PathVariable final Integer id) {
        ColunaVaga colunaVaga = colunaVagaService.findById(id);
        List<EntityRevision<ColunaVaga>> revisoes = genericRevisionRepository.listaRevisoesById(colunaVaga.getId(), ColunaVaga.class);
        model.addAttribute("audits", revisoes);
        return "/ColunaVaga/audit";
    }

}
