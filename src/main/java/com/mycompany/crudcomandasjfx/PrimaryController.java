/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.crudcomandasjfx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author medin
 */
public class PrimaryController implements Initializable {

    @FXML
    private Label tituloPendiente;
    @FXML
    private TableView<?> tablaPedidos;
    @FXML
    private TableColumn<?, ?> colIdPedidos;
    @FXML
    private TableColumn<?, ?> colProductoPedidos;
    @FXML
    private TableColumn<?, ?> colFechaPedidos;
    @FXML
    private TableColumn<?, ?> colPrecioPedidos;
    @FXML
    private TableColumn<?, ?> colPendientePedidos;
    @FXML
    private TableColumn<?, ?> colRecogidoPedidos;
    @FXML
    private TableView<?> tablaProductos;
    @FXML
    private TableColumn<?, ?> colIdProducto;
    @FXML
    private TableColumn<?, ?> colNombreProducto;
    @FXML
    private TableColumn<?, ?> colTipoProducto;
    @FXML
    private TableColumn<?, ?> colPrecioProducto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
