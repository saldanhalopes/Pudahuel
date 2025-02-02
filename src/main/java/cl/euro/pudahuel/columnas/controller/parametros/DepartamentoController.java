package cl.euro.pudahuel.columnas.controller.parametros;

import cl.euro.pudahuel.columnas.config.EntityRevision;
import cl.euro.pudahuel.columnas.config.GenericRevisionRepository;
import cl.euro.pudahuel.columnas.domain.CustomRevisionEntity;
import cl.euro.pudahuel.columnas.domain.Departamento;
import cl.euro.pudahuel.columnas.model.DepartamentoDTO;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.service.DepartamentoService;
import cl.euro.pudahuel.columnas.service.UsuarioService;
import cl.euro.pudahuel.columnas.util.UserRoles;
import cl.euro.pudahuel.columnas.util.WebUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    @Autowired
    private GenericRevisionRepository genericRevisionRepository;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
                       @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
                       final Model model) {
        final SimplePage<DepartamentoDTO> departamentos = departamentoService.findAll(filter, pageable);
        model.addAttribute("departamentos", departamentos);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(departamentos));
        return "parameters/departamento/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("departamento") final DepartamentoDTO departamentoDTO) {
        return "parameters/departamento/add";
    }

    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "', '" + UserRoles.MASTERUSER + "', '" + UserRoles.POWERUSER + "')")
    @PostMapping("/add")
    public String add(@ModelAttribute("departamento") @Valid final DepartamentoDTO departamentoDTO,  final BindingResult bindingResult,
                      final Model model, final RedirectAttributes redirectAttributes,
                      Principal principal, @ModelAttribute("password") String pass) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("bindingResult.hasErrors"));
            return "parameters/departamento/add";
        } else {
            if (usuarioService.validarUser(principal.getName(), pass)) {
                CustomRevisionEntity.setMotivoText("Novo Registro");
                departamentoService.create(departamentoDTO);
                redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("departamento.create.success"));
            } else {
                model.addAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("authentication.error"));
                return "parameters/departamento/add";
            }
        }
        return "redirect:/departamentos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("departamento", departamentoService.get(id));
        return "parameters/departamento/edit";
    }

    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "', '" + UserRoles.MASTERUSER + "')")
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
                       @ModelAttribute("departamento") @Valid final DepartamentoDTO departamentoDTO,
                       final BindingResult bindingResult, final Model model,
                       final RedirectAttributes redirectAttributes, @ModelAttribute("motivo") String motivo,
                       Principal principal, @ModelAttribute("password") String pass) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("bindingResult.hasErrors"));
            return "parameters/departamento/edit";
        } else {
            if (usuarioService.validarUser(principal.getName(), pass)) {
                CustomRevisionEntity.setMotivoText(motivo);
                departamentoService.update(id, departamentoDTO);
                redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("departamento.update.success"));
            } else {
                model.addAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("authentication.error"));
                return "parameters/departamento/edit";
            }
        }
        return "redirect:/departamentos";
    }

    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "')")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
                         final RedirectAttributes redirectAttributes,
                         @ModelAttribute("motivo") String motivo,
                         Principal principal,
                         @ModelAttribute("password") String pass) {
        final String referencedWarning = departamentoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            if (usuarioService.validarUser(principal.getName(), pass)) {
                CustomRevisionEntity.setMotivoText(motivo);
                departamentoService.delete(id);
                redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("departamento.delete.success"));
            } else {
                redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("authentication.error"));
            }
        }
        return "redirect:/departamentos";
    }

    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "')")
    @RequestMapping("/audit")
    public String getRevisions(Model model) {
        List<EntityRevision<Departamento>> revisoes = genericRevisionRepository.listaRevisoes(Departamento.class);
        model.addAttribute("audits", revisoes);
        return "parameters/departamento/audit";
    }

    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "')")
    @RequestMapping("/audit/{id}")
    public String getRevisions(Model model, @PathVariable final Integer id) {
        Departamento departamento = departamentoService.findById(id);
        List<EntityRevision<Departamento>> revisoes = genericRevisionRepository.listaRevisoesById(departamento.getId(), Departamento.class);
        model.addAttribute("audits", revisoes);
        return "parameters/departamento/audit";
    }
}
