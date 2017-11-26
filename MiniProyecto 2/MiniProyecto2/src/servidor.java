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
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
