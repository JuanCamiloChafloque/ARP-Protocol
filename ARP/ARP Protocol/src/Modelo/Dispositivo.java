package Modelo;

public class Dispositivo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String ip;
	private String mac;
	private String tipoMaquina;
	private String estado;
	private int cantInactivo;
	private int cantPendiente;
	private int tiempo;
	private boolean visible;

	public Dispositivo(String pIp, String pMac) {
		this.ip = pIp;
		this.mac = pMac;
		this.cantInactivo = 0;
		this.cantPendiente = 0;
		this.tiempo = 0;
		this.tipoMaquina = "Desconocido";
		this.estado = "Activo";
		this.visible = true;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String pIp) {
		this.ip = pIp;
	}

	public String getMac() {
		return this.mac;
	}

	public void setMac(String pMac) {
		this.mac = pMac;
	}

	public String getTipoMaquina() {
		return this.tipoMaquina;
	}

	public void setTipoMaquina(String pTipo) {
		this.tipoMaquina = pTipo;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String pEstado) {
		this.estado = pEstado;
	}

	public int getCantPendiente() {
		return this.cantPendiente;
	}

	public void setCantPendiente(int pPendiente) {
		this.cantPendiente = pPendiente;
	}

	public int getCantInactivo() {
		return this.cantInactivo;
	}

	public void setCantInactivo(int pInactivo) {
		this.cantInactivo = pInactivo;
	}

	public int getTiempo() {
		return this.tiempo;
	}

	public void setTiempo(int pTiempo) {
		this.tiempo = pTiempo;
	}

	public boolean getVisible() {
		return this.visible;
	}

	public void setVisible(boolean pVisible) {
		this.visible = pVisible;
	}
}
