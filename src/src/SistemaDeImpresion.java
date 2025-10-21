package src;

import java.time.LocalDateTime;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
	
//==================== MAIN ====================
public class SistemaDeImpresion{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SistemaDeImpresionFrame().setVisible(true);
        });
    }
}

class SistemaDeImpresionFrame extends JFrame {
    private JTextArea txtAreaResultado;
    private JLabel lblEstado;
    private JButton btnEnviar, btnImprimir, btnVerDocumentos, btnCantidad, btnSalir;
	
    //logica
    TdaColaCircular<Impresora> impresora = new TdaColaCircular<>(8);
    Impresora documento;
    Impresora documentoFin;
    private String documentoInfo = "";
    
    private long tiempoMaximoEspera = 0;
    private long tiempoMinimoEspera = Long.MAX_VALUE;
    
    public SistemaDeImpresionFrame() {
        inicializarComponentes();
        configurarVentana();
        crearMenu();
    }

    //==================== METODO CREAR BOTON ====================
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            BorderFactory.createEmptyBorder(18, 8, 18, 8)
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
	
	//==================== FIN METODO CREAR BOTON ====================
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
//////        
        // Panel izquierdo - Controles y formulario
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelIzquierdo.setBackground(new Color(240, 240, 240));
        
        // Panel de botones principales
        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 8, 47));
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
        JPanel panelFormulario = crearPanelBotones();
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

    //==================== MENU ====================
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
    //==================== FIN MENU ====================

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new GridBagLayout());
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
        
        // Configurar acción para los botones
        btnEnviar.addActionListener(e -> enviarDocumento());
        btnImprimir.addActionListener(e -> imprimirDocumento());
        btnVerDocumentos.addActionListener(e -> verDocumentos());
        btnCantidad.addActionListener(e -> cantidadDocumentos());
        btnSalir.addActionListener(e -> salirPrograma());
        
        return panel;
    }

    private void configurarVentana() {
        setTitle("Sistema de Impresión - Desarrollado por: Ricardo, Alan, Yael");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
    }

	//==================== LOGICA ====================
    private void enviarDocumento() {
		try {
		    LocalDateTime ahora = LocalDateTime.now();

			int hora = ahora.getHour();
			int minutos = ahora.getMinute();
			int segundos = ahora.getSecond();
			
            JPanel panel = new JPanel();
            panel.setLayout(new java.awt.GridLayout(4, 2, 5, 5));

            javax.swing.JTextField campoId = new javax.swing.JTextField(10);
            javax.swing.JTextField campoUsuario = new javax.swing.JTextField(10);
            javax.swing.JTextField campoArchivo = new javax.swing.JTextField(10);
			javax.swing.JTextField campoPaginas = new javax.swing.JTextField(10);

            panel.add(new JLabel("ID:"));
            panel.add(campoId);
            panel.add(new JLabel("Nombre de usuario:"));
            panel.add(campoUsuario);
            panel.add(new JLabel("Nombre del archivo:"));
            panel.add(campoArchivo);
            panel.add(new JLabel("Numero de paginas:"));
            panel.add(campoPaginas);

            int resultado = JOptionPane.showConfirmDialog(this, panel, "Agregar Motor",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (resultado == JOptionPane.OK_OPTION) {
                String id = campoId.getText().trim();
                String usuario = campoUsuario.getText().trim();
                String archivo = campoArchivo.getText().trim();
				int paginas;
				try {
					paginas = Integer.parseInt(campoPaginas.getText().trim());
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(this, "El número de páginas debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

                if (id.isEmpty() || usuario.isEmpty() || archivo.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                documento = new Impresora(id, usuario, archivo, paginas, hora, minutos, segundos);
				documentoFin = documento;
                impresora.encolar(documento);
				
                    documentoInfo = "ID: " + documento.getId() + "\n" +
                         "Usuario: " + documento.getNombreUsuario() + "\n" +
                         "Archivo: " + documento.getNombreArchivo() + "\n" +
                         "Paginas: " + documento.getNumPaginas() + "\n" +
                         "Hora de envio: " + String.format("%02d:%02d:%02d", hora, minutos, segundos) + "\n";
				
				txtAreaResultado.setText("DOCUMENTO ENVIADO CON EXITO\n" +
										"==================\n" +
										"Datos del documento enviado: \n" + 
										documentoInfo);
            }
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void imprimirDocumento() {
        try{
            LocalDateTime ahora = LocalDateTime.now();

            documento = impresora.desencolar();
        
            int diferenciaHora = ahora.getHour() - documento.getHora();
            int diferenciaMinutos = ahora.getMinute() - documento.getMinutos();
            int diferenciaSegundos = ahora.getSecond() - documento.getSegundos();
            diferenciaSegundos += diferenciaHora * 3600 + diferenciaMinutos * 60;

            long tiempoActual = (long) diferenciaSegundos;
            if (tiempoActual > tiempoMaximoEspera) {
                tiempoMaximoEspera = tiempoActual;
            }
            if (tiempoActual < tiempoMinimoEspera) {
                tiempoMinimoEspera = tiempoActual;
            }

            documentoInfo = "Datos del documento impreso:\n" +
                            "ID: " + documento.getId() + "\n" +
                            "Nombre del usuario: " + documento.getNombreUsuario() + "\n" +
                            "Nombre del archivo: " + documento.getNombreArchivo() + "\n" +
                            "Numero de paginas: " + documento.getNumPaginas() + "\n" +
                            "Tiempo de impresion: " + diferenciaSegundos + " segundos";
            
            txtAreaResultado.setText("DOCUMENTO IMPRESO\n" +
                                     "==================\n" +
                                     documentoInfo);
            
            lblEstado.setText("Estado: Documento impreso - " + impresora.getTamano() + " en cola");
            
            JOptionPane.showMessageDialog(this, 
                                          documentoInfo, 
                                          "Documento Impreso Exitosamente", 
                                          JOptionPane.INFORMATION_MESSAGE);

        }catch (IllegalStateException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verDocumentos() {
        String documentos;
        
        if (impresora.isVacia()) {
            documentos = "DOCUMENTOS EN COLA DE IMPRESION\n" +
                         "===============================\n" +
                         "No hay documentos en la cola de impresión\n" +
                         "-----------------------------------------";
        } else{
            documentos = "DOCUMENTOS EN COLA DE IMPRESION\n" +
                         "===============================\n" +
                         "Total de documentos: " + impresora.getTamano() + "\n" + 
                         impresora.toString();
        }
        
        txtAreaResultado.setText(documentos);
        lblEstado.setText("Estado: Mostrando documentos - " + impresora.getTamano() + " en cola");
        
        JTextArea areaDialogo = new JTextArea(15, 50); 
        areaDialogo.setText(documentos);
        areaDialogo.setEditable(false);
        areaDialogo.setFont(new Font("Consolas", Font.PLAIN, 13));
        
        JScrollPane scrollDialogo = new JScrollPane(areaDialogo);
        
        JOptionPane.showMessageDialog(this, 
                                      scrollDialogo, 
                                      "Vista de Documentos en Cola", 
                                      JOptionPane.PLAIN_MESSAGE);
    }

    private void cantidadDocumentos() {
        
        String infoCantidad = "Actualmente hay " + impresora.getTamano() + " documentos en cola\n" +
                              "Capacidad maxima: 8 documentos\n" +
                              "Espacio disponible: " + (8 - impresora.getTamano()) + " documentos";
        
        txtAreaResultado.setText("CANTIDAD DE DOCUMENTOS\n" +
                                 "======================\n" +
                                 infoCantidad); 
                                 
        lblEstado.setText("Estado: Mostrando cantidad - " + impresora.getTamano() + " en cola");
        
        JOptionPane.showMessageDialog(this, 
                                      infoCantidad, 
                                      "Cantidad de Documentos", 
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    private void salirPrograma() {
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de que desea salir del programa?\n\n" +
            "Programadores: Ricardo, Alan, Yael", 
            "Confirmar Salida", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            
            String estadisticasInfo;
            
            if (tiempoMinimoEspera == Long.MAX_VALUE) { 
                estadisticasInfo = "No se imprimieron documentos en esta sesión.";
            } else {
                
                double tiempoPromedio = (double) (tiempoMaximoEspera + tiempoMinimoEspera) / 2.0;
                
                estadisticasInfo = "Estadísticas de Documentos Impresos:\n" +
                                   "====================================\n" +
                                   "Tiempo MÁXIMO de espera: " + tiempoMaximoEspera + " segundos\n" +
                                   "Tiempo MÍNIMO de espera: " + tiempoMinimoEspera + " segundos\n" +
                                   String.format("Tiempo PROMEDIO (max/min): %.2f segundos", tiempoPromedio);
            }
            
            JOptionPane.showMessageDialog(this, 
                "PROGRAMA FINALIZADO\n" +
                "===================\n" +
                estadisticasInfo + "\n\n" +
                "Desarrollado por:\n" +
                "• Ricardo\n" +
                "• Alan\n" +
                "• Yael", 
                "Fin del Programa", 
                JOptionPane.INFORMATION_MESSAGE);
            
            System.exit(0);
        }
    }
	//==================== FIN LOGICA ====================
}
