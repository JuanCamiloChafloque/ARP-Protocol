package Interfaz;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PanelOpciones extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton btnCargar;
	private JButton btnGuardar;
	private JButton btnTramaManual;
	private Thread envioManual;
	private Thread guardarDispositivos;
	private MainInterfaz principal;

	private static final String CARGAR = "car";
	private static final String GUARDAR = "guardar";
	private static final String MANUAL = "manual";

	public PanelOpciones(MainInterfaz pPrincipal) {

		setLayout(new GridLayout(1, 3));
		principal = pPrincipal;

		btnCargar = new JButton("Cargar");
		btnCargar.setActionCommand(CARGAR);
		btnCargar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String archivo = JOptionPane.showInputDialog(null, "Digite el nombre del archivo a cargar.");
				principal.cargarArchivo(archivo);
			}
		});

		btnGuardar = new JButton("Guardar");
		btnGuardar.setActionCommand(GUARDAR);
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				guardarDispositivos = new Thread(new Runnable() {
					public void run() {
						String archivo = JOptionPane.showInputDialog(null, "Digite el nombre del archivo a guardar.");
						principal.guardarArchivo(archivo);
					}
				});
				guardarDispositivos.start();
			}
		});

		btnTramaManual = new JButton("Enviar Trama Manual");
		btnTramaManual.setActionCommand(MANUAL);
		btnTramaManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				envioManual = new Thread(new Runnable() {
					public void run() {
						String pIp = (JOptionPane.showInputDialog(null, "Digite el IP para enviar trama ARP. [1 - 254]"));
						principal.enviarTramaIndividual(pIp);
					}
				});
				envioManual.start();
			}
		});

		add(btnTramaManual);
		add(btnCargar);
		add(btnGuardar);

	}

}
