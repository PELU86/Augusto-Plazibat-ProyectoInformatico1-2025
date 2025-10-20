package ar.edu.et32;

import java.io.IOException;
import java.io.PrintStream;

public class main {

	public static void main(String[] args) {
		/*
		//salida de datos | canal de comunacaiones
		System.out;
		
		System.out.print(); //sin salto de linea
		System.out.println(); //salto de linea
		System.out.write(); //byte
		
		System.out.printf(texto, var); //concatenar datos
		System.out.printf(null, args);
		// "texto" + var + "texto2" + var2 <-- concatenar
		
		
		
		//salida de datos de errores
		System.err;
		System.err.printf(null, args); //concatenar datos
		
		//no se escribe directamente en el canal de comunicaciones(out)
		
		
		//canal de comunicaciones de entrada
		System.in;
		
		*/
		PrintStream ps = new PrintStream(System.out);
		PrintStream psErr = new PrintStream(System.err);
		
		ps.println("estamos todo bien");
		
		//las lineas de error tienen prioridad sobre cualquier mensaje
		//psErr.println("esti es un error");
		
		//la consola registra caracteres, la lectura es por bytes
		try {
			int linea;
			String palabra="";
			while( (linea = System.in.read())  !=  13  )
			{
				palabra = palabra + (char)linea; 
			}
			ps.println( palabra );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//forzar cambio de dato
		/*
		int pepe = 1;
		psErr.println((float)pepe);*/
		
		psErr.println("esto es un error");

		
		
		
		
		
		
		
		
		
		
		
		
	}

}
