import java.util.*;


public class sistema{
 
	ArrayList<cliente> cola = new ArrayList<cliente>();
	double tiempoSistema = 0;
	double siguienteLlegada = 0;
	ArrayList<servidor> servidores = new ArrayList<servidor>(); 	
	

	public void run() {
		double t = 0;
		while (tiempoSistema < 600) {
			for(servidor s : servidores) {
				
			}
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

	public double calculoTiempoLlegada() {
		double r = Math.random()*10;
		return Math.log((r-1))/-0.5;	
	}
}
