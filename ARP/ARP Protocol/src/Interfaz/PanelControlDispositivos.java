package Interfaz;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import Modelo.Dispositivo;
import net.miginfocom.swing.MigLayout;

public class PanelControlDispositivos extends JScrollPane{

	private MainInterfaz principal;
	private static final long serialVersionUID = 1L;
	private static long COUNT = 0;
	private JPanel panelDevice;
	/**
	 * Create the panel.
	 */
	public PanelControlDispositivos(MainInterfaz pPrincipal) {

		principal = pPrincipal;
		setWheelScrollingEnabled(false);
		panelDevice = new JPanel();
		panelDevice.setBackground(new Color(0, 0, 0));
		panelDevice.setLayout(new MigLayout("", "[grow]", "[]"));
		setViewportView(panelDevice);
		setMinimumSize(new Dimension(1370, 200));

	}

	public void agregarDispositivo(Dispositivo dispositivo) {
		PanelDispositivo panel = new PanelDispositivo(dispositivo, principal);
		panelDevice.add(panel, "cell 0 " + COUNT++ + ",grow");
		panelDevice.revalidate();
	}

	public void removerDispositivo(Dispositivo disp) {
		for(Component c : this.panelDevice.getComponents()) {
			if(c instanceof PanelDispositivo) {
				PanelDispositivo panelDisp = (PanelDispositivo) c;
				if((panelDisp.getMAC().equals(disp.getMac()) && (panelDisp.getIP().equals(disp.getIp())))) {
					panelDevice.remove(panelDisp);
					panelDevice.repaint();
				}
			}
		}
	}

}
