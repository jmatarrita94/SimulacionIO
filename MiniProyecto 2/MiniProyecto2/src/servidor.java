//import javax.swing.*;

public abstract class servidor	{

int estado;//estado del servidor si activo(1) o inactivo(0)
double tiempoEspera; // tiempo de espera entre clientes.
int contadorClientes; // contador de cuantos clientes ha atendido el servidor en un dia.
double tiempoServicio; //tiempo de servicio de un trabajo.
double tiempoServicioPromedio; //tiempo de servicio promedio en un dia.
double resultado;


public abstract double function();

}
