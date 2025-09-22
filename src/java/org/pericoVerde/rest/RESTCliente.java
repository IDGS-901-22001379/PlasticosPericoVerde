package org.pericoVerde.rest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.pericoVerde.model.Cliente;
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
import org.pericoVerde.controller.ControllerCliente;

/**
 * Servicio REST para manejar las operaciones CRUD de Cliente
 *
 */
@Path("Cliente")
public class RESTCliente {

    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCliente(@FormParam("datosCliente") @DefaultValue("") String datosCliente) {

        ControllerCliente cc = new ControllerCliente();
        Gson gson = new Gson();
        String out = null;

        try {
            Cliente cliente = gson.fromJson(datosCliente, Cliente.class);

            if (cliente == null) {
                throw new IllegalArgumentException("Invalid data for cliente");
            }

            if (cliente.getIdCliente() < 1) {
                cc.insert(cliente);
            } else {
                cc.update(cliente);
            }
            out = """
             {"result":"Datos de Cliente guardados correctamente."}
             """;
        } catch (JsonSyntaxException | IllegalArgumentException ex) {
            ex.printStackTrace();
            out = "{\"error\":\"Datos inválidos para el cliente: " + ex.toString().replaceAll("\"", "") + "\"}";
        } catch (Exception ex) {
            ex.printStackTrace();
            out = "{\"error\":\"" + ex.toString().replaceAll("\"", "") + "\"}";
        }

        return Response.ok(out).build();
    }

    @Path("modificar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@FormParam("modificacionCliente") @DefaultValue("") String modificacionCliente) {
        Cliente c = null;
        ControllerCliente cc = new ControllerCliente();
        String out = null;
        Gson gson = new Gson();
        System.out.println("Llegue al rest");
        System.out.println(modificacionCliente);
        try {
            gson = new Gson();
            c = gson.fromJson(modificacionCliente, Cliente.class);
            cc.update(c);

            out = """
                {"response":"Datos de Cliente modificados correctamente"}
                """;
        } catch (Exception ex) {
            out = "{\"response\":\"Ocurrió un error en el servidor. " + ex.toString().replaceAll("\"", "") + "\"}";
            ex.getStackTrace();
            ex.printStackTrace();
        }
        return Response.ok(out).build();
    }

    @Path("eliminar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response borradoLogico(@FormParam("idCliente") @DefaultValue("0") int idCliente) {
        ControllerCliente cc = new ControllerCliente();
        String out = null;
        try {
            cc.delete(idCliente);
            out = "{\"result\":\"Cliente eliminado correctamente.\"}";
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"" + e.toString().replaceAll("\"", "") + "\"}";
        }
        return Response.ok(out).build();
    }

    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        ControllerCliente cc = new ControllerCliente();
        List<Cliente> clientes = null;
        String out = null;
        Gson gson = new Gson();
        try {
            clientes = cc.getAll();
            out = gson.toJson(clientes);
            System.out.println(clientes);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"" + e.toString().replaceAll("\"", "") + "\"}";
        }
        return Response.ok(out).build();
    }

    @Path("get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCliente(@QueryParam("idCliente") @DefaultValue("0") int idCliente) {
        ControllerCliente cc = new ControllerCliente();
        Cliente cliente = null;
        String out = null;
        Gson gson = new Gson();
        try {
            cliente = cc.get(idCliente);
            if (cliente != null) {
                out = gson.toJson(cliente);
            } else {
                out = "{\"error\":\"Cliente no encontrado\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"" + e.toString().replaceAll("\"", "") + "\"}";
        }
        return Response.ok(out).build();
    }
}
