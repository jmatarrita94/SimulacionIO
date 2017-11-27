
public class cliente{
	double tiempoEspera;
	
	public cliente(){
		tiempoEspera = 0;
	}

	public double getTiempoEspera() {
		return tiempoEspera;
	}

	/**
	 * Aumenta el tiempo de espera del cliente en la cola
	 * @param tiempoEspera Cantidad de tiempo que debe aumentar
	 */
	public void setTiempoEspera(double tiempoEspera) {
		this.tiempoEspera += tiempoEspera;
	}	
	
}
