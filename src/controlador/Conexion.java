/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.JOptionPane;

/**
 *
 * @author zamuria
 */
public class Conexion {

    private static Connection connect = null;
    private static Conexion INSTANCE = null;

    private Conexion() {
        try {
            // Cargamos en ejecución 
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://"+DatosSesion.ip+"/"+DatosSesion.bd+"?user="+DatosSesion.bduser+"&password="+DatosSesion.bdpassr+"");
            //Mensaje de conexion realizada
         System.out.println("Conexion realizada");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static Connection getConexion() {
        if (connect == null) {
            INSTANCE = new Conexion();
        }
        return connect;
    }
}
