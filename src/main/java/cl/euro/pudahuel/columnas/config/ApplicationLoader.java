package cl.euro.pudahuel.columnas.config;

import cl.euro.pudahuel.columnas.domain.Grupo;
import cl.euro.pudahuel.columnas.domain.Seguranca;
import cl.euro.pudahuel.columnas.domain.Sistema;
import cl.euro.pudahuel.columnas.domain.Usuario;
import cl.euro.pudahuel.columnas.enums.SegurancaTipo;
import cl.euro.pudahuel.columnas.repos.GrupoRepository;
import cl.euro.pudahuel.columnas.repos.SegurancaRepository;
import cl.euro.pudahuel.columnas.repos.SistemaRepository;
import cl.euro.pudahuel.columnas.service.RegistrationService;
import cl.euro.pudahuel.columnas.util.UserRoles;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class ApplicationLoader implements ApplicationRunner {

    private final GrupoRepository grupoRepository;

    private final SistemaRepository sistemaRepository;

    private final SegurancaRepository segurancaRepository;

    private final RegistrationService registrationService;

    public ApplicationLoader(final GrupoRepository grupoRepository,
                             final SistemaRepository sistemaRepository,
                             SegurancaRepository segurancaRepository,
                             final RegistrationService registrationService) {
        this.grupoRepository = grupoRepository;
        this.sistemaRepository = sistemaRepository;
        this.segurancaRepository = segurancaRepository;
        this.registrationService = registrationService;
    }


    @Override
    public void run(final ApplicationArguments args) throws IOException {
        if (sistemaRepository.count() == 0) {
            final Sistema sistema = new Sistema();
            sistema.setSistemaNome("Lablims");
            sistema.setSistemaCriador("Rafael Saldanha Lopes");
            sistema.setBuilderVersao(1.0);
            sistema.setDetalhes("Sistema de Gerenciamento de Laboraório");
            sistemaRepository.save(sistema);
            final Seguranca seguranca = new Seguranca();
            seguranca.setSegurancaTipo(SegurancaTipo.user2faCode);
            seguranca.setValue("false");
            segurancaRepository.save(seguranca);
        }
        if (grupoRepository.count() == 0) {
            final Grupo adminGrupo = new Grupo();
            adminGrupo.setRegra(UserRoles.ADMIN);
            adminGrupo.setGrupo("ADMIN");
            adminGrupo.setTipo("ADMINISTRADOR");
            grupoRepository.save(adminGrupo);
            final Usuario usuario = new Usuario();
            usuario.setEmail("admin@admin.com");
            usuario.setNome("Administrador");
            usuario.setSobrenome("Lablims");
            usuario.setUsername("admin");
            usuario.setPassword("admin");
            usuario.setAtivo(true);
            usuario.setToken(true);
            usuario.setGrupo(adminGrupo);
            registrationService.register(usuario);
        }

    }

}
