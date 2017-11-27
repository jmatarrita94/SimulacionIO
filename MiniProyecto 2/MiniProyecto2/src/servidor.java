import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class servidor	{

	double tiempoEsperaTotal=0; // tiempo de espera total al final del dia.
	double tiempoUltimaSalida=0; //tiempo de la ultima salida para luego calcular tiempo espera.
	int contadorClientes=0; // contador de cuantos clientes ha atendido el servidor en un dia.
	double tiempoServicio=0; //tiempo de servicio de un trabajo.
	double tiempoServicioPromedio=0; //tiempo de servicio promedio en un dia.
	double tiempoActual = 0;
	double resultado=0;
	
	/**
	 * Calcula el tiempo que tomara realizar el servicio
	 * @return El tiempo que el servidor durara realizando un servicio
	 */
	public abstract double calculoTiempoServicio();
	
	/**
	 * Inicio del servicio
	 * @param tiempoActual2 Tiempo actual del sistema
	 * @return Tiempo que durara el servicio
	 */
	public double atenderCliente(double tiempoActual2) {
		this.tiempoServicio = this.calculoTiempoServicio();
		this.contadorClientes++;
		this.tiempoActual = tiempoActual2;
		this.tiempoEsperaTotal += (this.tiempoActual - tiempoUltimaSalida);
		this.tiempoServicioPromedio += tiempoServicio;
		return tiempoServicio;
	}
	
	/**
	 * Fin del servicio
	 * @param tiempoActual2 Tiempo actual del sistema
	 */
	public void servirCliente(double tiempoActual2){
		this.tiempoActual = tiempoActual2;
		this.tiempoUltimaSalida = tiempoActual2;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
