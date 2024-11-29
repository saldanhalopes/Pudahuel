package cl.euro.pudahuel.columnas.util;

import cl.euro.pudahuel.columnas.domain.Usuario;
import cl.euro.pudahuel.columnas.repos.SistemaRepository;
import cl.euro.pudahuel.columnas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
public class HomeController {

    @Autowired
    private SistemaRepository sistemaRepository;

    @Autowired
    private final UsuarioService usuarioService;

    public HomeController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping({ "/", "/index"})
    public String goHome(final Model model, Principal principal) {
       Usuario usuario = usuarioService.findByUsername(principal.getName());
        model.addAttribute("user", usuario);
        if(usuario.getChangePass()){
            return "redirect:/alterarSenha/" + usuario.getSecret();
        }else{
            return "dashboard";
        }
    }

    @GetMapping("/parameters")
    public String parameters() {
        return "parameters/index";
    }

//    @GetMapping("/colunas")
//    public String logs() {
//        return "colunas/index";
//    }


}
