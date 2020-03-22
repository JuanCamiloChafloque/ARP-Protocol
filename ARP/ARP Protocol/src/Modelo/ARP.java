package Modelo;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

import Interfaz.MainInterfaz;
import jpcap.*;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;

public class ARP {

	private static final Inet4Address IP = getIpLocal();
	private static final byte[] MAC = getMac(IP);
	private static final Inet4Address MASK = getMascaraSubRed(IP);
	private static final Inet4Address SUBNET = getSubred(IP, MASK);
	private static final int TAM_MASK = getTamMascara(MASK);
	private static final int HOST = getHost(TAM_MASK);
	private int timeout;
	private Map<String,Dispositivo> dispositivos;
	private int pendiente;
	private int inactivo;
	private int tiempo;
	private int actualizar;
	private Thread remover;
	private MainInterfaz inter;
	private String[] listaIps;

	public ARP(int pInactivo, int pPendiente, int pTiempo, int pActualizar, MainInterfaz pInter) {

		this.inter = pInter;
		this.dispositivos = new Hashtable<String,Dispositivo>();
		this.inactivo = pInactivo;
		this.pendiente = pPendiente;
		this.tiempo = pTiempo;
		this.timeout = 6000;
		this.actualizar = pActualizar;
		this.listaIps = new String[HOST + 1];
		try {
			generarIPs();
		} catch (UnknownHostException e) {
			System.out.println("Error generando las IPs. " + e.getMessage());
		}
	}

	public boolean crearMensajeARP(Inet4Address ip) throws IOException {

		jpcap.NetworkInterface[] disp = JpcapCaptor.getDeviceList();
		jpcap.NetworkInterface dispActual = null;
		loop:
			for(jpcap.NetworkInterface actual: disp) {
				for(jpcap.NetworkInterfaceAddress addr: actual.addresses) {

					if (!(addr.address instanceof Inet4Address)) {
						continue;
					}
					byte[] ipDes = ip.getAddress();
					byte[] masc = addr.subnet.getAddress();
					byte[] ipOr = addr.address.getAddress();
					for (int i = 0; i < 4; i++) {
						ipDes[i] = (byte) (ipDes[i] & masc[i]);
						ipOr[i] = (byte) (ipOr[i] & masc[i]);
					}
					if (Arrays.equals(ipDes, ipOr)) {
						dispActual = actual;
						break loop;
					}
				}
			}

		if(dispActual == null) {
			throw new IllegalArgumentException("Error encontrando la dirección " + ip.getHostAddress());
		}

		JpcapCaptor captor = JpcapCaptor.openDevice(dispActual, 2000, false, 3000);
		captor.setFilter("arp", true);
		JpcapSender send = captor.getJpcapSenderInstance();

		byte[] broadcast = new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255};

		ARPPacket arp = new ARPPacket();
		arp.hardtype = ARPPacket.HARDTYPE_ETHER;
		arp.prototype = ARPPacket.PROTOTYPE_IP;
		arp.operation = ARPPacket.ARP_REQUEST;
		arp.hlen = 6;
		arp.plen = 4;
		arp.sender_hardaddr = MAC;
		arp.sender_protoaddr = IP.getAddress();
		arp.target_hardaddr = broadcast;
		arp.target_protoaddr = ip.getAddress();

		EthernetPacket ethernet = new EthernetPacket();
		ethernet.frametype = EthernetPacket.ETHERTYPE_ARP;
		ethernet.src_mac = MAC;
		ethernet.dst_mac = broadcast;

		arp.datalink = ethernet;
		send.sendPacket(arp);		
		System.out.println("Sending ARP message to " + ip.getHostAddress() + "...");

		while(true) {

			ARPPacket respuesta = (ARPPacket) captor.getPacket();
			if(respuesta == null) {

				System.out.println("--->" + ip.getHostAddress() + " did not respond...");

				if(this.dispositivos.containsKey(ip.getHostAddress())) {

					Dispositivo d = this.dispositivos.get(ip.getHostAddress());
					if(d.getEstado().equalsIgnoreCase("Activo")) {
						this.dispositivos.get(ip.getHostAddress()).setCantPendiente(d.getCantPendiente() + 1);
						if(this.dispositivos.get(ip.getHostAddress()).getCantPendiente() == pendiente) {
							this.dispositivos.get(ip.getHostAddress()).setEstado("Pendiente");
						}
					}
					else if(d.getEstado().equalsIgnoreCase("Pendiente")) {
						this.dispositivos.get(ip.getHostAddress()).setCantInactivo(d.getCantInactivo() + 1);
						if(this.dispositivos.get(ip.getHostAddress()).getCantInactivo() == inactivo) {
							this.dispositivos.get(ip.getHostAddress()).setEstado("Inactivo");
							contarTiempo(this.dispositivos.get(ip.getHostAddress()));			
						}

					}
				}
				captor.close();
				return true;

			}
			else if(Arrays.equals(respuesta.sender_protoaddr, ip.getAddress())) {

				System.out.println("--->" + ip.getHostAddress() + " responded...");

				if(this.dispositivos.containsKey(ip.getHostAddress())) {
					this.dispositivos.get(ip.getHostAddress()).setEstado("Activo");
					this.dispositivos.get(ip.getHostAddress()).setCantPendiente(0);;
					this.dispositivos.get(ip.getHostAddress()).setCantInactivo(0);
					this.dispositivos.get(ip.getHostAddress()).setTiempo(0);
					inter.agregarDispositivo(this.dispositivos.get(ip.getHostAddress()));
				}
				else {
					byte[] macDisp = respuesta.sender_hardaddr;
					String stringMAC = bytesAString(macDisp, ":");
					Dispositivo nuevoDisp = new Dispositivo(ip.getHostAddress(), stringMAC);
					this.dispositivos.put(ip.getHostAddress(), nuevoDisp);
					inter.agregarDispositivo(nuevoDisp);
				}
				imprimirDispositivo(ip.getHostAddress());
				captor.close();
				return true;
			}
		}
	}

	public void generarIPs() throws UnknownHostException {

		byte[] ip = SUBNET.getAddress();
		Inet4Address dir;
		dir = (Inet4Address) Inet4Address.getByAddress(ip);
		this.listaIps[0] = dir.getHostAddress();
		for(int i = 1; i < 7; i++) {
			ip[3] = (byte) i;
			dir = (Inet4Address) Inet4Address.getByAddress(ip);
			this.listaIps[i] = dir.getHostAddress();			
		}
	}

	public void enviarMesajesARP() throws UnknownHostException, IOException {

		for(int i = 1; i < 7; i++) {
			Inet4Address ip = (Inet4Address) Inet4Address.getByName(this.listaIps[i]);
			System.out.println("Checking " + ip.getHostAddress() + "...");
			if(ip.isReachable(this.timeout))
				crearMensajeARP(ip);
			else
				System.out.println("--->" + ip.getHostAddress() + " did not respond in " + this.timeout + "ms...");
		}
	}

	public void enviarMensajeManual(String pIp) throws IOException {

		Inet4Address ip = (Inet4Address) Inet4Address.getByName(pIp);
		System.out.println("Checking " + ip.getHostAddress() + "...");
		if(ip.isReachable(this.timeout)) {
			crearMensajeARP(ip);
		}
		else {
			System.out.println("--->" + ip.getHostAddress() + " did not respond in " + this.timeout + "ms...");
			if(this.dispositivos.containsKey(ip.getHostAddress())) {

				Dispositivo d = this.dispositivos.get(ip.getHostAddress());
				if(d.getEstado().equalsIgnoreCase("Activo")) {
					this.dispositivos.get(ip.getHostAddress()).setCantPendiente(d.getCantPendiente() + 1);
					if(this.dispositivos.get(ip.getHostAddress()).getCantPendiente() == pendiente) {
						this.dispositivos.get(ip.getHostAddress()).setEstado("Pendiente");
					}
				}
				else if(d.getEstado().equalsIgnoreCase("Pendiente")) {
					this.dispositivos.get(ip.getHostAddress()).setCantInactivo(d.getCantInactivo() + 1);
					if(this.dispositivos.get(ip.getHostAddress()).getCantInactivo() == inactivo) {
						this.dispositivos.get(ip.getHostAddress()).setEstado("Inactivo");
						contarTiempo(this.dispositivos.get(ip.getHostAddress()));
					}

				}
			}
		}
	}

	public void imprimirDispositivo(String key) {

		Dispositivo disp = this.dispositivos.get(key);
		System.out.println("------>Device IP Address: " + key + " || Device MAC Address: " + disp.getMac());
	}

	public static Inet4Address getIpLocal() {
		try {
			return (Inet4Address) Inet4Address.getLocalHost();
		} catch (UnknownHostException e) {
			System.out.println("Error obteniendo la dirección IP. " + e.getMessage());
		}
		return null;
	}

	public static byte[] getMac(Inet4Address ip) {
		try {
			return NetworkInterface.getByInetAddress(ip).getHardwareAddress();
		} catch (SocketException e) {
			System.out.println("Error obteniendo la dirección MAC. " + e.getMessage());
		}
		return null;
	}

	public static Inet4Address getMascaraSubRed(Inet4Address ip) {

		try {
			NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ip);
			int shft = 0xffffffff<<(32- 
					networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength());
			int oct1 = ((byte) ((shft&0xff000000)>>24)) & 0xff;
			int oct2 = ((byte) ((shft&0x00ff0000)>>16)) & 0xff;
			int oct3 = ((byte) ((shft&0x0000ff00)>>8)) & 0xff;
			int oct4 = ((byte) (shft&0x000000ff)) & 0xff;
			String mascara = oct1+"."+oct2+"."+oct3+"."+oct4;
			return (Inet4Address) Inet4Address.getByName(mascara);

		}catch (Exception e) {
			System.out.println("Error obteniendo la mascara de red. " + e.getMessage());
		}
		return null;
	}

	public static int getHost(int masc) {
		int host = (int) Math.pow(2, Integer.SIZE - masc) - 2;
		return host;
	}

	public static Inet4Address getSubred(Inet4Address pIp, Inet4Address masc) {
		try {
			byte[] mascara = masc.getAddress();
			byte[] ip = pIp.getAddress();

			for(int i = 0; i < 4; i++) {
				ip[i] = (byte)(ip[i] & mascara[i]);
			}
			return (Inet4Address) Inet4Address.getByAddress(ip);
		}catch(Exception e) {
			System.out.println("Error generando la subred IP. " + e.getMessage());
		}
		return null;
	}

	public static int getTamMascara(Inet4Address masc) {
		try {
			int tam = -1;
			int mascara = convertirOctetoaEntero(masc.getHostAddress());
			int host = (int) (Integer.SIZE - (Math.log((tam ^ mascara) + 1) / Math.log(2)));
			return host;
		} catch (Exception e) {
			System.out.println("Error obteniendo el tamaño de la mascara de subred. " + e.getMessage());
		}	
		return -1;
	}

	public static int convertirOctetoaEntero(String ipDir) {
		String[] ip = ipDir.split("\\.|/");
		int oct1 = Integer.parseInt(ip[0]);
		int oct2 = Integer.parseInt(ip[1]);
		int oct3 = Integer.parseInt(ip[2]);
		int oct4 = Integer.parseInt(ip[3]);
		int bin = oct1;
		bin = (bin << 8) + oct2;
		bin = (bin << 8) + oct3;
		bin = (bin << 8) + oct4;
		return bin;
	}

	public static String bytesAString(byte[] bMAC, String sep) {

		String stringMAC = "";
		int n = 1;
		for (byte b : bMAC) {
			String bt = "" + Integer.toHexString(b & 0xff);
			stringMAC += ((bt.length() == 1) ? "0" : "" ) + bt;
			stringMAC += ((n < bMAC.length) ? sep : "");
			n++;
		}
		return stringMAC;
	}

	public void contarTiempo(Dispositivo disp) {
		remover = new Thread(new Runnable() {
			public void run() {
				try {
					System.out.println("Eliminando dispositivo " + disp.getIp() + "...");
					int t = timeout;
					while(t-- > 0 && disp.getEstado() == "Inactivo") {

					}
					Thread.sleep(200);
					if(disp.getEstado() == "Inactivo") {
						dispositivos.remove(disp.getIp());
						inter.removerDispositivo(disp);
						System.out.println("Se eliminó el dispositivo " + disp.getIp() + "...");
					}
				} catch (InterruptedException evento) {
					System.out.println("No se pudo eliminar el dispositivo con ID: " + disp.getIp());
				}
			}
		});
		remover.start();	
	}

	public int getPendiente() {
		return this.pendiente;
	}

	public void setPendiente(int pPendiente) {
		this.pendiente = pPendiente;
	}

	public int getInactivo() {
		return this.inactivo;
	}

	public void setInactivo(int pInactivo) {
		this.inactivo = pInactivo;
	}

	public int getTiempo() {
		return this.tiempo;
	}

	public void setTiempo(int pTiempo) {
		this.tiempo = pTiempo;
	}

	public int getActualizar() {
		return this.actualizar;
	}

	public void setActualizar(int pActualizar) {
		this.actualizar = pActualizar;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public void setTimeout(int pTimeout) {
		this.timeout = pTimeout;
	}

	public String[] getListaIps() {
		return this.listaIps;
	}

	public void setListaIps(String[] pLista) {
		this.listaIps = pLista;
	}

	public Map<String, Dispositivo> getDispositivos(){
		return this.dispositivos;
	}

	public void setDispositivos(Map<String, Dispositivo> pDispositivos) {
		this.dispositivos = pDispositivos;
	}
}
