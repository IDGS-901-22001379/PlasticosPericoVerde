/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.pericoVerde.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.pericoVerde.bd.ConexionMySQL;
import org.pericoVerde.model.Empleado;
import org.pericoVerde.model.Usuario;

/**
 *
 * @author Crepa
 */
public class ControllerEmpleado {

   public int insertE(Empleado emp) throws Exception {
       // Abrir la conexion con BD
    ConexionMySQL connMySQL = new ConexionMySQL();
    Connection conn = connMySQL.open();
    CallableStatement cstmt = null;
    conn.setAutoCommit(false);
    try {
        
        

        String sql = "CALL insertarEmpleado (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        cstmt = conn.prepareCall(sql);

        cstmt.setString(1, emp.getNombre());
        cstmt.setString(2, emp.getApellidoPaterno());
        cstmt.setString(3, emp.getApellidoMaterno());
        cstmt.setString(4, emp.getGenero());
        cstmt.setString(5, emp.getFechaNacimiento());
        cstmt.setString(6, emp.getRfc());
        cstmt.setString(7, emp.getCurp());
        cstmt.setString(8, emp.getDomicilio());
        cstmt.setInt(9, emp.getCodigoPostal());
        cstmt.setString(10, emp.getCiudad());
        cstmt.setString(11, emp.getEstado());
        cstmt.setString(12, emp.getTelefono());
        cstmt.setString(13, emp.getFechaIngreso());
        cstmt.setString(14, emp.getPuesto());
        cstmt.setString(15, emp.getEmail());
        cstmt.setString(16, emp.getCodigoEmpleado());

        cstmt.registerOutParameter(17, Types.INTEGER);

        cstmt.executeUpdate();
        int idEmpleado=cstmt.getInt(17);
        emp.setId_empleado(idEmpleado);

        conn.commit();
                // Cerrar todos los objetos de BD
       cstmt.close();
        conn.close();
        return idEmpleado;
        
    } catch (SQLException e) {
        if (conn != null) {
            conn.rollback();
            System.out.println("Rollback realizado");
        }
        e.printStackTrace(); // Imprime la excepción para depurar
    } 
    return -1;
}


    public int delete(int idEmpleado) throws Exception {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        // Desactivar auto-commit para manejar la transacción manualmente
        conn.setAutoCommit(false);
        try {
            String sql = "UPDATE empleados SET estatusEmpleado = 0 WHERE codigo_empleado = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idEmpleado);
            int cambio = pstmt.executeUpdate();

            if (cambio == 0) {
                conn.rollback();

                return -1;
            }
            // Commit de la transacción
            conn.commit();

            pstmt.close();
            conn.close();

            return 1;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("rollback hecho");
            return 0;
        }
    }

    public List<Empleado> getAll(boolean activas) throws Exception {
        String sql = "SELECT * FROM v_Empleado";

        if (activas) {
            sql += " WHERE estatusEmpleado = 1";
        }
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        ArrayList<Empleado> empleados = new ArrayList<>();

        while (rs.next()) {
            Empleado emp = fill(rs);
            empleados.add(emp);
        }
        rs.close();
        pstmt.close();
        conn.close();

        return empleados;
    }

    private Empleado fill(ResultSet rs) throws Exception {
    Empleado e = new Empleado();

    e.setId_empleado(rs.getInt("id_empleado"));
    e.setNombre(rs.getString("nombre"));
    e.setApellidoPaterno(rs.getString("apellido_paterno"));
    e.setApellidoMaterno(rs.getString("apellido_materno"));
    e.setGenero(rs.getString("genero"));
    e.setFechaNacimiento(rs.getString("fecha_nacimiento"));
    e.setRfc(rs.getString("rfc"));
    e.setCurp(rs.getString("curp"));
    e.setDomicilio(rs.getString("domicilio"));
    e.setCodigoPostal(rs.getInt("codigo_postal"));
    e.setCiudad(rs.getString("ciudad"));
    e.setEstado(rs.getString("estado"));
    e.setTelefono(rs.getString("telefono"));
    e.setFechaIngreso(rs.getString("fecha_ingreso"));
    e.setPuesto(rs.getString("puesto"));
    e.setEmail(rs.getString("email"));
    e.setCodigoEmpleado(rs.getString("codigo_empleado"));
    e.setEstatusEmpleado(rs.getInt("estatusEmpleado"));

    return e;
}

}
