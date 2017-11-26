
public class cliente{
	double tiempoEspera;
	
	public cliente(){
		tiempoEspera = 0;
	}

	public double getTiempoEspera() {
		return tiempoEspera;
	}

	public void setTiempoEspera(double tiempoEspera) {
		this.tiempoEspera += tiempoEspera;
	}	
	
}
