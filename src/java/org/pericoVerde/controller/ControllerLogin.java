package org.pericoVerde.controller;

import org.pericoVerde.bd.ConexionMySQL;
import org.pericoVerde.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ControllerLogin {

    public Usuario login(String nombreUsuario, String contrasenia) throws Exception {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND contrasenia = ?";
        
        ConexionMySQL conexionMySQL = new ConexionMySQL();
        Connection conn = conexionMySQL.open();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            ps.setString(2, contrasenia);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreUsuario(rs.getString("nombre_usuario"));
            }
        } finally {
            conn.close();
        }
        return usuario;
    }
}