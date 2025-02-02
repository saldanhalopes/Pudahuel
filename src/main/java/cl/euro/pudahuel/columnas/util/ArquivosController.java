package cl.euro.pudahuel.columnas.util;

import cl.euro.pudahuel.columnas.config.EntityRevision;
import cl.euro.pudahuel.columnas.config.GenericRevisionRepository;
import cl.euro.pudahuel.columnas.domain.Arquivos;
import cl.euro.pudahuel.columnas.domain.CustomRevisionEntity;
import cl.euro.pudahuel.columnas.model.ArquivosDTO;
import cl.euro.pudahuel.columnas.model.SimplePage;
import cl.euro.pudahuel.columnas.service.ArquivosService;
import cl.euro.pudahuel.columnas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/arquivoss")
public class ArquivosController {

    private final ArquivosService arquivosService;

    @Autowired
    private GenericRevisionRepository genericRevisionRepository;

    public ArquivosController(final ArquivosService arquivosService) {
        this.arquivosService = arquivosService;
    }

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String list(@RequestParam(required = false) final String filter,
                       @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
                       final Model model) {
        final SimplePage<ArquivosDTO> arquivoss = arquivosService.findAll(filter, pageable);
        model.addAttribute("arquivoss", arquivoss);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(arquivoss));
        return "pages/arquivos/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("arquivos") final ArquivosDTO arquivosDTO) {
        return "pages/arquivos/add";
    }

    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "', '" + UserRoles.MASTERUSER + "', '" + UserRoles.POWERUSER + "')")
    @PostMapping("/add")
    public String add(@ModelAttribute("arquivos") @Valid final ArquivosDTO arquivosDTO,  final BindingResult bindingResult,
                      final Model model, final RedirectAttributes redirectAttributes,
                      Principal principal, @ModelAttribute("password") String pass) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("bindingResult.hasErrors"));
            return "pages/arquivos/add";
        } else {
            if (usuarioService.validarUser(principal.getName(), pass)) {
                CustomRevisionEntity.setMotivoText("Novo Registro");
                arquivosService.create(arquivosDTO);
                redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("arquivos.create.success"));
            } else {
                model.addAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("authentication.error"));
                return "pages/arquivos/add";
            }
        }
        return "redirect:/arquivoss";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("arquivos", arquivosService.get(id));
        return "pages/arquivos/edit";
    }

    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "', '" + UserRoles.MASTERUSER + "')")
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
                       @ModelAttribute("arquivos") @Valid final ArquivosDTO arquivosDTO,
                       final BindingResult bindingResult, final Model model,
                       final RedirectAttributes redirectAttributes, @ModelAttribute("motivo") String motivo,
                       Principal principal, @ModelAttribute("password") String pass) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("bindingResult.hasErrors"));
            return "pages/arquivos/edit";
        } else {
            if (usuarioService.validarUser(principal.getName(), pass)) {
                CustomRevisionEntity.setMotivoText(motivo);
                arquivosService.update(id, arquivosDTO);
                redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("arquivos.update.success"));
            } else {
                model.addAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("authentication.error"));
                return "pages/arquivos/edit";
            }
        }
        return "redirect:/arquivoss";
    }

    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "')")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
                         final RedirectAttributes redirectAttributes,
                         @ModelAttribute("motivo") String motivo,
                         Principal principal,
                         @ModelAttribute("password") String pass) {
            if (usuarioService.validarUser(principal.getName(), pass)) {
                CustomRevisionEntity.setMotivoText(motivo);
                arquivosService.delete(id);
                redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("arquivos.delete.success"));
            } else {
                redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("authentication.error"));
            }
        return "redirect:/arquivoss";
    }

    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "', '" + UserRoles.MASTERUSER + "', '" + UserRoles.POWERUSER + "')")
    @GetMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<?> showArquivo(@PathVariable("id") Integer id) throws IOException {
        ArquivosDTO arquivosDTO = arquivosService.get(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(arquivosDTO.getTipo()))
                .contentLength(arquivosDTO.getTamanho())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + arquivosDTO.getNome())
                .body(arquivosDTO.getArquivo());
    }

    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "')")
    @RequestMapping("/audit")
    public String getRevisions(Model model) {
        List<EntityRevision<Arquivos>> revisoes = genericRevisionRepository.listaRevisoes(Arquivos.class);
        model.addAttribute("audits", revisoes);
        return "pages/arquivos/audit";
    }

    @PreAuthorize("hasAnyAuthority('" + UserRoles.ADMIN + "')")
    @RequestMapping("/audit/{id}")
    public String getRevisions(Model model, @PathVariable final Integer id) {
        Arquivos arquivos = arquivosService.findById(id);
        List<EntityRevision<Arquivos>> revisoes = genericRevisionRepository.listaRevisoesById(arquivos.getId(), Arquivos.class);
        model.addAttribute("audits", revisoes);
        return "pages/arquivos/audit";
    }
}
