/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.pericoVerde.bd;
import java.sql.Connection; //Objetos para 
import java.sql.DriverManager; //Conectarse con base de datos
/**
 *
 * @author Cielo
 */
public class ConexionMySQL {
    public Connection open () throws Exception{
        String user = "root"; //Nombre del usuario de la base de datos
        String password = "mt88xfire"; //Contraseña 
        String url = "jdbc:mysql://localhost:3306/pericoVerde?" + //Cambiar nombre de la base de datos. Dirección de donde se ubica la base de datos.
                "useSSL=false&" +
                "allowPublicKeyRetrieval=true&" +
                "useUnicode=true&" + 
                "characterEncoding=utf-8"; //URL de la conexión.
        
        Connection conn = null; //Declaración de la variable de conexión.
        
        //1. Registrar el Driver JDBC de MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.Abrimos una conexion MySQL
        conn = DriverManager.getConnection(url, user, password);
        
        return conn; //Devuelve la conexión.
    }
}
