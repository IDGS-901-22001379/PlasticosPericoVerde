/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.pericoVerde.rest;

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
import org.pericoVerde.controller.ControllerEmpleado;
import org.pericoVerde.model.Empleado;

/**
 *
 * @author Crepa
 */
@Path("empleado")
public class RESTEmpleado {

    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosEmpleado") @DefaultValue("") String datosEmpleado) throws Exception {
        ControllerEmpleado ce = new ControllerEmpleado();
        String out = null;
        String receivedData = "";
        try {
            Gson gson = new Gson();
            Empleado empleado = gson.fromJson(datosEmpleado, Empleado.class);
            System.out.println("Datos recibidos en el servidor: " + datosEmpleado);

            receivedData = datosEmpleado;

            int id = ce.insertE(empleado);
            out = "{\"result\":\"Datos del Empleado guardados correctamente.\", \"receivedData\": " + receivedData + ", \"ID Generado\": " + id + "}";
            return Response.ok(out).build();
        } catch (Exception ex) {
            // En caso de excepción, crear una respuesta JSON con el mensaje de error y los datos recibidos
            out = "{\"exception\":\"Ocurrió un error en el servidor. El empleado no se ha guardado o actualizado correctamente. %s\", \"receivedData\": " + receivedData + "}";
            out = String.format(out, ex.toString().replaceAll("\"", ""));
            ex.printStackTrace();  // Imprime el stack trace de la excepción
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }

    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("activas") boolean activas) {
        ControllerEmpleado ce = new ControllerEmpleado();
        List<Empleado> empleados = null;
        String out = null;
        Gson gson = new Gson();
        try {
            empleados = ce.getAll(activas);
            out = gson.toJson(empleados);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":" + e.toString().replaceAll("\"", "") + "\"}";
        }
        return Response.ok(out).build();
    }

     @Path("delete")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@QueryParam("idEmpleado") @DefaultValue("0") int idEmpleado) {
        ControllerEmpleado ce = new ControllerEmpleado();
        String out = null;
        try {
            int estatus = ce.delete(idEmpleado);
            out = "{\"result\":\"Empleado eliminado correctamente.\", \"Estatus Generado\": " + estatus + "}";
        } catch (Exception ex) {
            ex.printStackTrace();
            out = String.format("{\"exception\":\"Ocurrió un error en el servidor. El empleado no se ha eliminado correctamente. %s\"}", ex.toString().replaceAll("\"", ""));
        }
        return Response.ok(out).build();
    }

}
