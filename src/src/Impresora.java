/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src;
/**
 *
 * @author ricardo, alan, yael
 */
public class Impresora {
	private String id;
	private String nombreUsuario;
	private String nombreArchivo;
	private int numPaginas;
	private int[] fecha = new int[3];

	public Impresora(String id, String nombreUsuario, String nombreArchivo, int numPaginas, int hora, int minutos, int segundos){
		this.id = id;
		this.nombreUsuario = nombreUsuario;
		this.nombreArchivo = nombreArchivo;
		this.numPaginas = numPaginas;
		this.fecha[0] = hora;
		this.fecha[1] = minutos;
		this.fecha[2] = segundos;
	}

	public String getId() {
		return id;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public int getNumPaginas() {
		return numPaginas;
	}

	public int getHora() {
		return fecha[0];
	}

	public int getMinutos() {
		return fecha[1];
	}

	public int getSegundos() {
		return fecha[2];
	}
	

	public void setId(String id) {
		this.id = id;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public void setNumPaginas(int numPaginas) {
		this.numPaginas = numPaginas;
	}

	public void setHora(int hora) {
		this.fecha[0] = hora;
	}

	public void setMinutos(int minutos) {
		this.fecha[1] = minutos;
	}

	public void setSegundos(int segundos) {
		this.fecha[2] = segundos;
	}

	@Override
	public String toString() {
		return "Impresora{" + "id=" + id + ", nombreUsuario=" + nombreUsuario + ", nombreArchivo=" + nombreArchivo + ", numPaginas=" + numPaginas + ", fecha=" + fecha + '}';
	}

}
