package Interfaz;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Modelo.ARP;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.SystemColor;

public class PanelEncabezado extends JPanel {

	private static final long serialVersionUID = 1L;
	private MainInterfaz principal;
	private JTextField IP;
	private JTextField Mascara;
	private JTextField MAC;
	private JTextField Puerta;
	private JTextField tama;
	private JTextField mensajesPendiente;
	private JTextField mensajesInactivo;
	private JTextField tiempoEliminar;
	private JTextField Timeout;
	private Thread enviarTramas;
	private static final String ACTUALIZAR = "actu";
	private static final String ENVIAR = "Enviar";
	/**
	 * Create the panel.
	 */
	public PanelEncabezado(MainInterfaz app) {

		principal = app;

		setBackground(SystemColor.controlShadow);
		setBounds(0, 0, 1292, 281);
		setLayout(null);

		JLabel lblProtocoloArp = new JLabel("Protocolo ARP");
		lblProtocoloArp.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 24));
		lblProtocoloArp.setBounds(552, 11, 213, 29);
		add(lblProtocoloArp);

		JLabel lblNewLabel = new JLabel("Direccion IP:");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(166, 50, 122, 20);
		add(lblNewLabel);

		IP = new JTextField();
		IP.setHorizontalAlignment(SwingConstants.CENTER);
		IP.setBorder(null);
		IP.setForeground(Color.LIGHT_GRAY);
		IP.setFont(new Font("Tahoma", Font.ITALIC, 14));
		IP.setForeground(Color.WHITE);
		IP.setOpaque(false);
		IP.setBackground(Color.LIGHT_GRAY);
		IP.setBounds(318, 51, 104, 20);
		IP.setText(ARP.getIpLocal().getHostAddress());
		IP.setColumns(8);
		IP.setEditable(false);
		add(IP);

		JLabel lblMascaraDeSubred = new JLabel("Mascara de Subred:");
		lblMascaraDeSubred.setForeground(Color.WHITE);
		lblMascaraDeSubred.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblMascaraDeSubred.setBounds(731, 84, 144, 19);
		add(lblMascaraDeSubred);

		Mascara = new JTextField();
		Mascara.setText("255.255.255.0");
		Mascara.setBorder(null);
		Mascara.setOpaque(false);
		Mascara.setHorizontalAlignment(SwingConstants.CENTER);
		Mascara.setForeground(Color.WHITE);
		Mascara.setFont(new Font("Tahoma", Font.ITALIC, 14));
		Mascara.setColumns(8);
		Mascara.setBackground(Color.LIGHT_GRAY);
		Mascara.setBounds(885, 84, 108, 20);
		Mascara.setEditable(false);
		add(Mascara);

		JLabel lblDireccionMac = new JLabel("Direccion MAC:");
		lblDireccionMac.setForeground(Color.WHITE);
		lblDireccionMac.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDireccionMac.setBounds(164, 84, 110, 19);
		add(lblDireccionMac);

		MAC = new JTextField();
		MAC.setText(ARP.bytesAString(ARP.getMac(ARP.getIpLocal()), ":"));
		MAC.setBorder(null);
		MAC.setOpaque(false);
		MAC.setHorizontalAlignment(SwingConstants.CENTER);
		MAC.setForeground(Color.WHITE);
		MAC.setFont(new Font("Tahoma", Font.ITALIC, 14));
		MAC.setEditable(false);
		MAC.setColumns(8);
		MAC.setBackground(Color.LIGHT_GRAY);
		MAC.setBounds(318, 82, 127, 20);
		add(MAC);

		JLabel lblPuertaDeEnlace = new JLabel("Puerta de enlace:");
		lblPuertaDeEnlace.setForeground(Color.WHITE);
		lblPuertaDeEnlace.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPuertaDeEnlace.setBounds(731, 51, 144, 19);
		add(lblPuertaDeEnlace);

		Puerta = new JTextField();
		Puerta.setBorder(null);
		Puerta.setText(ARP.getSubred(ARP.getIpLocal(), ARP.getMascaraSubRed(ARP.getIpLocal())).getHostAddress());
		Puerta.setOpaque(false);
		Puerta.setHorizontalAlignment(SwingConstants.CENTER);
		Puerta.setForeground(Color.WHITE);
		Puerta.setFont(new Font("Tahoma", Font.ITALIC, 14));
		Puerta.setEditable(false);
		Puerta.setColumns(8);
		Puerta.setBackground(Color.LIGHT_GRAY);
		Puerta.setBounds(885, 51, 108, 20);
		add(Puerta);

		JLabel lblTamaoHost = new JLabel("/ Tama\u00F1o Host:");
		lblTamaoHost.setForeground(Color.WHITE);
		lblTamaoHost.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTamaoHost.setBounds(1003, 84, 144, 19);
		add(lblTamaoHost);

		tama = new JTextField();
		tama.setBorder(null);
		tama.setText("" + ARP.getHost(ARP.getTamMascara(ARP.getMascaraSubRed(ARP.getIpLocal()))));
		tama.setOpaque(false);
		tama.setHorizontalAlignment(SwingConstants.CENTER);
		tama.setForeground(Color.WHITE);
		tama.setFont(new Font("Tahoma", Font.ITALIC, 14));
		tama.setEditable(false);
		tama.setColumns(8);
		tama.setBackground(Color.LIGHT_GRAY);
		tama.setBounds(1157, 84, 63, 20);
		add(tama);

		JLabel lblCantidadDeMensajes = new JLabel("Cantidad de mensajes para entrar a estado PENDIENTE:");
		lblCantidadDeMensajes.setForeground(Color.WHITE);
		lblCantidadDeMensajes.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCantidadDeMensajes.setBounds(166, 128, 313, 19);
		add(lblCantidadDeMensajes);

		mensajesPendiente = new JTextField();
		mensajesPendiente.setBackground(Color.WHITE);
		mensajesPendiente.setBounds(499, 128, 41, 20);
		add(mensajesPendiente);
		mensajesPendiente.setColumns(10);

		JLabel lblCantidadDeMensajes_1 = new JLabel("Cantidad de mensajes para entrar a estado INACTIVO:");
		lblCantidadDeMensajes_1.setForeground(Color.WHITE);
		lblCantidadDeMensajes_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCantidadDeMensajes_1.setBounds(166, 158, 298, 19);
		add(lblCantidadDeMensajes_1);

		mensajesInactivo = new JTextField();
		mensajesInactivo.setColumns(10);
		mensajesInactivo.setBackground(Color.WHITE);
		mensajesInactivo.setBounds(499, 159, 41, 20);
		add(mensajesInactivo);

		JLabel lblTiempoParaEliminar = new JLabel("Tiempo para eliminar dispositivo (ms): ");
		lblTiempoParaEliminar.setForeground(Color.WHITE);
		lblTiempoParaEliminar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTiempoParaEliminar.setBounds(166, 188, 313, 19);
		add(lblTiempoParaEliminar);

		tiempoEliminar = new JTextField();
		tiempoEliminar.setColumns(10);
		tiempoEliminar.setBackground(Color.WHITE);
		tiempoEliminar.setBounds(499, 190, 41, 20);
		add(tiempoEliminar);

		Timeout = new JTextField();
		Timeout.setColumns(10);
		Timeout.setBackground(Color.WHITE);
		Timeout.setBounds(887, 128, 41, 20);
		add(Timeout);

		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.setIcon(new ImageIcon(PanelEncabezado.class.getResource("/Imagenes/gas.png")));
		btnActualizar.setBackground(Color.LIGHT_GRAY);
		btnActualizar.setBounds(575, 213, 171, 23);
		btnActualizar.setActionCommand(ACTUALIZAR);
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(mensajesPendiente.getText().equals("") || mensajesInactivo.getText().equals("") || tiempoEliminar.getText().equals("") || Timeout.getText().equals("")){
					JOptionPane.showMessageDialog(null, "Digite los parametros a considerar en el envío de tramas");
				}
				else {
					principal.actualizarParametros(mensajesPendiente.getText(), mensajesInactivo.getText(), tiempoEliminar.getText(), Timeout.getText());
					JOptionPane.showMessageDialog(null, "Se actualizaron los parámetros");
				}
			}
		});
		add(btnActualizar);

		JLabel lblTiempoDeEspera = new JLabel("Tiempo de espera (ms):");
		lblTiempoDeEspera.setForeground(Color.WHITE);
		lblTiempoDeEspera.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTiempoDeEspera.setBounds(731, 128, 144, 19);
		add(lblTiempoDeEspera);

		JButton btnEnviarTramasArp = new JButton("Enviar Tramas ARP");
		btnEnviarTramasArp.setIcon(new ImageIcon(PanelEncabezado.class.getResource("/Imagenes/send-mail.png")));
		btnEnviarTramasArp.setSelectedIcon(new ImageIcon(PanelEncabezado.class.getResource("/Imagenes/send-mail.png")));
		btnEnviarTramasArp.setBackground(Color.LIGHT_GRAY);
		btnEnviarTramasArp.setActionCommand("actu");
		btnEnviarTramasArp.setBounds(575, 247, 171, 23);
		btnEnviarTramasArp.setActionCommand(ENVIAR);
		btnEnviarTramasArp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				enviarTramas = new Thread(new Runnable() {
					public void run() {
							principal.enviarTramas();
					}
				});
				enviarTramas.start();
			}
		});
		add(btnEnviarTramasArp);
	}
}