package src;
/**
 *
 * @author ricardenas, alan, yael
 */
import java.time.LocalDateTime;
import java.util.Scanner;

public class SistemaDeImpresion {
	public static void main(String[] args){
		Logica logica = new Logica();
		Scanner leer = new Scanner(System.in);
		int opc;
		do{
			System.out.println("\nMANEJO DE LA COLA DE UNA IMPRESORA");
			System.out.println("[1] Enviar un documento");
			System.out.println("[2] Imprimir el documento");
			System.out.println("[3] Ver los documentos en la cola");
			System.out.println("[4] Ver la cantidad de documentos en la cola");
			System.out.println("[5] Finalizar programa");
			System.out.print("¿Opcion deseada? [1-5]: ");
			opc = leer.nextInt();
			switch(opc){
				case 1:
					try{
						logica.enviarDocumento();
					} catch(IllegalStateException e){
						System.out.println(e.getMessage());
					}
					break;
				case 2:
					try{
						logica.imprimirDocumento();
					} catch(IllegalStateException e){
						System.out.println(e.getMessage());
					}
					break;
				case 3:
					try{
						logica.verDocumentos();
					} catch(IllegalStateException e){
						System.out.println(e.getMessage());
					}
					break;
				case 4:	
					logica.cantidadDocumentos();
					break;
				case 5:
					System.out.println("\nFin del Programa\nProgramador: Ricardo, Alan, Yael");
					break;
				default: 
					leer.nextLine();
					System.out.println("\nOpcion no valida. Intente de nuevo");
			}
		}while(opc != 5);
	}

}

class Logica{
	TdaColaCircular<Impresora> impresora = new TdaColaCircular<>(8);
	Scanner leer = new Scanner(System.in);
	Impresora documento;
	public void enviarDocumento(){
		LocalDateTime ahora = LocalDateTime.now();

		System.out.println("Ingrese el id del archivo");
		String id = leer.nextLine();
		System.out.println("Ingrese el nombre del usuario");
		String usuario = leer.nextLine();
		System.out.println("Ingrese el nombre del archivo");
		String nombre = leer.nextLine();
		System.out.println("Ingrese el numero de paginas");
		int numero = leer.nextInt();
		leer.nextLine();

		int hora = ahora.getHour();
		int minutos = ahora.getMinute();
		int segundos = ahora.getSecond();

		documento = new Impresora(id, usuario, nombre, numero, hora, minutos, segundos);

		impresora.encolar(documento);
	}

	public void imprimirDocumento(){
		LocalDateTime ahora = LocalDateTime.now();

		documento = impresora.desencolar();
		System.out.println("Datos del documento impreso: ");
		System.out.println("Id: " + documento.getId());
		System.out.println("Nombre del usuario: " + documento.getNombreUsuario());
		System.out.println("Nombre del archivo: " + documento.getNombreArchivo());
		System.out.println("Numero de paginas: " + documento.getNumPaginas());

		int diferenciaHora = ahora.getHour() - documento.getHora();
		int diferenciaMinutos = ahora.getMinute() - documento.getMinutos();
		int diferenciaSegundos = ahora.getSecond() - documento.getSegundos();

		diferenciaSegundos += diferenciaHora * 3600 + diferenciaMinutos * 60;

		System.out.println("El documento tardo " + diferenciaSegundos + " segundos en ser impreso");
	}

	public void verDocumentos(){
		//q pereza
	}

	public void cantidadDocumentos(){
		System.out.println("Actualmente hay " + impresora.getTamano() + " documentos en cola");
	}

}