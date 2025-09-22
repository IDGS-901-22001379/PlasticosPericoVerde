package org.pericoVerde.controller;

import org.pericoVerde.bd.ConexionMySQL;
import org.pericoVerde.model.Proveedor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ControllerProveedor {

    public int insert(Proveedor proveedor) throws Exception {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        int idProveedorGenerado = 0;

        try {
            conn.setAutoCommit(false);  // Desactivar autocommit

            String sql = """
                         CALL insertarProveedor(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                         """;
            CallableStatement cstmt = conn.prepareCall(sql);

            cstmt.setString(1, proveedor.getNombre());
            cstmt.setString(2, proveedor.getTipoProveedor());
            cstmt.setString(3, proveedor.getRfc());
            cstmt.setString(4, proveedor.getDireccion());
            cstmt.setString(5, proveedor.getCodigoPostal());
            cstmt.setString(6, proveedor.getCiudad());
            cstmt.setString(7, proveedor.getEstado());
            cstmt.setString(8, proveedor.getTelefono());
            cstmt.setInt(9, proveedor.getEstatusProveedores());
            cstmt.setString(10, proveedor.getCorreoElectronico());

            cstmt.registerOutParameter(11, Types.INTEGER);

            cstmt.executeUpdate();
            conn.commit();  // Confirmar la transacción

            idProveedorGenerado = cstmt.getInt(11);
            proveedor.setIdProveedor(idProveedorGenerado);

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

        return idProveedorGenerado;
    }

    public void update(Proveedor proveedor) throws Exception {
        String sql = """
                 CALL modificarProveedor(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                 """;
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);

        try {
            conn.setAutoCommit(false);  // Desactivar autocommit

            cstmt.setInt(1, proveedor.getIdProveedor());
            cstmt.setString(2, proveedor.getNombre());
            cstmt.setString(3, proveedor.getTipoProveedor());
            cstmt.setString(4, proveedor.getRfc());
            cstmt.setString(5, proveedor.getDireccion());
            cstmt.setString(6, proveedor.getCodigoPostal());
            cstmt.setString(7, proveedor.getCiudad());
            cstmt.setString(8, proveedor.getEstado());
            cstmt.setString(9, proveedor.getTelefono());
            cstmt.setInt(10, proveedor.getEstatusProveedores());
            cstmt.setString(11, proveedor.getCorreoElectronico());

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

    public List<Proveedor> getAll() throws Exception {
        String sql = "SELECT * FROM proveedores";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        List<Proveedor> proveedores = new ArrayList<>();

        while (rs.next()) {
            Proveedor proveedor = fill(rs);
            proveedores.add(proveedor);
        }

        rs.close();
        pstmt.close();
        conn.close();

        return proveedores;
    }

    private Proveedor fill(ResultSet rs) throws Exception {
        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor(rs.getInt("id_proveedor"));
        proveedor.setNombre(rs.getString("nombre"));
        proveedor.setTipoProveedor(rs.getString("tipo_proveedor"));
        proveedor.setRfc(rs.getString("rfc"));
        proveedor.setDireccion(rs.getString("direccion"));
        proveedor.setCodigoPostal(rs.getString("codigo_postal"));
        proveedor.setCiudad(rs.getString("ciudad"));
        proveedor.setEstado(rs.getString("estado"));
        proveedor.setTelefono(rs.getString("telefono"));
        proveedor.setEstatusProveedores(rs.getInt("estatusProveedores"));
        proveedor.setCorreoElectronico(rs.getString("correo_electronico"));

        return proveedor;
    }

    public void delete(int idProveedor) throws Exception {
        String sql = "{CALL eliminarProveedor(?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);

        try {
            conn.setAutoCommit(false);  // Desactivar autocommit

            cstmt.setInt(1, idProveedor);
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

    public Proveedor get(int idProveedor) throws Exception {
        String sql = "SELECT * FROM proveedores WHERE id_proveedor = ?";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, idProveedor);
        ResultSet rs = pstmt.executeQuery();
        Proveedor proveedor = null;

        if (rs.next()) {
            proveedor = fill(rs);
        }

        rs.close();
        pstmt.close();
        conn.close();

        return proveedor;
    }
}
