package src;
import javax.swing.*;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
import java.time.Duration;
import java.time.Instant;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author lalit
 */
public class SistemaImpresionVisual extends JFrame {
    
    private JTextArea areaDocsVisual;
    private TdaColaCircular<Documento> colaDeImpresion;
    
    public SistemaImpresionVisual(){
        setSize(1000,500);
        setTitle("Manejo de impresion de documentos");
       
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        setLayout(null);
        setLocationRelativeTo(null);
        colaDeImpresion = new TdaColaCircular<>(8);
        
        initComponents();
        actualizarListaVisual();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salirDelPrograma();
            }
        });
    }
    
    private void initComponents(){
        
        //-----Menu archivo-----
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemAcercaDe = new JMenuItem("Acerca de...");
        JMenuItem salir = new JMenuItem("Salir");
        
        menuArchivo.add(itemAcercaDe);
        itemAcercaDe.addActionListener(e ->{
            JOptionPane.showMessageDialog(this, "Nombre del programa: Sistema de impresion"
                    + "\nVersion: 2.0\nEquipo #2\n\nIntegrantes del equipo:"
                    + "\nCárdenas Sánchez Ricardo Gael\nGómez Ramos Alan Ramiro"
                    + "\nPérez Oliva Eduardo Yael");
        });
        
        menuArchivo.addSeparator();
        menuArchivo.add(salir);
        salir.addActionListener(e -> salirDelPrograma());
        
        //-----Menu Central Telefonica-----
        JMenu menuCT = new JMenu("Sistema Impresiones");
        JMenuItem enviarDoc = new JMenuItem("Enviar un documento");
        JMenuItem imprimirDoc = new JMenuItem("Imprimir el documento");
        JMenuItem verDocs = new JMenuItem("Ver los documentos en cola");
        JMenuItem cantDocs = new JMenuItem("Cantidad de documentos en la cola");
        
        menuCT.add(enviarDoc);
        menuCT.addSeparator();
        menuCT.add(imprimirDoc);
        menuCT.addSeparator();
        menuCT.add(verDocs);
        menuCT.addSeparator();
        menuCT.add(cantDocs);
        
        //-----Añadimos los menus a la barae-----
        menuBar.add(menuArchivo);
        menuBar.add(menuCT);
        
        //-----Añadimos lstners al menu -----
        enviarDoc.addActionListener(e -> accionEnviarDocumento());
        imprimirDoc.addActionListener(e -> accionImprimirDocumento());
        verDocs.addActionListener(e -> accionVerCola());
        cantDocs.addActionListener(e -> accionVerCantidad());
        
        //-----Titulo programa-----
        JLabel titulo = new JLabel("SISTEMA DE IMPRESIONES");
        titulo.setBounds(380, 10, 300, 50);
        titulo.setBackground(Color.blue);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        this.add(titulo);
        
        //-----Panel de Botones
        Color azulBajito = new Color(176,216,230);
        int anchoBoton = 160;
        int altoBoton = 120;
        int xBoton = 40;
        int yInicial = 80;
        int espacio = 20; 
        
        //-----Boton enviar-----
        JButton btnEnviar = new JButton("Enviar documento");
        btnEnviar.setBackground(azulBajito);
        btnEnviar.setBounds(xBoton,yInicial,anchoBoton,altoBoton);
        btnEnviar.setOpaque(true);
        btnEnviar.setBorderPainted(false);
        this.add(btnEnviar);

        btnEnviar.addActionListener(e -> accionEnviarDocumento());
        
        //-----Boton imprimir-----
        JButton btnImprimir = new JButton("Imprimir documento");
        btnImprimir.setBackground(azulBajito);
        btnImprimir.setBounds(xBoton + anchoBoton + espacio,yInicial,anchoBoton,altoBoton);
        btnImprimir.setOpaque(true);
        btnImprimir.setBorderPainted(false);
        this.add(btnImprimir);

        btnImprimir.addActionListener(e -> accionImprimirDocumento());
        
        //-----Boton Ver documentos-----
        JButton btnVer = new JButton("Ver/Actualizar cola");
        btnVer.setBackground(azulBajito);
        btnVer.setBounds(xBoton,yInicial + altoBoton + espacio,anchoBoton,altoBoton);
        btnVer.setOpaque(true);
        btnVer.setBorderPainted(false);
        this.add(btnVer);
        
        btnVer.addActionListener(e -> accionVerCola());
        
        //-----Boton cantidad documentos-----
        JButton btnCantidad = new JButton("Ver cantidad en cola");
        btnCantidad.setBackground(azulBajito);
        btnCantidad.setBounds(xBoton + anchoBoton + espacio, yInicial + altoBoton + espacio,anchoBoton,altoBoton);
        btnCantidad.setOpaque(true);
        btnCantidad.setBorderPainted(false);
        this.add(btnCantidad);
        
        btnCantidad.addActionListener(e -> accionVerCantidad());
        
        //-----Panel de texto derecho-----
        areaDocsVisual = new JTextArea();
        areaDocsVisual.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaDocsVisual.setEditable(false);
        JScrollPane scrollArea = new JScrollPane(areaDocsVisual);
        scrollArea.setBounds(400, 80, 560, 262);
        this.add(scrollArea);
        
    }
    
    private void actualizarListaVisual() {
        
        if (colaDeImpresion.isVacia()) {
            areaDocsVisual.setText("---------------------------- Cola Vacía -----------------------------");
            return; 
        }

        Instant horaActual = Instant.now();

        ArrayList<Documento> tempStorage = new ArrayList<>();
        StringBuilder textoDeLaCola = new StringBuilder();
        
        while (!colaDeImpresion.isVacia()) {
            try {
                tempStorage.add(colaDeImpresion.desencolar());
            } catch (Exception e) { /* Ignorar error */ }
        }

        int i = 1;
        for (Documento doc : tempStorage) {
            
            Instant horaCreacion = doc.getHoraCreacion();
            long segundosEsperando = Duration.between(horaCreacion, horaActual).getSeconds();

            textoDeLaCola.append("----------------------- Posición en Cola: ").append(i).append(" -----------------------\n");
            
            textoDeLaCola.append(doc.toString()); 
            
            textoDeLaCola.append("Tiempo en cola: ").append(segundosEsperando).append(" segundos\n");
            
            textoDeLaCola.append("\n"); 
   
            i++;
            
            try {
                colaDeImpresion.encolar(doc);
            } catch (Exception e) { /* Ignorar error */ }
        }
        
        areaDocsVisual.setText(textoDeLaCola.toString());
        areaDocsVisual.setCaretPosition(0); 
    }
    
    private void accionEnviarDocumento() {
 
        JTextField txtId = new JTextField();
        JTextField txtNombreUsuario = new JTextField();
        JTextField txtNombreArchivo = new JTextField();
        JTextField txtNoPaginas = new JTextField();

        JPanel panelDialogo = new JPanel(new GridLayout(0, 2, 5, 5));

        panelDialogo.add(new JLabel("ID:"));
        panelDialogo.add(txtId);
        panelDialogo.add(new JLabel("Nombre de Usuario:"));
        panelDialogo.add(txtNombreUsuario);
        panelDialogo.add(new JLabel("Nombre de Archivo:"));
        panelDialogo.add(txtNombreArchivo);
        panelDialogo.add(new JLabel("Número de Páginas:"));
        panelDialogo.add(txtNoPaginas);

        int resultado = JOptionPane.showConfirmDialog(this, panelDialogo,
                "Enviar Nuevo Documento", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            
            try {
                int id = Integer.parseInt(txtId.getText()); 
                String nombreUsuario = txtNombreUsuario.getText().trim();
                String nombreArchivo = txtNombreArchivo.getText().trim();
                int noPaginas = Integer.parseInt(txtNoPaginas.getText()); 

                if (nombreUsuario.isEmpty() || nombreArchivo.isEmpty() || noPaginas <= 0 || id <= 0) {
                    JOptionPane.showMessageDialog(this, "Campos vacíos o números (ID, Páginas) deben ser > 0.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return; // No continuamos
                }

                try {
                    Documento nuevoDoc = new Documento(id, nombreUsuario, nombreArchivo, noPaginas);
                    colaDeImpresion.encolar(nuevoDoc);
                    actualizarListaVisual();
                    
                } catch (IllegalStateException ex) {
                    JOptionPane.showMessageDialog(this,
                            "La cola está llena (máx 8). No se pudo enviar.",
                            "Error: Cola Llena",
                            JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID y Número de páginas deben ser valores numéricos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void accionImprimirDocumento() {
        try {
            Documento docImpreso = colaDeImpresion.desencolar();
            
            Instant horaCreacion = docImpreso.getHoraCreacion();
            Instant horaImpresion = Instant.now();
            long segundosEnCola = Duration.between(horaCreacion, horaImpresion).getSeconds();

            actualizarListaVisual();
            
            JOptionPane.showMessageDialog(this,
                    "Se imprimió correctamente:\n\n" + docImpreso.toString()
                    + "\nTiempo total en cola: " + segundosEnCola + " segundos",
                    "Impresión Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
                    
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this,
                    "No hay documentos en la cola para imprimir.",
                    "Cola Vacía",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void accionVerCola() {
        actualizarListaVisual();
        JOptionPane.showMessageDialog(this, "Vista de la cola actualizada.", "Actualizado", JOptionPane.PLAIN_MESSAGE);
    }

    private void accionVerCantidad() {
        int tamano = colaDeImpresion.getTamano();
        String mensaje;
        if (tamano == 0) {
            mensaje = "La cola está vacía.";
        } else if (tamano == 1) {
            mensaje = "Hay 1 documento en la cola.";
        } else {
            mensaje = "Hay " + tamano + " documentos en la cola.";
        }
        JOptionPane.showMessageDialog(this, mensaje, "Cantidad de Documentos", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void accionVerEstadisticas() {
        if (colaDeImpresion.isVacia()) {
            JOptionPane.showMessageDialog(this, "No hay documentos en cola para calcular estadísticas.", "Cola Vacía", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        long tiempoMaximo = 0;
        long tiempoMinimo = Long.MAX_VALUE;
        long sumaDeTiempos = 0;
        int totalDocumentos = colaDeImpresion.getTamano();
        Instant horaReferencia = Instant.now();
        
        Documento docMax = null;
        Documento docMin = null;
        
        ArrayList<Documento> tempStorage = new ArrayList<>();
        
        while (!colaDeImpresion.isVacia()) {
            Documento docActual = colaDeImpresion.desencolar();
            long segundosEnCola = Duration.between(docActual.getHoraCreacion(), horaReferencia).getSeconds();

            sumaDeTiempos += segundosEnCola;
            if (segundosEnCola > tiempoMaximo) {
                tiempoMaximo = segundosEnCola;
                docMax = docActual;
            }
            if (segundosEnCola < tiempoMinimo) {
                tiempoMinimo = segundosEnCola;
                docMin = docActual;
            }
            tempStorage.add(docActual);
        }
        
        for(Documento doc : tempStorage) {
            colaDeImpresion.encolar(doc);
        }
        
        double tiempoPromedio = (double) sumaDeTiempos / totalDocumentos;

        StringBuilder reporte = new StringBuilder("--- Estadísticas de la Cola ---\n\n");
        reporte.append("Total de documentos: ").append(totalDocumentos).append("\n\n");
        
        if (docMax != null) {
            reporte.append(String.format("Max. espera: %s (%d segundos)\n", docMax.getNombreArchivo(), tiempoMaximo));
        }
        if (docMin != null) {
            reporte.append(String.format("Min. espera: %s (%d segundos)\n", docMin.getNombreArchivo(), tiempoMinimo));
        }
        reporte.append(String.format("\nPromedio de espera: %.2f segundos.\n", tiempoPromedio));
        
        JOptionPane.showMessageDialog(this, reporte.toString(), "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void salirDelPrograma() {
        accionVerEstadisticas();
        System.exit(0);
    }
    
    public static void main(String []args){
        SistemaImpresionVisual ventana = new SistemaImpresionVisual();
        ventana.setVisible(true);
    }
}
