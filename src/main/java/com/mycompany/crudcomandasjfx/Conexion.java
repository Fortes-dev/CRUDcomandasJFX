/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.crudcomandasjfx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author FranciscoRomeroGuill
 */
public class Conexion {

    private static Connection conexion;

    static {
        String url = "jdbc:mysql://localhost:3306/comandas";
        String user = "root";
        String pass = "";

        try {
            conexion = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexión realizada con exito!!!");
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    public static Connection getConexion() {
        return conexion;
    }

}
