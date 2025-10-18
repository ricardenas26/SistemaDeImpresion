package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class SistemaDeImpresion{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SistemaDeImpresionFrame().setVisible(true);
        });
    }
}

class SistemaDeImpresionFrame extends JFrame {
    private Logica logica;
    private JTextField txtId, txtUsuario, txtNombreArchivo, txtPaginas;
    private JTextArea txtAreaResultado;
    private JLabel lblEstado;
    private JButton btnEnviar, btnImprimir, btnVerDocumentos, btnCantidad, btnSalir;

    public SistemaDeImpresionFrame() {
        logica = new Logica();
        inicializarComponentes();
        configurarVentana();
        crearMenu();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior con título
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(60, 63, 65));
        panelSuperior.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel("SISTEMA DE GESTION DE COLA DE IMPRESORA", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(220, 220, 220));
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        
        lblEstado = new JLabel("Estado: Listo");
        lblEstado.setForeground(new Color(255, 215, 0));
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelSuperior.add(lblEstado, BorderLayout.EAST);
        
        add(panelSuperior, BorderLayout.NORTH);

        // Panel central dividido
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setDividerSize(3);
        splitPane.setBackground(new Color(200, 200, 200));
        
        // Panel izquierdo - Controles y formulario
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelIzquierdo.setBackground(new Color(240, 240, 240));
        
        // Panel de botones principales
        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 8, 8));
        panelBotones.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            "Opciones del Sistema"
        ));
        panelBotones.setBackground(new Color(250, 250, 250));
        
        btnEnviar = crearBoton("Enviar un documento", new Color(46, 125, 50));
        btnImprimir = crearBoton("Imprimir documento", new Color(25, 118, 210));
        btnVerDocumentos = crearBoton("Ver documentos en cola", new Color(245, 124, 0));
        btnCantidad = crearBoton("Cantidad de documentos", new Color(123, 31, 162));
        btnSalir = crearBoton("Finalizar programa", new Color(198, 40, 40));
        
        panelBotones.add(btnEnviar);
        panelBotones.add(btnImprimir);
        panelBotones.add(btnVerDocumentos);
        panelBotones.add(btnCantidad);
        panelBotones.add(btnSalir);
        
        panelIzquierdo.add(panelBotones, BorderLayout.NORTH);
        
        // Panel de formulario
        JPanel panelFormulario = crearPanelFormulario();
        panelIzquierdo.add(panelFormulario, BorderLayout.CENTER);
        
        // Panel derecho - Solo resultados
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelDerecho.setBackground(new Color(240, 240, 240));
        
        // Área de texto para resultados
        JPanel panelResultado = new JPanel(new BorderLayout());
        panelResultado.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            "Resultados del Sistema"
        ));
        panelResultado.setBackground(new Color(250, 250, 250));
        
        txtAreaResultado = new JTextArea(20, 50);
        txtAreaResultado.setEditable(false);
        txtAreaResultado.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtAreaResultado.setBackground(new Color(253, 253, 253));
        txtAreaResultado.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane scrollResultado = new JScrollPane(txtAreaResultado);
        scrollResultado.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panelResultado.add(scrollResultado, BorderLayout.CENTER);
        
        panelDerecho.add(panelResultado, BorderLayout.CENTER);
        
        splitPane.setLeftComponent(panelIzquierdo);
        splitPane.setRightComponent(panelDerecho);
        
        add(splitPane, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            BorderFactory.createEmptyBorder(12, 5, 12, 5)
        ));
        
        // Agregar hover effects(aca de que cambia el color bien aca bien aca :v)
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.brighter());
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
                boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return boton;
    }

    private void crearMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(70, 130, 180));
        menuBar.setBorder(BorderFactory.createLineBorder(new Color(50, 100, 150)));
        
        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        menuArchivo.setForeground(Color.WHITE);
        
        JMenuItem menuAcercaDe = new JMenuItem("Acerca de...");
        menuAcercaDe.addActionListener(e -> mostrarAcercaDe());
        menuAcercaDe.setFont(new Font("Segoe UI", Font.PLAIN, 12));

	JMenuItem menuPeterAlert = new JMenuItem("Peter Alert");
	menuPeterAlert.addActionListener(e -> mostrarPeterAlert());
        menuPeterAlert.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JMenuItem menuSalir = new JMenuItem("Salir");
        menuSalir.addActionListener(e -> salirPrograma());
        menuSalir.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        menuArchivo.add(menuAcercaDe);
        menuArchivo.addSeparator();
	menuArchivo.add(menuPeterAlert);
        menuArchivo.addSeparator();
        menuArchivo.add(menuSalir);
        
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);
    }

    private void mostrarPeterAlert(){
	ImageIcon peter = new ImageIcon("src/icons/peter.png");
	JLabel label = new JLabel(peter);
	JOptionPane optionPane = new JOptionPane(label, JOptionPane.PLAIN_MESSAGE);
	JDialog dialog = optionPane.createDialog(this, "Peter Alert");

	 // Centrar exactamente sobre la ventana principal
	dialog.setLocationRelativeTo(this);
	dialog.setVisible(true);
    }

    private void mostrarAcercaDe() {
        JOptionPane.showMessageDialog(this,
            "SISTEMA DE IMPRESIÓN\n\n" +
            "Versión: 2.0\n" +
            "Equipo: #1\n\n" +
            "DESARROLLADO POR:\n" +
            "• Ricardenas\n" +
            "• Alan\n" +
            "• Yael\n\n" +
            "Sistema de gestión de cola de impresión",
            "Acerca del Sistema",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            "Formulario de Documento"
        ));
        panel.setBackground(new Color(248, 249, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Configuración de componentes
        JLabel lblId = new JLabel("ID del archivo:");
        JLabel lblUsuario = new JLabel("Nombre del usuario:");
        JLabel lblNombre = new JLabel("Nombre del archivo:");
        JLabel lblPaginas = new JLabel("Número de páginas:");
        
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPaginas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        txtId = crearTextField();
        txtUsuario = crearTextField();
        txtNombreArchivo = crearTextField();
        txtPaginas = crearTextField();
        
        // Configurar acción para los botones
        btnEnviar.addActionListener(e -> enviarDocumento());
        btnImprimir.addActionListener(e -> imprimirDocumento());
        btnVerDocumentos.addActionListener(e -> verDocumentos());
        btnCantidad.addActionListener(e -> cantidadDocumentos());
        btnSalir.addActionListener(e -> salirPrograma());
        
        // Agregar componentes al panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblId, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(txtId, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblUsuario, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(txtUsuario, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblNombre, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(txtNombreArchivo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(lblPaginas, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(txtPaginas, gbc);
        
        return panel;
    }

    private JTextField crearTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        textField.setBackground(Color.WHITE);
        return textField;
    }

    private void configurarVentana() {
        setTitle("Sistema de Impresión - Desarrollado por: Ricardo, Alan, Yael");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
    }

    private void enviarDocumento() {
        try {
            if (txtId.getText().isEmpty() || txtUsuario.getText().isEmpty() || 
                txtNombreArchivo.getText().isEmpty() || txtPaginas.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, complete todos los campos del formulario", 
                    "Campos Incompletos", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int paginas;
            try {
                paginas = Integer.parseInt(txtPaginas.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "El número de páginas debe ser un valor numérico", 
                    "Error de Formato", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            logica.setDatosTemporales(txtId.getText(), txtUsuario.getText(), 
                                    txtNombreArchivo.getText(), paginas);
            logica.enviarDocumento();
            
            String resultado = logica.getUltimoResultado();
            txtAreaResultado.setText("DOCUMENTO ENVIADO EXITOSAMENTE\n" +
                                   "================================\n" +
                                   resultado);
            
            lblEstado.setText("Estado: Documento enviado - " + logica.getTamanoCola() + " en cola");
            limpiarCamposFormulario();
            
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void imprimirDocumento() {
        try {
            String resultado = logica.imprimirDocumento();
            
            txtAreaResultado.setText("DOCUMENTO IMPRESO\n" +
                                   "==================\n" +
                                   resultado);
            
            lblEstado.setText("Estado: Documento impreso - " + logica.getTamanoCola() + " en cola");
            
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verDocumentos() {
        String documentos = logica.verDocumentos();
        txtAreaResultado.setText(documentos);
        lblEstado.setText("Estado: Mostrando documentos - " + logica.getTamanoCola() + " en cola");
    }

    private void cantidadDocumentos() {
        String cantidad = logica.cantidadDocumentos();
        txtAreaResultado.setText("CANTIDAD DE DOCUMENTOS\n" +
                               "======================\n" +
                               cantidad + "\n" +
                               "Capacidad maxima: 8 documentos\n" +
                               "Espacio disponible: " + (8 - logica.getTamanoCola()) + " documentos");
        lblEstado.setText("Estado: Mostrando cantidad - " + logica.getTamanoCola() + " en cola");
    }

    private void limpiarCamposFormulario() {
        txtId.setText("");
        txtUsuario.setText("");
        txtNombreArchivo.setText("");
        txtPaginas.setText("");
    }

    private void salirPrograma() {
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de que desea salir del programa?\n\n" +
            "Programadores: Ricardo, Alan, Yael", 
            "Confirmar Salida", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                logica.salir();
                String resultadoFinal = logica.getUltimoResultado();
                
                JOptionPane.showMessageDialog(this, 
                    "PROGRAMA FINALIZADO\n" +
                    "===================\n" +
                    resultadoFinal + "\n\n" +
                    "Desarrollado por:\n" +
                    "• Ricardo\n" +
                    "• Alan\n" +
                    "• Yael", 
                    "Fin del Programa", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, 
                    ex.getMessage() + "\n\nDesarrollado por: Ricardo, Alan, Yael", 
                    "Fin del Programa", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            System.exit(0);
        }
    }
}
// Clase Logica con visual
class Logica {
    TdaColaCircular<Impresora> impresora = new TdaColaCircular<>(8);
    Impresora documento;
    Impresora documentoFin;
    private String ultimoResultado = "";
    private String idTemp, usuarioTemp, nombreTemp;
    private int paginasTemp;

    public void setDatosTemporales(String id, String usuario, String nombre, int paginas) {
        this.idTemp = id;
        this.usuarioTemp = usuario;
        this.nombreTemp = nombre;
        this.paginasTemp = paginas;
    }

    public void enviarDocumento() {
        LocalDateTime ahora = LocalDateTime.now();

        int hora = ahora.getHour();
        int minutos = ahora.getMinute();
        int segundos = ahora.getSecond();

        documento = new Impresora(idTemp, usuarioTemp, nombreTemp, paginasTemp, hora, minutos, segundos);
        documentoFin = documento;
        impresora.encolar(documento);
        
        ultimoResultado = "ID: " + documento.getId() + "\n" +
                         "Usuario: " + documento.getNombreUsuario() + "\n" +
                         "Archivo: " + documento.getNombreArchivo() + "\n" +
                         "Paginas: " + documento.getNumPaginas() + "\n" +
                         "Hora de envio: " + String.format("%02d:%02d:%02d", hora, minutos, segundos) + "\n" +
                         "Documentos en cola: " + impresora.getTamano();
    }

    public String imprimirDocumento() {
        LocalDateTime ahora = LocalDateTime.now();

        documento = impresora.desencolar();
        
        int diferenciaHora = ahora.getHour() - documento.getHora();
        int diferenciaMinutos = ahora.getMinute() - documento.getMinutos();
        int diferenciaSegundos = ahora.getSecond() - documento.getSegundos();
        diferenciaSegundos += diferenciaHora * 3600 + diferenciaMinutos * 60;

        ultimoResultado = "Datos del documento impreso:\n" +
                         "ID: " + documento.getId() + "\n" +
                         "Nombre del usuario: " + documento.getNombreUsuario() + "\n" +
                         "Nombre del archivo: " + documento.getNombreArchivo() + "\n" +
                         "Numero de paginas: " + documento.getNumPaginas() + "\n" +
                         "Tiempo de impresion: " + diferenciaSegundos + " segundos";
	
	return ultimoResultado;
    }

    public String verDocumentos() {
        if (impresora.isVacia()) {
            return "DOCUMENTOS EN COLA DE IMPRESION\n" +
                   "===============================\n" +
                   "No hay documentos en la cola de impresión\n" +
                   "-----------------------------------------";
        }

        
        return "DOCUMENTOS EN COLA DE IMPRESION\n" +
	       "===============================\n" +
	       "Total de documentos: " + impresora.getTamano() + "\n" + 
		impresora.toString();
    }

    public String cantidadDocumentos() {
        return "Actualmente hay " + impresora.getTamano() + " documentos en cola";
    }

    public void salir() {
        LocalDateTime ahora = LocalDateTime.now();
        
        
        if(impresora.isVacia()){
            throw new IllegalArgumentException("No hay documentos en cola");
        } else {
            documento = impresora.frenteCola();
            int diferenciaHora = ahora.getHour() - documento.getHora();
            int diferenciaMinutos = ahora.getMinute() - documento.getMinutos();
            int diferenciaSegundos = ahora.getSecond() - documento.getSegundos();
            diferenciaSegundos += diferenciaHora * 3600 + diferenciaMinutos * 60;
            
            int diferenciaHora2 = ahora.getHour() - documentoFin.getHora();
            int diferenciaMinutos2 = ahora.getMinute() - documentoFin.getMinutos();
            int diferenciaSegundos2 = ahora.getSecond() - documentoFin.getSegundos();
            diferenciaSegundos2 += diferenciaHora2 * 3600 + diferenciaMinutos2 * 60;

            ultimoResultado = "Documento con mas tiempo: " + documento.getNombreArchivo() + "\n" +
                             "Tiempo: " + diferenciaSegundos + " segundos\n" +
                             "Documento con menos tiempo: " + documentoFin.getNombreArchivo() + "\n" +
                             "Tiempo: " + diferenciaSegundos2 + " segundos\n" +
                             "Tiempo promedio: " + (diferenciaSegundos + diferenciaSegundos2)/2 + " segundos";
        }
    }

    public String getUltimoResultado() {
        return ultimoResultado;
    }

    public int getTamanoCola() {
        return impresora.getTamano();
    }
    
}