package com.example.libreria_api.dto.sistemausuarios;

import com.example.libreria_api.model.sistemausuarios.Usuario;
import com.example.libreria_api.model.sistemausuarios.TipoDeDocumento;
import com.example.libreria_api.model.sistemausuarios.Rol;

public class UsuarioMapper {

    // response
    public static UsuarioResponseDTO toUsuarioResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setUsu_id(usuario.getUsuId());
        dto.setUsuNombre(usuario.getUsuNombre());
        dto.setUsuCorreo(usuario.getUsuCorreo());
        dto.setUsuTelefono(usuario.getUsuTelefono());
        dto.setUsuDocnum(usuario.getUsuDocnum());
        dto.setUsuOrigen(usuario.getUsuOrigen());
        dto.setUsuActivo(usuario.isUsuActivo());
        dto.setTipoDeDocumentoId(
                usuario.getTipoDeDocumento() != null ? usuario.getTipoDeDocumento().getTipdoc_id() : null
        );
        dto.setRolId(
                usuario.getRol() != null ? usuario.getRol().getRol_id() : null
        );
        return dto;
    }

    // request
    public static Usuario toUsuario(UsuarioRequestDTO dto, TipoDeDocumento tipoDeDocumento, Rol rol) {
        Usuario usuario = new Usuario();
        usuario.setUsuNombre(dto.getUsuNombre());
        usuario.setUsuCorreo(dto.getUsuCorreo());
        usuario.setUsuTelefono(dto.getUsuTelefono());
        usuario.setUsuPassword(dto.getUsuPassword());
        usuario.setUsuDocnum(dto.getUsuDocnum());
        usuario.setTipoDeDocumento(tipoDeDocumento);
        usuario.setRol(rol);

        // campos por defecto para origen y estado
        usuario.setUsuActivo(true);
        usuario.setUsuOrigen(null);

        return usuario;
    }
}
