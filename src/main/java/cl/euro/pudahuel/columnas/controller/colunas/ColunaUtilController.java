package cl.euro.pudahuel.columnas.controller.colunas;

import cl.euro.pudahuel.columnas.config.EntityRevision;
import cl.euro.pudahuel.columnas.config.GenericRevisionRepository;
import cl.euro.pudahuel.columnas.domain.*;
import cl.euro.pudahuel.columnas.model.ColunaUtilDTO;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.repos.*;
import cl.euro.pudahuel.columnas.service.ColunaUtilService;
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
@RequestMapping("/colunaUtils")
@PreAuthorize("hasAuthority('" + UserRoles.ADMIN + "')")
public class ColunaUtilController {

    private final ColunaUtilService colunaUtilService;
    private final ColunaRepository colunaRepository;
    private final SetorRepository setorRepository;
    private final MetodologiaRepository metodologiaRepository;
    private final ColunaVagaRepository colunaVagaRepository;
    private final ArquivosRepository arquivosRepository;

    @Autowired
    private GenericRevisionRepository genericRevisionRepository;

    public ColunaUtilController(final ColunaUtilService colunaUtilService,
            final ColunaRepository colunaRepository, final SetorRepository setorRepository,
            final MetodologiaRepository metodologiaRepository,
            final ColunaVagaRepository colunaVagaRepository,
            final ArquivosRepository arquivosRepository) {
        this.colunaUtilService = colunaUtilService;
        this.colunaRepository = colunaRepository;
        this.setorRepository = setorRepository;
        this.metodologiaRepository = metodologiaRepository;
        this.colunaVagaRepository = colunaVagaRepository;
        this.arquivosRepository = arquivosRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("colunaValues", colunaRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Coluna::getId, Coluna::getCodigo)));
        model.addAttribute("setorValues", setorRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Setor::getId, Setor::getSetor)));
        model.addAttribute("metodologiaValues", metodologiaRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Metodologia::getId, Metodologia::getObs)));
        model.addAttribute("colunaVagaValues", colunaVagaRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(ColunaVaga::getId, ColunaVaga::getObs)));
        model.addAttribute("certificadoValues", arquivosRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Arquivos::getId, Arquivos::getNome)));
        model.addAttribute("anexosValues", arquivosRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Arquivos::getId, Arquivos::getNome)));
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final SimplePage<ColunaUtilDTO> colunaUtils = colunaUtilService.findAll(filter, pageable);
        model.addAttribute("colunaUtils", colunaUtils);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(colunaUtils));
        return "colunaUtil/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("colunaUtil") final ColunaUtilDTO colunaUtilDTO) {
        return "colunaUtil/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("colunaUtil") @Valid final ColunaUtilDTO colunaUtilDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "colunaUtil/add";
        }
        colunaUtilService.create(colunaUtilDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("colunaUtil.create.success"));
        return "redirect:/colunaUtils";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("colunaUtil", colunaUtilService.get(id));
        return "colunaUtil/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("colunaUtil") @Valid final ColunaUtilDTO colunaUtilDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "colunaUtil/edit";
        }
        colunaUtilService.update(id, colunaUtilDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("colunaUtil.update.success"));
        return "redirect:/colunaUtils";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = colunaUtilService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            colunaUtilService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("colunaUtil.delete.success"));
        }
        return "redirect:/colunaUtils";
    }

    @RequestMapping("/audit")
    public String getRevisions(Model model) {
        List<EntityRevision<ColunaUtil>> revisoes = genericRevisionRepository.listaRevisoes(ColunaUtil.class);
        model.addAttribute("audits", revisoes);
        return "/colunaUtil/audit";
    }

    @RequestMapping("/audit/{id}")
    public String getRevisions(Model model, @PathVariable final Integer id) {
        ColunaUtil colunaUtil = colunaUtilService.findById(id);
        List<EntityRevision<ColunaUtil>> revisoes = genericRevisionRepository.listaRevisoesById(colunaUtil.getId(), ColunaUtil.class);
        model.addAttribute("audits", revisoes);
        return "/colunaUtil/audit";
    }

}
