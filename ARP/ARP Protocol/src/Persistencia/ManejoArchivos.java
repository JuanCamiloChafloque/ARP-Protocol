package Persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import javax.swing.JOptionPane;

import Modelo.ARP;
import Modelo.Dispositivo;

public class ManejoArchivos {

	public static void guardarArchivo(ARP arp, String nombreArchivo){

		File outFile = new File(nombreArchivo);
		FileOutputStream outStream = null;
		ObjectOutputStream dataOutStream = null;

		try {
			outStream = new FileOutputStream(outFile);
			dataOutStream = new ObjectOutputStream(outStream);
			dataOutStream.writeObject(arp.getDispositivos());
			JOptionPane.showMessageDialog(null, "Se guardaron los dispositivos exitosamente.");
		}
		catch (FileNotFoundException e) {
			System.out.println("Error en ruta de archivo. " + e.getMessage());
		}
		catch (IOException e) {
			System.out.println("Error guardando los datos en el archivo. " + e.getMessage());
		}
		catch (Exception e) {
			System.out.println("Error. " + e.getMessage());
		}
		finally {
			try {
				dataOutStream.close();
				outStream.close();
			}
			catch (IOException e) {
				System.out.println("Error cerrando el archivo:" + e.getMessage());
			}
		} 
	}

	@SuppressWarnings("unchecked")
	public static Map<String,Dispositivo> cargarArchivo(String nombreArchivo){

		File inFile = new File(nombreArchivo);
		FileInputStream inStream = null;
		ObjectInputStream dataInStream = null;
		Map<String,Dispositivo> disp=null;
		try {
			inStream = new FileInputStream(inFile);
			dataInStream = new ObjectInputStream(inStream);
			disp = (Map<String, Dispositivo>)dataInStream.readObject();
			JOptionPane.showMessageDialog(null, "Se cargaron los dispositivos exitosamente.");
			return disp;
		}
		catch (FileNotFoundException e) {
			System.out.println("Error en ruta de archivo:" + e.getMessage());
		}
		catch (IOException e) {
			System.out.println("Error leyendo del archivo:" + e.getMessage());
		}
		catch (Exception e) {
			System.out.println("excepcion inesperada:" + e.getMessage());
		}
		finally {
			try {
				dataInStream.close();
				inStream.close();
			}
			catch (IOException e) {
				System.out.println("excepcion cerrando el archivo:" + e.getMessage());
			}
		}
		return disp; 
	}
}
