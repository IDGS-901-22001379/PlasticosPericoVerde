/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.pericoVerde.controller;

import org.pericoVerde.model.Asistencia;

/**
 *
 * @author Crepa
 */
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.pericoVerde.bd.ConexionMySQL;

public class ControllerAsistencia {
  public int registrarEntrada(int codigoEmpleado) throws Exception {
        if (!existeCodigoEmpleado(codigoEmpleado)) {
            return -1; // Código de empleado no existe
        }

        // Verificar si ya existe una entrada para el empleado en la fecha actual
        if (existeEntradaParaEmpleado(codigoEmpleado)) {
            return 0; // Ya existe una entrada para el empleado en la fecha actual
        }

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement stmt = null;
        conn.setAutoCommit(false);

        try {
            String sql = "INSERT INTO Asistencias (codigoEmpleado, fecha, horaEntrada) VALUES (?, CURDATE(), CURTIME())";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, codigoEmpleado);
            stmt.executeUpdate();
            conn.commit();
            return 1; // Entrada registrada correctamente
        } catch (SQLException se) {
            if (conn != null) {
                conn.rollback();
                System.out.println("Rollback realizado");
            }
            se.printStackTrace();
            return 0; // Error al registrar la entrada
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

   public int registrarSalida(int codigoEmpleado) throws Exception {
    if (!existeCodigoEmpleado(codigoEmpleado)) {
        return -1; // Código de empleado no existe
    }

    ConexionMySQL connMySQL = new ConexionMySQL();
    Connection conn = connMySQL.open();
    PreparedStatement stmt = null;
    conn.setAutoCommit(false);

    try {
        String sql = "UPDATE Asistencias SET horaSalida = CURTIME() WHERE codigoEmpleado = ? AND fecha = CURDATE() AND horaSalida IS NULL";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, codigoEmpleado);
        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated == 0) {
            conn.rollback();
            return -1; // No se encontró entrada válida para el empleado en la fecha actual
        }
        conn.commit();
        return 1;
    } catch (SQLException se) {
        if (conn != null) {
            conn.rollback();
            System.out.println("Rollback realizado");
        }
        se.printStackTrace();
        return 0;
    } finally {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
private boolean existeCodigoEmpleado(int codigoEmpleado) throws Exception {
    ConexionMySQL connMySQL = new ConexionMySQL();
    Connection conn = connMySQL.open();
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        System.out.println(codigoEmpleado);
        System.out.println("hola");
        String sql = "SELECT COUNT(*) FROM empleados WHERE codigo_empleado = ?";
        System.out.println(sql);
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, codigoEmpleado);
        System.out.println(stmt);
        rs = stmt.executeQuery();
        if (rs.next()) {
            int count = rs.getInt(1);
            return count > 0;
        }
        return false;
    } catch (SQLException se) {
        se.printStackTrace();
        return false;
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}

private boolean existeEntradaParaEmpleado(int codigoEmpleado) throws Exception {
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) FROM Asistencias WHERE codigoEmpleado = ? AND fecha = CURDATE()";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, codigoEmpleado);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
            return false;
        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    
  public List<Asistencia> getAllHorarios() throws Exception {
        String sql = "SELECT * FROM v_Horarios";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        ArrayList<Asistencia> horarios = new ArrayList<>();

        while (rs.next()) {
            Asistencia asistencia = fill(rs);
            horarios.add(asistencia);
        }
        rs.close();
        pstmt.close();
        conn.close();

        return horarios;
    }

    private Asistencia fill(ResultSet rs) throws Exception {
        Asistencia a = new Asistencia();

        a.setIdAsistencia(rs.getInt("idAsistencia"));
        a.setCodigoEmpleado(rs.getInt("codigoEmpleado"));
        a.setNombre(rs.getString("nombre"));
        a.setApellidoPaterno(rs.getString("apellido_paterno"));
        a.setApellidoMaterno(rs.getString("apellido_materno"));
        a.setPuesto(rs.getString("puesto"));
        a.setFecha(rs.getString("fecha"));
        a.setHoraEntrada(rs.getString("horaEntrada"));
        a.setHoraSalida(rs.getString("horaSalida"));

        return a;
    }
}
