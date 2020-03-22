package Interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Modelo.ARP;
import Modelo.Dispositivo;
import Persistencia.ManejoArchivos;

public class MainInterfaz extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private PanelEncabezado panelEncabezado;
	private PanelOpciones panelOpciones;
	private ARP modelo;
	private PanelControlDispositivos panelControl;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainInterfaz frame = new MainInterfaz();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainInterfaz() {

		modelo = new ARP(0, 0, 0, 1, this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1292, 744);
		contentPane = new JPanel();
		contentPane.setEnabled(false);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle("Protocolo ARP");
		setFont(new Font("Tahoma", Font.PLAIN, 12));
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(1292, 700));
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		JPanel panelInfo = new JPanel();
		panelInfo.setBounds(0, 0, 1370, 281);
		contentPane.add(panelInfo);
		panelInfo.setLayout(new BorderLayout(0, 0));

		panelEncabezado = new PanelEncabezado(this);
		panelInfo.add(panelEncabezado, BorderLayout.CENTER);
		panelOpciones = new PanelOpciones(this);

		JPanel panelDisp = new JPanel();
		panelDisp.setBounds(0, 279, 1370, 430);
		contentPane.add(panelDisp);
		panelDisp.setLayout(new BorderLayout(0, 0));

		panelControl = new PanelControlDispositivos(this);
		panelControl.setVisible(true);
		panelDisp.add(panelControl, BorderLayout.CENTER);
		panelDisp.add(panelOpciones, BorderLayout.SOUTH);
	}

	public void actualizarParametros(String pPendiente, String pInactivo, String pTiempo, String pTimeout) {
		modelo.setPendiente(Integer.parseInt(pPendiente));
		modelo.setInactivo(Integer.parseInt(pInactivo));
		modelo.setTiempo(Integer.parseInt(pTiempo));
		modelo.setTimeout(Integer.parseInt(pTimeout));
	}

	public void enviarTramas() {
		try {
			modelo.enviarMesajesARP();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void enviarTramaIndividual(String pIp) {
		try {
			modelo.enviarMensajeManual(pIp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void agregarDispositivo(Dispositivo pDisp) {
		this.panelControl.agregarDispositivo(pDisp);
	}

	public void cargarArchivo(String pArchivo) {
		Map<String, Dispositivo> dispositivos = ManejoArchivos.cargarArchivo(pArchivo);
		modelo.setDispositivos(dispositivos);
	}

	public void guardarArchivo(String pArchivo) {
		ManejoArchivos.guardarArchivo(modelo, pArchivo);
	}

	public void removerDispositivo(Dispositivo disp) {
		panelControl.removerDispositivo(disp);
	}
}
