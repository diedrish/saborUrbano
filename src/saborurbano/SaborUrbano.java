/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saborurbano;

import Vistas.Login.Login;
import controlador.Conexion;
import java.sql.Connection;

/**
 *
 * @author Diedrish Zamuria
 */
public class SaborUrbano {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

       

        Login l = new Login();
        l.setLocationRelativeTo(null);
        l.setVisible(true);
    }

}
