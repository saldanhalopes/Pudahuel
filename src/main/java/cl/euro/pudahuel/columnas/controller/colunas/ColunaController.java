package cl.euro.pudahuel.columnas.controller.colunas;

import cl.euro.pudahuel.columnas.config.EntityRevision;
import cl.euro.pudahuel.columnas.config.GenericRevisionRepository;
import cl.euro.pudahuel.columnas.domain.Coluna;
import cl.euro.pudahuel.columnas.domain.ColunaConfig;
import cl.euro.pudahuel.columnas.enums.ColunaConfigParametro;
import cl.euro.pudahuel.columnas.model.ColunaDTO;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.repos.ColunaConfigRepository;
import cl.euro.pudahuel.columnas.service.ColunaService;
import cl.euro.pudahuel.columnas.util.CustomCollectors;
import cl.euro.pudahuel.columnas.util.UserRoles;
import cl.euro.pudahuel.columnas.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/colunas")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class ColunaController {

    private final ColunaService colunaService;
    private final ColunaConfigRepository colunaConfigRepository;

    @Autowired
    private GenericRevisionRepository genericRevisionRepository;

    public ColunaController(final ColunaService colunaService,
            final ColunaConfigRepository colunaConfigRepository) {
        this.colunaService = colunaService;
        this.colunaConfigRepository = colunaConfigRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("tipoColunaValues", colunaConfigRepository.findByPar(ColunaConfigParametro.TIPO)
                .stream()
                .collect(CustomCollectors.toSortedMap(ColunaConfig::getId, ColunaConfig::getConfiguracao)));
        model.addAttribute("fabricanteColunaValues", colunaConfigRepository.findByPar(ColunaConfigParametro.FABRICANTE)
                .stream()
                .collect(CustomCollectors.toSortedMap(ColunaConfig::getId, ColunaConfig::getConfiguracao)));
        model.addAttribute("marcaColunaValues", colunaConfigRepository.findByPar(ColunaConfigParametro.MARCA)
                .stream()
                .collect(CustomCollectors.toSortedMap(ColunaConfig::getId, ColunaConfig::getConfiguracao)));
        model.addAttribute("faseColunaValues", colunaConfigRepository.findByPar(ColunaConfigParametro.FASE)
                .stream()
                .collect(CustomCollectors.toSortedMap(ColunaConfig::getId, ColunaConfig::getConfiguracao)));
        model.addAttribute("comprimentoColunaValues", colunaConfigRepository.findByPar(ColunaConfigParametro.COMPRIMENTO)
                .stream()
                .collect(CustomCollectors.toSortedMap(ColunaConfig::getId, ColunaConfig::getConfiguracao)));
        model.addAttribute("diametroColunaValues", colunaConfigRepository.findByPar(ColunaConfigParametro.DIAMETRO)
                .stream()
                .collect(CustomCollectors.toSortedMap(ColunaConfig::getId, ColunaConfig::getConfiguracao)));
        model.addAttribute("particulaColunaValues", colunaConfigRepository.findByPar(ColunaConfigParametro.PARTICULA)
                .stream()
                .collect(CustomCollectors.toSortedMap(ColunaConfig::getId, ColunaConfig::getConfiguracao)));
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
                       @RequestParam(defaultValue = "50") final int size,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "id") String sort,
                       @RequestParam(required = false) String sortDir,
                       final Model model) {
        Pageable pag = PageRequest.of(page, size, WebUtils.getSortDirection(sortDir), sort);
        model.addAttribute("filter", filter);
        model.addAttribute("size", size);
        model.addAttribute("page", page);
        model.addAttribute("sort", sort);
        model.addAttribute("sortDir", sortDir);
        final SimplePage<ColunaDTO> estoques = colunaService.findAll(filter, pag);
        model.addAttribute("colunas", estoques);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(estoques));
        return "coluna/list";
    }


    @GetMapping("/add")
    public String add(@ModelAttribute("coluna") final ColunaDTO colunaDTO) {
        return "coluna/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("coluna") @Valid final ColunaDTO colunaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "coluna/add";
        }
        colunaService.create(colunaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("coluna.create.success"));
        return "redirect:/colunas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("coluna", colunaService.get(id));
        return "coluna/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("coluna") @Valid final ColunaDTO colunaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "coluna/edit";
        }
        colunaService.update(id, colunaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("coluna.update.success"));
        return "redirect:/colunas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = colunaService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            colunaService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("coluna.delete.success"));
        }
        return "redirect:/colunas";
    }

    @RequestMapping("/audit")
    public String getRevisions(Model model) {
        List<EntityRevision<Coluna>> revisoes = genericRevisionRepository.listaRevisoes(Coluna.class);
        model.addAttribute("audits", revisoes);
        return "/coluna/audit";
    }

    @RequestMapping("/audit/{id}")
    public String getRevisions(Model model, @PathVariable final Integer id) {
        Coluna coluna = colunaService.findById(id);
        List<EntityRevision<Coluna>> revisoes = genericRevisionRepository.listaRevisoesById(coluna.getId(), Coluna.class);
        model.addAttribute("audits", revisoes);
        return "/coluna/audit";
    }

}
