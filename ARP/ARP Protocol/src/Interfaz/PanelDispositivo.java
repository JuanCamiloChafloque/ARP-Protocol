package Interfaz;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Modelo.Dispositivo;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;

public class PanelDispositivo extends JPanel {

	private static final long serialVersionUID = 1L;

	private MainInterfaz principal;
	private JTextField ipDispositivo;
	private JTextField macDispositivo;
	private Dispositivo dispositivo;
	private JTextField estadoDispositivo;
	private Thread envioManual;
	private Thread actualizarTipo;
	private static final String MANUAL = "manual";
	private static final String COMBO = "Combo";

	public PanelDispositivo(Dispositivo disp, MainInterfaz pPrincipal) {

		principal = pPrincipal;
		setLayout(null);
		setBackground(new Color(100, 100, 100));
		setBorder(new LineBorder(new Color(10, 10, 10), 3, true));
		setBounds(0, 0, 1370, 187);
		setMinimumSize(new Dimension(1330, 200));
		dispositivo = disp;

		JLabel lblDispositivo = new JLabel("Dispositivo");
		lblDispositivo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDispositivo.setHorizontalAlignment(SwingConstants.CENTER);
		lblDispositivo.setToolTipText("");
		lblDispositivo.setBounds(169, 11, 124, 29);
		add(lblDispositivo);

		JLabel lblImagenDispo = new JLabel("");
		lblImagenDispo.setBounds(1161, 11, 153, 132);						
		lblImagenDispo.setIcon(new ImageIcon(PanelDispositivo.class.getResource("/Imagenes/" + this.dispositivo.getTipoMaquina() + ".png")));
		add(lblImagenDispo);

		JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		comboBox.setForeground(Color.WHITE);
		comboBox.setBounds(326, 15, 129, 20);
		comboBox.setBackground(new Color(100, 100, 100));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Desconocido", "Computador", "Router", "Movil", "Portatil"}));
		comboBox.setSelectedItem(this.dispositivo.getTipoMaquina().toString());
		comboBox.setActionCommand(COMBO);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				actualizarTipo = new Thread(new Runnable() {
					public void run() {
						Object obj = comboBox.getSelectedItem();
						lblImagenDispo.setIcon(new ImageIcon(PanelDispositivo.class.getResource("/Imagenes/" + obj.toString().toLowerCase() + ".png")));
						disp.setTipoMaquina(obj.toString());
						dispositivo.setTipoMaquina(comboBox.getSelectedItem().toString());
					}
				});
				actualizarTipo.start();
			}
		});
		add(comboBox);

		JLabel lblDireccionIp = new JLabel("Direccion IP");
		lblDireccionIp.setToolTipText("");
		lblDireccionIp.setHorizontalAlignment(SwingConstants.CENTER);
		lblDireccionIp.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDireccionIp.setBounds(169, 51, 124, 29);
		add(lblDireccionIp);

		ipDispositivo = new JTextField();
		ipDispositivo.setFont(new Font("Tahoma", Font.BOLD, 11));
		ipDispositivo.setForeground(Color.WHITE);
		ipDispositivo.setBorder(new EmptyBorder(0,5,0,0));
		ipDispositivo.setBackground(new Color(100, 100, 100));
		ipDispositivo.setEditable(false);
		ipDispositivo.setText(this.dispositivo.getIp());
		ipDispositivo.setBounds(326, 55, 129, 20);
		add(ipDispositivo);
		ipDispositivo.setColumns(10);

		JLabel lblDireaccionMac = new JLabel("Direccion MAC");
		lblDireaccionMac.setToolTipText("");
		lblDireaccionMac.setHorizontalAlignment(SwingConstants.CENTER);
		lblDireaccionMac.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDireaccionMac.setBounds(169, 91, 124, 29);
		add(lblDireaccionMac);

		macDispositivo = new JTextField();
		macDispositivo.setEditable(false);
		macDispositivo.setFont(new Font("Tahoma", Font.BOLD, 11));
		macDispositivo.setForeground(Color.WHITE);
		macDispositivo.setBorder(new EmptyBorder(0,5,0,0));
		macDispositivo.setBackground(new Color(100, 100, 100));
		macDispositivo.setColumns(10); 
		macDispositivo.setText(this.dispositivo.getMac());
		macDispositivo.setBounds(326, 95, 129, 20);
		add(macDispositivo);

		JLabel lblEstadoActual = new JLabel("Estado Actual");
		lblEstadoActual.setToolTipText("");
		lblEstadoActual.setHorizontalAlignment(SwingConstants.CENTER);
		lblEstadoActual.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEstadoActual.setBounds(169, 131, 124, 29);
		add(lblEstadoActual);

		estadoDispositivo = new JTextField();
		estadoDispositivo.setEditable(false);
		estadoDispositivo.setFont(new Font("Tahoma", Font.BOLD, 11));
		estadoDispositivo.setForeground(Color.WHITE);
		estadoDispositivo.setBorder(new EmptyBorder(0,5,0,0));
		estadoDispositivo.setBackground(new Color(100, 100, 100));
		estadoDispositivo.setColumns(10);
		estadoDispositivo.setBounds(326, 135, 129, 20);
		estadoDispositivo.setText(this.dispositivo.getEstado());
		add(estadoDispositivo);

		JTextField MensajesPendientes = new JTextField();
		MensajesPendientes.setHorizontalAlignment(SwingConstants.CENTER);
		MensajesPendientes.setEditable(false);
		MensajesPendientes.setFont(new Font("Tahoma", Font.BOLD, 11));
		MensajesPendientes.setForeground(Color.WHITE);
		MensajesPendientes.setBorder(new EmptyBorder(0,5,0,0));
		MensajesPendientes.setBackground(new Color(100, 100, 100));
		MensajesPendientes.setText("" + disp.getCantPendiente());
		MensajesPendientes.setBounds(712, 15, 86, 20);
		add(MensajesPendientes);
		MensajesPendientes.setColumns(10);

		JTextField MensajesInactivo = new JTextField();
		MensajesInactivo.setHorizontalAlignment(SwingConstants.CENTER);
		MensajesInactivo.setEditable(false);
		MensajesInactivo.setFont(new Font("Tahoma", Font.BOLD, 11));
		MensajesInactivo.setForeground(Color.WHITE);
		MensajesInactivo.setBorder(new EmptyBorder(0,5,0,0));
		MensajesInactivo.setBackground(new Color(100, 100, 100));
		MensajesInactivo.setText("" + disp.getCantInactivo());
		MensajesInactivo.setColumns(10);
		MensajesInactivo.setBounds(712, 55, 86, 20);
		add(MensajesInactivo);

		JButton btnArpManual = new JButton("ARP Manual");
		btnArpManual.setIcon(new ImageIcon(PanelDispositivo.class.getResource("/Imagenes/send-mail.png")));
		btnArpManual.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnArpManual.setBorder(new LineBorder(new Color(0,0,0)));
		btnArpManual.setForeground(Color.WHITE);
		btnArpManual.setFocusTraversalKeysEnabled(false);
		btnArpManual.setFocusPainted(false);
		btnArpManual.setBackground(new Color(100, 100, 100));
		btnArpManual.setBounds(1161, 154, 124, 23);
		btnArpManual.setActionCommand(MANUAL);
		btnArpManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				envioManual = new Thread(new Runnable() {
					public void run() {
						principal.enviarTramaIndividual(ipDispositivo.getText());
						MensajesPendientes.setText(""+disp.getCantPendiente());
						MensajesInactivo.setText(""+disp.getCantInactivo());
						estadoDispositivo.setText(disp.getEstado());
					}
				});
				envioManual.start();
			}
		});
		add(btnArpManual);

		JLabel lblPaquetesArpPara = new JLabel("Paquetes ARP para PENDIENTE");
		lblPaquetesArpPara.setToolTipText("");
		lblPaquetesArpPara.setHorizontalAlignment(SwingConstants.CENTER);
		lblPaquetesArpPara.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPaquetesArpPara.setBounds(508, 11, 187, 29);
		add(lblPaquetesArpPara);

		JLabel lblPaquetesArpPara_1 = new JLabel("Paquetes ARP para INACTIVO");
		lblPaquetesArpPara_1.setToolTipText("");
		lblPaquetesArpPara_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPaquetesArpPara_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPaquetesArpPara_1.setBounds(508, 51, 187, 29);
		add(lblPaquetesArpPara_1);

	}

	public String getMAC() {
		return this.macDispositivo.getText();
	}

	public String getIP() {
		return this.ipDispositivo.getText();
	}
}
