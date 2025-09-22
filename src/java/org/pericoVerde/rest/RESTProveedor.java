package org.pericoVerde.rest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.pericoVerde.model.Proveedor;
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
import org.pericoVerde.controller.ControllerProveedor;

/**
 * Servicio REST para manejar las operaciones CRUD de Proveedor
 *
 */
@Path("Proveedor")
public class RESTProveedor {

    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertProveedor(@FormParam("datosProveedor") @DefaultValue("") String datosProveedor) {

        ControllerProveedor cp = new ControllerProveedor();
        Gson gson = new Gson();
        String out = null;

        try {
            Proveedor proveedor = gson.fromJson(datosProveedor, Proveedor.class);

            if (proveedor == null) {
                throw new IllegalArgumentException("Datos inválidos para el proveedor");
            }

            if (proveedor.getIdProveedor() < 1) {
                cp.insert(proveedor);
            } else {
                cp.update(proveedor);
            }
            out = "{\"result\":\"Datos de Proveedor guardados correctamente.\"}";
        } catch (JsonSyntaxException | IllegalArgumentException ex) {
            ex.printStackTrace();
            out = "{\"error\":\"Datos inválidos para el proveedor: " + ex.toString().replaceAll("\"", "") + "\"}";
        } catch (Exception ex) {
            ex.printStackTrace();
            out = "{\"error\":\"" + ex.toString().replaceAll("\"", "") + "\"}";
        }

        return Response.ok(out).build();
    }

    @Path("modificar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@FormParam("modificacionProveedor") @DefaultValue("") String modificacionProveedor) {
        Proveedor p = null;
        ControllerProveedor cp = new ControllerProveedor();
        String out = null;
        Gson gson = new Gson();
        try {
            p = gson.fromJson(modificacionProveedor, Proveedor.class);
            cp.update(p);

            out = "{\"response\":\"Datos de Proveedor modificados correctamente\"}";
        } catch (Exception ex) {
            out = "{\"response\":\"Ocurrió un error en el servidor: " + ex.toString().replaceAll("\"", "") + "\"}";
            ex.printStackTrace();
        }
        return Response.ok(out).build();
    }

    @Path("eliminar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProveedor(@FormParam("idProveedor") @DefaultValue("0") int idProveedor) {
        ControllerProveedor cp = new ControllerProveedor();
        String out = null;
        try {
            cp.delete(idProveedor);
            out = "{\"result\":\"Proveedor eliminado correctamente.\"}";
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
        ControllerProveedor cp = new ControllerProveedor();
        List<Proveedor> proveedores = null;
        String out = null;
        Gson gson = new Gson();
        try {
            proveedores = cp.getAll();
            out = gson.toJson(proveedores);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"" + e.toString().replaceAll("\"", "") + "\"}";
        }
        return Response.ok(out).build();
    }

    @Path("get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProveedor(@QueryParam("idProveedor") @DefaultValue("0") int idProveedor) {
        ControllerProveedor cp = new ControllerProveedor();
        Proveedor proveedor = null;
        String out = null;
        Gson gson = new Gson();
        try {
            proveedor = cp.get(idProveedor);
            if (proveedor != null) {
                out = gson.toJson(proveedor);
            } else {
                out = "{\"error\":\"Proveedor no encontrado\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"" + e.toString().replaceAll("\"", "") + "\"}";
        }
        return Response.ok(out).build();
    }
}
