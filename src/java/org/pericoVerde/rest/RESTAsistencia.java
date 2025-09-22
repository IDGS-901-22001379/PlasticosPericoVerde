/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.pericoVerde.rest;

/**
 *
 * @author Crepa
 */
import com.google.gson.Gson;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.pericoVerde.controller.ControllerAsistencia;
import org.pericoVerde.model.Asistencia;

@Path("asistencia")
public class RESTAsistencia {
    @Path("entrada")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarEntrada(@FormParam("codigoEmpleado") @DefaultValue("0") int codigoEmpleado) {
        ControllerAsistencia ca = new ControllerAsistencia();
        String out = null;
        try {
            int resultado = ca.registrarEntrada(codigoEmpleado);
            if (resultado == -1) {
                out = "{\"result\":\"Código de empleado no existe.\"}";
            } else {
                out = "{\"result\":\"Entrada registrada correctamente.\"}";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            out = String.format("{\"exception\":\"Ocurrió un error en el servidor al registrar la entrada. %s\"}", ex.toString().replaceAll("\"", ""));
        }
        return Response.ok(out).build();
    }

    @Path("salida")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarSalida(@FormParam("codigoEmpleado") @DefaultValue("0") int codigoEmpleado) {
        ControllerAsistencia ca = new ControllerAsistencia();
        String out = null;
        try {
            int resultado = ca.registrarSalida(codigoEmpleado);
            if (resultado == -1) {
                out = "{\"result\":\"Código de empleado no existe o no hay entrada registrada.\"}";
            } else {
                out = "{\"result\":\"Salida registrada correctamente.\"}";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            out = String.format("{\"exception\":\"Ocurrió un error en el servidor al registrar la salida. %s\"}", ex.toString().replaceAll("\"", ""));
        }
        return Response.ok(out).build();
    }
    
    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllHorarios() {
        ControllerAsistencia ca = new ControllerAsistencia();
        List<Asistencia> horarios = null;
        String out = null;
        Gson gson = new Gson();
        try {
            horarios = ca.getAllHorarios();
            out = gson.toJson(horarios);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"" + e.toString().replaceAll("\"", "") + "\"}";
        }
        return Response.ok(out).build();
    }
}