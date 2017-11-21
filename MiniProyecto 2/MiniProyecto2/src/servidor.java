

public abstract class servidor	{

int estado;//estado del servidor si activo(1) o inactivo(0)
double tiempoEsperaTotal; // tiempo de espera total al final del dia.
double tiempoUltimaSalida; //tiempo de la ultima salida para luego calcular tiempo espera.
int contadorClientes; // contador de cuantos clientes ha atendido el servidor en un dia.
double tiempoServicio; //tiempo de servicio de un trabajo.
double tiempoServicioPromedio; //tiempo de servicio promedio en un dia.
double tiempoActual = 0;
double siguienteSalida =-1;
double resultado;
	


public abstract double calculoTiempoServicio();

/*public void actualizarTiempo(double tiempoServicio) {
	tiempoActual += tiempoServicio;
}
*/

public double run (double tiempoActual) {
	if (siguienteSalida == tiempoActual) {
		tiempoUltimaSalida = tiempoActual;
		estado = 0;
		siguienteSalida = -1;
	}
	return siguienteSalida;
}

public void atenderCliente(double tiempoActual) {
	tiempoServicio = this.calculoTiempoServicio();
	contadorClientes++;
	tiempoEsperaTotal += (tiempoActual - tiempoUltimaSalida);
	this.estado = 1;
	siguienteSalida = tiempoActual+tiempoServicio;
	tiempoServicioPromedio += tiempoServicio;
}





}
