

public abstract class servidor	{

	double tiempoEsperaTotal; // tiempo de espera total al final del dia.
	double tiempoUltimaSalida; //tiempo de la ultima salida para luego calcular tiempo espera.
	int contadorClientes; // contador de cuantos clientes ha atendido el servidor en un dia.
	double tiempoServicio; //tiempo de servicio de un trabajo.
	double tiempoServicioPromedio; //tiempo de servicio promedio en un dia.
	double tiempoActual = 0;
	double resultado;
		
	
	
	public abstract double calculoTiempoServicio();
	
	
//	public double run (double tiempoActual) {
//		if (siguienteSalida == tiempoActual) {
//			tiempoUltimaSalida = tiempoActual;
//			siguienteSalida = -1;
//		}
//		return siguienteSalida;
//	}
	
	public double atenderCliente(double tiempoActual2) {
		this.tiempoServicio = this.calculoTiempoServicio();
		this.contadorClientes++;
		this.tiempoActual = tiempoActual2;
		this.tiempoEsperaTotal += (this.tiempoActual - tiempoUltimaSalida);
		//this.siguienteSalida = tiempoActual+tiempoServicio;
		this.tiempoServicioPromedio += tiempoServicio;
		return tiempoServicio;
	}
	
	public void servirCliente(double tiempoActual2){
		this.tiempoActual = tiempoActual2;
		this.tiempoUltimaSalida = tiempoActual2;
	}
}
