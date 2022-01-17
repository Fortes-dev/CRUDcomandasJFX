/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.crudcomandasjfx;

import java.awt.Dimension;
import java.net.URL;
import java.sql.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.MouseButton.PRIMARY;
import static javafx.scene.input.MouseButton.SECONDARY;
import javafx.scene.input.MouseEvent;
import javax.swing.JFrame;
import models.Pedido;
import models.Producto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.swing.JRViewer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 * FXML Controller class
 *
 * @author medin
 */
public class PrimaryController implements Initializable {

    @FXML
    private Label tituloPendiente;
    @FXML
    private TableView<Pedido> tablaPedidos;
    @FXML
    private TableColumn<Pedido, Long> colIdPedidos;
    @FXML
    private TableColumn<Pedido, String> colProductoPedidos;
    @FXML
    private TableColumn<Pedido, Date> colFechaPedidos;
    @FXML
    private TableColumn<Pedido, Double> colPrecioPedidos;
    @FXML
    private TableColumn<Pedido, String> colPendientePedidos;
    @FXML
    private TableColumn<Pedido, String> colRecogidoPedidos;
    @FXML
    private TableView<Producto> tablaProductos;
    @FXML
    private TableColumn<Producto, Long> colIdProducto;
    @FXML
    private TableColumn<Producto, String> colNombreProducto;
    @FXML
    private TableColumn<Producto, String> colTipoProducto;
    @FXML
    private TableColumn<Producto, Double> colPrecioProducto;

    private static Session s;

    static {
        s = HibernateUtil.getSessionFactory().openSession();
    }

    private static final Query LISTCARTA = s.createQuery("FROM Producto");
    private static final Query PRODUCTOID = s.createQuery("FROM Producto pr where pr.id=:id");
    private static final Query PEDIDOID = s.createQuery("FROM Pedido p where p.id=:id");
    private static final Query PENDIENTEHOY = s.createQuery("FROM Pedido p where p.fecha=:fecha");
    private static final Query PENDIENTESINRECOGER = s.createQuery("FROM Pedido p where p.fecha=:fecha and p.recogido='no'");
    @FXML
    private Button btnPedidos;
    @FXML
    private Button btnCarta;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initTablePedido();
        initTableProducto();

        tablaPedidos.setOnMouseClicked((MouseEvent e) -> {

            if (e.getButton().equals(PRIMARY)) {
                var alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("Recoger pedido");
                alerta.setHeaderText("RECOGER");
                alerta.setGraphic(new ImageView(new Image(this.getClass().getResource("/img/burger.png").toString())));
                alerta.setContentText("¿Desea recoger el pedido? \n[id=" + tablaPedidos.getSelectionModel().getSelectedItem().getId()
                        + "\nProducto=" + tablaPedidos.getSelectionModel().getSelectedItem().getProducto().getNombre()
                        + "\nFecha=" + tablaPedidos.getSelectionModel().getSelectedItem().getFecha() + "]");

                alerta.showAndWait();

                if (alerta.getResult().equals(ButtonType.OK)) {

                    Long id;

                    if (tablaPedidos.getSelectionModel().getSelectedItem() != null) {
                        id = tablaPedidos.getSelectionModel().getSelectedItem().getId();

                        PEDIDOID.setParameter("id", id);

                        var pedido = (Pedido) PEDIDOID.list().get(0);

                        pedido.setPendiente("no");
                        pedido.setRecogido("si");

                        Transaction t = s.beginTransaction();
                        s.update(pedido);
                        t.commit();

                        initTablePedido();
                    }
                }

            } else if (e.getButton().equals(SECONDARY)) {
                var alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("Eliminar pedido");
                alerta.setHeaderText("ELIMINAR");
                ((Button) alerta.getDialogPane().lookupButton(ButtonType.OK)).setText("Eliminar");
                alerta.setGraphic(new ImageView(new Image(this.getClass().getResource("/img/borrar.png").toString())));
                alerta.setContentText("¿Desea eliminar el pedido? \n[id=" + tablaPedidos.getSelectionModel().getSelectedItem().getId()
                        + "\nProducto=" + tablaPedidos.getSelectionModel().getSelectedItem().getProducto().getNombre()
                        + "\nFecha=" + tablaPedidos.getSelectionModel().getSelectedItem().getFecha() + "]");
                alerta.showAndWait();
                if (alerta.getResult().equals(ButtonType.OK)) {

                    Long id;

                    if (tablaPedidos.getSelectionModel().getSelectedItem() != null) {
                        id = tablaPedidos.getSelectionModel().getSelectedItem().getId();
                        PEDIDOID.setParameter("id", id);

                        var pedido = (Pedido) PEDIDOID.list().get(0);

                        Transaction t = s.beginTransaction();
                        s.remove(pedido);
                        t.commit();

                        initTablePedido();
                    }
                }
            }

        });

        tablaProductos.setOnMouseClicked((MouseEvent e) -> {
            if (e.getButton().equals(PRIMARY)) {
                var alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("Comanda");
                alerta.setHeaderText("PEDIDO");
                alerta.setGraphic(new ImageView(new Image(this.getClass().getResource("/img/pedido.png").toString())));
                alerta.setContentText("Usted va a pedir este producto:\n\n" + tablaProductos.getSelectionModel().getSelectedItem().toString()
                        + "\n\n¿Desea realizar el pedido?");
                alerta.showAndWait();
                if (alerta.getResult().equals(ButtonType.OK)) {

                    Long id;

                    if (tablaProductos.getSelectionModel().getSelectedItem() != null) {
                        id = tablaProductos.getSelectionModel().getSelectedItem().getId();
                        PRODUCTOID.setParameter("id", id);

                        var producto = (Producto) PRODUCTOID.list().get(0);
                        var pedido = new Pedido();
                        var date = fechaHoy();
                        pedido.setFecha(date);
                        pedido.setProducto(producto);
                        pedido.setPrecio(producto.getPrecio());
                        pedido.setPendiente("si");
                        pedido.setRecogido("no");

                        Transaction t = s.beginTransaction();
                        s.save(pedido);
                        t.commit();

                        var confirmacion = new Alert(Alert.AlertType.INFORMATION);
                        confirmacion.setTitle("Confirmacion pedido");
                        confirmacion.setHeaderText("CONFIRMACION");
                        confirmacion.setGraphic(new ImageView(new Image(this.getClass().getResource("/img/confirm.png").toString())));
                        confirmacion.setContentText("Su pedido:\n\n" + producto.toString() + "\n\nse ha realizado con éxito.");
                        confirmacion.show();
                        initTablePedido();
                    }
                }
            }
        }
        );
    }

    public void initTablePedido() {

        tablaPedidos.getItems().clear();

        ObservableList<Pedido> listaPed = FXCollections.observableArrayList();

        colIdPedidos.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProductoPedidos.setCellValueFactory((CellDataFeatures<Pedido, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getProducto().getNombre()));
        colFechaPedidos.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colPrecioPedidos.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colPendientePedidos.setCellValueFactory(new PropertyValueFactory<>("pendiente"));
        colRecogidoPedidos.setCellValueFactory(new PropertyValueFactory<>("recogido"));

        var date = fechaHoy();
        
        PENDIENTEHOY.setParameter("fecha", date);

        listaPed.addAll(PENDIENTEHOY.list());

        tablaPedidos.setItems(listaPed);

        PENDIENTESINRECOGER.setParameter("fecha", date);
        tituloPendiente.setText("Pedidos pendientes: " + PENDIENTESINRECOGER.list().size());

        tablaPedidos.setRowFactory(row -> new TableRow<Pedido>() {
            @Override
            protected void updateItem(Pedido item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setStyle("");
                } else if (item.isRecogido().equals("si")) {
                    setStyle("-fx-background-color: #baffba;");
                } else {
                    setStyle("-fx-background-color: #ffd7d1;");
                }

            }
        });
    }

    public void initTableProducto() {
        ObservableList<Producto> listaProd = FXCollections.observableArrayList();

        tablaProductos.setItems(listaProd);

        colIdProducto.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipoProducto.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colPrecioProducto.setCellValueFactory(new PropertyValueFactory<>("precio"));

        listaProd.addAll(LISTCARTA.list());

    }

    private Date fechaHoy() {
        var utilDate = new java.util.Date();
        long lnMilisegundos = utilDate.getTime();

        return new java.sql.Date(lnMilisegundos);
    }

    @FXML
    private void btnPedidos(ActionEvent event) {
        String archivo = "pedidos.jrxml";

        try {
            var parameters = new HashMap();
            var date = fechaHoy();
            System.out.println(date);
            
            parameters.put("fechaHoy", date);

            JasperReport informe = JasperCompileManager.compileReport(archivo);
            JasperPrint impresion = JasperFillManager.fillReport(informe, parameters, Conexion.getConexion());

            JRViewer visor = new JRViewer(impresion);

            JFrame ventanaInforme = new JFrame("Pedidos");
            ventanaInforme.getContentPane().add(visor);
            ventanaInforme.pack();
            ventanaInforme.setVisible(true);
            ventanaInforme.setMinimumSize(new Dimension(1500, 1500));

            JRPdfExporter exportador = new JRPdfExporter();
            exportador.setExporterInput(new SimpleExporterInput(impresion));
            exportador.setExporterOutput(new SimpleOutputStreamExporterOutput("informe_pedidos.pdf"));

            var configuracion = new SimplePdfExporterConfiguration();
            exportador.setConfiguration(configuracion);

            exportador.exportReport();

        } catch (JRException ex) {
            System.out.println(ex);
        }
    }

    @FXML
    private void btnCarta(ActionEvent event) {
        String archivo = "carta.jrxml";

        try {
            var parameters = new HashMap();
            parameters.put("Carta", "Carta Muchachos de Artacho");

            JasperReport informe = JasperCompileManager.compileReport(archivo);
            JasperPrint impresion = JasperFillManager.fillReport(informe, parameters, Conexion.getConexion());

            JRViewer visor = new JRViewer(impresion);

            JFrame ventanaInforme = new JFrame("Carta");
            ventanaInforme.getContentPane().add(visor);
            ventanaInforme.pack();
            ventanaInforme.setVisible(true);
            ventanaInforme.setMinimumSize(new Dimension(1500, 900));

            JRPdfExporter exportador = new JRPdfExporter();
            exportador.setExporterInput(new SimpleExporterInput(impresion));
            exportador.setExporterOutput(new SimpleOutputStreamExporterOutput("carta.pdf"));

            var configuracion = new SimplePdfExporterConfiguration();
            exportador.setConfiguration(configuracion);

            exportador.exportReport();

        } catch (JRException ex) {
            System.out.println(ex);
        }
    }
}
