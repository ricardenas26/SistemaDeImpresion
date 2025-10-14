/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src;
/**
 *
 * @author ricardo, alan, yael
 */
public class TdaColaCircular<T>{
	private final int CAPACIDAD;
	private int frente;
	private int fin;
	private T elementos[];

	public TdaColaCircular(int capacidad){
		frente = -1;
		fin = -1;
		CAPACIDAD = capacidad;
		elementos = (T[]) new Object [CAPACIDAD];
	}

	public void encolar(T elemento) throws IllegalStateException{
		if(isLlena()){
			throw new IllegalStateException("La cola esta llena");
		}
		if(isVacia()){
			frente = fin = 0;
		} else{
			if(fin == CAPACIDAD-1){
				fin = -1;
			} else{
				fin++;
			}

		}

		elementos[fin] = elemento;
	}

	public boolean isLlena() {
		return (frente == 0 && fin == CAPACIDAD-1) || (fin + 1 == frente);
	}

	public T desencolar() throws IllegalStateException{
		if (isVacia()){
			throw new IllegalStateException("La cola esta vacia");
		}
		T aux = elementos[frente];
		if(frente == fin){
			frente = fin = -1;
		} else{
			if(frente == CAPACIDAD-1){
				frente = 0;
			} else{
				frente++;
			}
		}
		return aux;
	}

	public boolean isVacia(){
		return frente == -1; 
	}

	public T frenteCola() throws IllegalStateException{
		if(isVacia()){
			throw new IllegalStateException("La cola esta vacia");
		}
		return elementos[frente];
	}

	public int getCAPACIDAD() {
		return CAPACIDAD;
	}

	public int getTamano(){
		if(isVacia()){
			return 0;
		}
		if(frente < fin){
			return fin - frente + 1;
		}
		if(frente > fin){
			return CAPACIDAD - frente + fin + 1;
		}
		return 1;

	}

	@Override
	public String toString() {
		if(isVacia()){
			return "";
		}
		StringBuilder sb = new StringBuilder("[");
		if(frente <= fin){
			for(int i=frente; i<=fin; i++){
				sb.append(elementos[i]);
				if(i < fin){
					sb.append(", ");
				}
			}
		} else{
			for(int i=frente; i<CAPACIDAD; i++){
				sb.append(elementos[i]);
				sb.append(", ");
			}
			for(int i=0; i<=fin; i++){
				sb.append(elementos[i]);
				if(i < fin){
					sb.append(", ");
				}
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
}

