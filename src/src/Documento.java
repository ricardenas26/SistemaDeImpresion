/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src;
import java.time.Instant;
/**
 * @author lalit
 */
public class Documento {
    
    private int id;
    private String nombreUsuario;
    private String nombreArchivo;
    private int noPaginas;
    private Instant horaCreacion;

    public Documento(int id, String nombreUsuario, String nombreArchivo, int noPaginas) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.nombreArchivo = nombreArchivo;
        this.noPaginas = noPaginas;
        this.horaCreacion = Instant.now(); //Captura el tiempo en que se crea el objeto
    }
    
    public Instant getHoraCreacion(){
        return horaCreacion;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public int getNoPaginas() {
        return noPaginas;
    }

    public void setNoPaginas(int noPaginas) {
        this.noPaginas = noPaginas;
    }

    @Override
    public String toString() {
        return "ID del ocumento: " + id + "\n"
                + "Nombre de usuario: " +nombreUsuario + "\n"
                + "Nombre del archivo: " +nombreArchivo + "\n"
                + "No. Hojas: " +noPaginas + "\n";
    }
    
    
}
