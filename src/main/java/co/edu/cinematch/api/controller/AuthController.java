package co.edu.cinematch.api.controller;

import co.edu.cinematch.core.CineMatchBackend;
import co.edu.cinematch.core.exception.AuthenticationException;
import co.edu.cinematch.core.exception.ResourceNotFoundException;
import co.edu.cinematch.core.model.Rol;
import co.edu.cinematch.core.model.Usuario;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final CineMatchBackend backend;
    public AuthController(CineMatchBackend backend) { this.backend = backend; }

    @PostMapping("/api/clientes/registro") @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> registrar(@RequestBody Map<String, String> datos) {
        Usuario usuario = backend.auth.registrar(new Usuario(datos.get("nombre"), correo(datos), clave(datos), datos.get("telefono"), Rol.CLIENTE));
        return sesion(usuario);
    }
    @PostMapping({"/api/clientes/login", "/api/auth/login"})
    public Map<String, String> login(@RequestBody Map<String, String> datos) { return sesion(backend.auth.iniciarSesion(correo(datos), clave(datos))); }
    @PutMapping({"/api/clientes/perfil", "/api/auth/perfil"})
    public Map<String, String> perfil(@RequestBody Map<String, String> datos) {
        Usuario usuario = buscarCorreo(correo(datos));
        if (!usuario.getPassword().equals(datos.get("contrasenaActual"))) throw new AuthenticationException("La contraseña actual no es correcta.");
        String nueva = datos.get("contrasenaNueva");
        return sesion(backend.auth.actualizarPerfil(usuario.getId(), datos.get("nombre"), datos.get("telefono"), nueva == null || nueva.isBlank() ? usuario.getPassword() : nueva));
    }
    private Usuario buscarCorreo(String correo) { return backend.auth.listar().stream().filter(u -> u.getEmail().equalsIgnoreCase(correo)).findFirst().orElseThrow(() -> new ResourceNotFoundException("Usuario", correo)); }
    private String correo(Map<String, String> datos) { return datos.getOrDefault("correo", datos.get("email")); }
    private String clave(Map<String, String> datos) { return datos.getOrDefault("contrasena", datos.get("password")); }
    private Map<String, String> sesion(Usuario u) { return Map.of("token", UUID.randomUUID().toString(), "nombre", u.getNombre(), "correo", u.getEmail(), "telefono", u.getTelefono(), "rol", u.getRol().name()); }
}

