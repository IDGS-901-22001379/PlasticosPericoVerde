/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.pericoVerde.rest;

import com.google.gson.Gson;
import org.pericoVerde.bd.ConexionMySQL;
import org.pericoVerde.controller.ControllerLogin;
import org.pericoVerde.model.Usuario;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("login")
public class RESTLogin {

    @Path("validar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(
            @QueryParam("nombreUsuario") String nombreUsuario,
            @QueryParam("contrasenia") String contrasenia) {
        String out = null;
        ControllerLogin cl = new ControllerLogin();
        Gson gson = new Gson();
        Usuario usu = null;
        try {
            usu = cl.login(nombreUsuario, contrasenia);
            if (usu != null) {
                out = gson.toJson(usu);
            } else {
                out = "{\"error\":\"Datos de acceso incorrectos.\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"exception\":\"" + e.toString().replaceAll("\"", "") + "\"}";
        }
        return Response.ok(out).build();
    }
}