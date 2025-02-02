package cl.euro.pudahuel.columnas.service;

import cl.euro.pudahuel.columnas.domain.Usuario;
import cl.euro.pudahuel.columnas.repos.UsuarioRepository;
import com.amdelamar.jotp.OTP;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(final Usuario usuario) {
        usuario.setEmail(usuario.getEmail().toLowerCase());
        usuario.setNome(usuario.getNome().toUpperCase());
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setSobrenome(usuario.getSobrenome().toUpperCase());
        usuario.setUsername(usuario.getUsername().toLowerCase());
        usuario.setSecret(OTP.randomBase32(20));
        usuario.setGrupo(usuario.getGrupo());
        usuario.setChangePass(true);
        usuarioRepository.save(usuario);
    }




}
