package org.pericoVerde.controller;

import org.pericoVerde.bd.ConexionMySQL;
import org.pericoVerde.model.Cliente;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ControllerCliente {

    public int insert(Cliente cliente) throws Exception {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        int idClienteGenerado = 0;

        try {
            conn.setAutoCommit(false);  // Desactivar autocommit

            String sql = """
                     CALL insertarCliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                     """;
            CallableStatement cstmt = conn.prepareCall(sql);

            cstmt.setString(1, cliente.getNombre());
            cstmt.setString(2, cliente.getApellidoPaterno());
            cstmt.setString(3, cliente.getApellidoMaterno());
            cstmt.setString(4, cliente.getGenero());
            cstmt.setString(5, cliente.getFechaNacimiento());
            cstmt.setString(6, cliente.getRfc());
            cstmt.setString(7, cliente.getCurp());
            cstmt.setString(8, cliente.getDomicilio());
            cstmt.setString(9, cliente.getCodigoPostal());
            cstmt.setString(10, cliente.getCiudad());
            cstmt.setString(11, cliente.getEstado());
            cstmt.setString(12, cliente.getTelefono());
            cstmt.setString(13, cliente.getFechaRegistro());
            cstmt.setInt(14, cliente.getEstatusCliente());
            cstmt.setString(15, cliente.getCorreoElectronico());

            cstmt.registerOutParameter(16, Types.INTEGER);

            cstmt.executeUpdate();
            conn.commit();  // Confirmar la transacción

            idClienteGenerado = cstmt.getInt(16);
            cliente.setIdCliente(idClienteGenerado);

            cstmt.close();
        } catch (Exception e) {
            conn.rollback();  // Revertir la transacción en caso de error
            throw e;
        } finally {
            try {
                conn.setAutoCommit(true);  // Volver a activar autocommit
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            conn.close();
        }

        return idClienteGenerado;
    }

    public void update(Cliente cliente) throws Exception {
        String sql = """
                 CALL modificarCliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                 """;
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);

        try {
            conn.setAutoCommit(false);  // Desactivar autocommit

            cstmt.setInt(1, cliente.getIdCliente());
            cstmt.setString(2, cliente.getNombre());
            cstmt.setString(3, cliente.getApellidoPaterno());
            cstmt.setString(4, cliente.getApellidoMaterno());
            cstmt.setString(5, cliente.getGenero());
            cstmt.setString(6, cliente.getFechaNacimiento());
            cstmt.setString(7, cliente.getRfc());
            cstmt.setString(8, cliente.getCurp());
            cstmt.setString(9, cliente.getDomicilio());
            cstmt.setString(10, cliente.getCodigoPostal());
            cstmt.setString(11, cliente.getCiudad());
            cstmt.setString(12, cliente.getEstado());
            cstmt.setString(13, cliente.getTelefono());
            cstmt.setString(14, cliente.getFechaRegistro());
            cstmt.setInt(15, cliente.getEstatusCliente());
            cstmt.setString(16, cliente.getCorreoElectronico());

            cstmt.executeUpdate();
            conn.commit();  // Confirmar la transacción

        } catch (Exception e) {
            conn.rollback();  // Revertir la transacción en caso de error
            throw e;
        } finally {
            try {
                conn.setAutoCommit(true);  // Volver a activar autocommit
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            cstmt.close();
            conn.close();
        }
    }

    public List<Cliente> getAll() throws Exception {
        String sql = "SELECT * FROM clientes";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        List<Cliente> clientes = new ArrayList<>();

        while (rs.next()) {
            Cliente cliente = fill(rs);
            clientes.add(cliente);
        }

        rs.close();
        pstmt.close();
        conn.close();

        return clientes;
    }

    private Cliente fill(ResultSet rs) throws Exception {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("id_cliente"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setApellidoPaterno(rs.getString("apellido_paterno"));
        cliente.setApellidoMaterno(rs.getString("apellido_materno"));
        cliente.setGenero(rs.getString("genero"));
        cliente.setFechaNacimiento(rs.getString("fecha_nacimiento"));
        cliente.setRfc(rs.getString("rfc"));
        cliente.setCurp(rs.getString("curp"));
        cliente.setDomicilio(rs.getString("domicilio"));
        cliente.setCodigoPostal(rs.getString("codigo_postal"));
        cliente.setCiudad(rs.getString("ciudad"));
        cliente.setEstado(rs.getString("estado"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setFechaRegistro(rs.getString("fecha_registro"));
        cliente.setEstatusCliente(rs.getInt("estatusCliente"));
        cliente.setCorreoElectronico(rs.getString("correo_electronico"));

        return cliente;
    }

    public void delete(int idCliente) throws Exception {
        String sql = "{CALL eliminarCliente(?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);

        try {
            conn.setAutoCommit(false);  // Desactivar autocommit

            cstmt.setInt(1, idCliente);
            cstmt.executeUpdate();
            conn.commit();  // Confirmar la transacción

        } catch (Exception e) {
            conn.rollback();  // Revertir la transacción en caso de error
            throw e;
        } finally {
            try {
                conn.setAutoCommit(true);  // Volver a activar autocommit
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            cstmt.close();
            conn.close();
        }
    }

    public Cliente get(int idCliente) throws Exception {
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, idCliente);
        ResultSet rs = pstmt.executeQuery();
        Cliente cliente = null;

        if (rs.next()) {
            cliente = fill(rs);
        }

        rs.close();
        pstmt.close();
        conn.close();

        return cliente;
    }
}