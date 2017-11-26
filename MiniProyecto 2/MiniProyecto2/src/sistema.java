import java.util.LinkedList;
import java.util.Queue;


public class sistema{
 
	Queue<cliente> cola = new LinkedList<cliente>();
	double tiempoSistema = 0;
	double siguienteLlegada = 0;
	boolean[] estadoServidores = new boolean[4];
	double[] tiempoServidores = new double[4];
	int clientesPorDía; //Cantidad total de llegadas
	int clientesAtendidos; // Cantidad total de llegadas atentidas
	int clientesFuera; //Cantidad total de llegadas no atendidas
	int clientesEnCola; // Cantidad total de llegadas a la cola
	double tiempoDeEsperaEnCola; //Minutos sumados de espera en la cola (para sacar promedio)
	servidorGamma s1g;
	servidorGamma s2g;
	servidorExponencial s3e;
	servidorUniforme s4u;
	
	
	public sistema(){
		this.siguienteLlegada = this.calculoTiempoLlegada();
		s1g = new servidorGamma(3,7);
		s2g = new servidorGamma(2,5);
		s3e = new servidorExponencial(0.3);
		s4u = new servidorUniforme(4,9);
		for(int i = 0; i<4; i++){
			this.estadoServidores[i] = false;
		}
	}

	public void run() {
		double t = 0;
		t = this.siguienteLlegada;
		/*
		 *Primero: -revisar todos los tiempos de los servidores (solo si el estado es activo) 
		 *Segundo: -revisar la próxima llegada 
		 *Tercero: -revisar la primer persona en cola 
		 *Cuarto: Tomar el menor y proceder con la acción (llegada_cola-llegada_servidor-salida) 
		 *Quinto: Actualizar tiempos y servidores; 
		 *Sexto:  Solicitar nueva llegada
		 *Séptimo: repetir 
		 *Octavo: acabar los atendidos y la cola 
		 *NOTA: TOMAR INFO EN 603
		 *
		 *Por último: Correr la simulación 1000 veces, y sacar promedios
		 */
		while (tiempoSistema < 600) {
			
		}
	}
	
	public void manejoLlegada() {
		cliente clienteTemp = null;
		if(this.tiempoSistema == this.siguienteLlegada){
			this.siguienteLlegada = this.tiempoSistema + this.calculoTiempoLlegada();
			//hay que agregar cliente a la cola y el tiempo de llegada que seria el tiempoActual.
			//esto de abajo va en otro if
			//se calcula cuantos servidores estan vacios y se ve si el cliente tiene menos de 6 minutos y se manda
			//a un servidor vacio.
			
		}
	}

	private double calculoTiempoLlegada() {
		double r = Math.random()*10;
		return Math.log((r-1))/-0.5;	
	}
	
	private boolean asignarServidor(double tiempo){
		boolean asignado = false;
		for(int i=0; i<4; i++){
			if(!estadoServidores[i]){
				estadoServidores[i] = true;
				if(i == 0 ){
					tiempoServidores[i] = s1g.atenderCliente(tiempo);
				}
				else if(i == 1){
					tiempoServidores[i] = s2g.atenderCliente(tiempo);
				}
				else if(i == 2){
					tiempoServidores[i] = s3e.atenderCliente(tiempo);
				}
				else{
					tiempoServidores[i] = s4u.atenderCliente(tiempo);
				}
				asignado = true;
				i = 4;
			}
		}
		return asignado;
	}
	
	private void desasignarServidor(int servidor, double tiempo){
		estadoServidores[servidor] = false;
		if(servidor == 0 ){
			s1g.servirCliente(tiempo);
		}
		else if(servidor == 1){
			s2g.servirCliente(tiempo);
		}
		else if(servidor == 2){
			s3e.servirCliente(tiempo);
		}
		else{
			s4u.servirCliente(tiempo);
		}
	}
	
	public void actualizarTiempos(double tiempo){
		this.siguienteLlegada += tiempo;
		actualizarTiemposCola(tiempo);
		actualizarTiemposServidores(tiempo);
	}
	
	private void actualizarTiemposCola(double tiempo){
		for(cliente item : this.cola){
		    item.setTiempoEspera(tiempo);
		}
	}
	
	private void actualizarTiemposServidores(double tiempo){
		for(int i = 0; i<4; i++){
			if(this.estadoServidores[i]){
				this.tiempoServidores[i] -= tiempo;
			}
		}
	}
}
