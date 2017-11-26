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
		s1g = new servidorGamma(3,7);
		s2g = new servidorGamma(2,5);
		s3e = new servidorExponencial(0.3);
		s4u = new servidorUniforme(4,9);
		for(int i = 0; i<4; i++){
			this.estadoServidores[i] = false;
		}
	}

	public void run() {
		this.siguienteLlegada = this.calculoTiempoLlegada();
		double t = 0;
		servidor servidorMenorTiempo;
		while (tiempoSistema < 600) {
			servidorMenorTiempo = this.obtenerTiempoMenorServidor();
			if(servidorMenorTiempo != null && cola.isEmpty()){
				//HAY SERVIDORES EN SERVICIO Y LA COLA ESTA VACIA
				t = tiempoServidores[this.servidorEntero(servidorMenorTiempo)];
				if (t < this.siguienteLlegada){
					//SE TOMA EL SIGUIENTE SERVICIO
					this.manejoSalida(servidorMenorTiempo, t);
				}else{
					//SE TOMA LA SIGUIENTE LLEGADA
					this.manejoLlegada(this.siguienteLlegada);
					this.siguienteLlegada = this.calculoTiempoLlegada();
				}
			}else if(servidorMenorTiempo != null && !cola.isEmpty()){
				//HAY SERVIDORES EN SERVICIO Y HAY GENTE EN COLA
				t = tiempoServidores[this.servidorEntero(servidorMenorTiempo)];
				cliente clientePos1 = cola.peek();
				if(clientePos1.tiempoEspera<=t && clientePos1.tiempoEspera >=6){
					this.tiempoDeEsperaEnCola += clientePos1.tiempoEspera;
					this.siguienteLlegada -= clientePos1.tiempoEspera;
					this.cola.poll();
					this.clientesFuera++;
					this.clientesEnCola--;
					
				}else{
					if (t < this.siguienteLlegada){
						//SE TOMA EL SIGUIENTE SERVICIO
						this.manejoSalida(servidorMenorTiempo, t);
						this.cola.poll();
						this.manejoLlegada(0);
					}else{
						//SE TOMA LA SIGUIENTE LLEGADA
						this.manejoLlegada(this.siguienteLlegada);
						this.siguienteLlegada = this.calculoTiempoLlegada();
					}
				}	
			}else{
				//NO HAY SERVIDORES EN SERVICIO
				this.manejoLlegada(this.siguienteLlegada);
				this.siguienteLlegada = this.calculoTiempoLlegada();
			}
			this.imprimirEstado();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while(!cola.isEmpty() || this.hayServidoresActivos()){
			//SE CIERRAN LAS OFICINAS
			servidorMenorTiempo = this.obtenerTiempoMenorServidor();
			if(servidorMenorTiempo != null && cola.isEmpty()){
				//HAY SERVIDORES EN SERVICIO Y LA COLA ESTA VACIA
				t = tiempoServidores[this.servidorEntero(servidorMenorTiempo)];
				//SE TOMA EL SIGUIENTE SERVICIO
				this.manejoSalida(servidorMenorTiempo, t);
			}else if(servidorMenorTiempo != null && !cola.isEmpty()){
				//HAY SERVIDORES EN SERVICIO Y HAY GENTE EN COLA
				t = tiempoServidores[this.servidorEntero(servidorMenorTiempo)];
				cliente clientePos1 = cola.peek();
				if(clientePos1.tiempoEspera<=t && clientePos1.tiempoEspera >=6){
					this.tiempoDeEsperaEnCola += clientePos1.tiempoEspera;
					this.siguienteLlegada -= clientePos1.tiempoEspera;
					this.cola.poll();
					this.clientesFuera++;
					this.clientesEnCola--;
				}else{
					//SE TOMA EL SIGUIENTE SERVICIO
					this.manejoSalida(servidorMenorTiempo, t);
					this.cola.poll();
					this.manejoLlegada(0);
				}	
			}
			this.imprimirEstado();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void manejoLlegada(double tiempoLlegada) {
		cliente clienteTemp = new cliente();
		for(int i = 0; i<4; i++){
			if(this.estadoServidores[i]){
				this.tiempoServidores[i] -= tiempoLlegada;
			}
		}
		if(!this.cola.isEmpty()){
			for(cliente item : this.cola){
			    item.setTiempoEspera(tiempoLlegada);
			}
		}
		this.tiempoSistema += tiempoLlegada;
		this.siguienteLlegada -= tiempoLlegada;
		if(!this.asignarServidor(this.tiempoSistema)){
			this.cola.add(clienteTemp);
			this.clientesEnCola++;
		}else{
			this.clientesAtendidos++;
		}
		this.clientesPorDía++;
	}
	
	private void manejoSalida(servidor serv, double tiempoSalida){
		for(int i = 0; i<4; i++){
			if(this.estadoServidores[i]){
				this.tiempoServidores[i] -= tiempoSalida;
			}
		}
		if(!this.cola.isEmpty()){
			for(cliente item : this.cola){
			    item.setTiempoEspera(tiempoSalida);
			}
		}
		this.tiempoSistema += tiempoSalida;
		this.siguienteLlegada -= tiempoSalida;
		this.desasignarServidor(this.servidorEntero(serv));
		
	}

	private double calculoTiempoLlegada() {
		double res;
		do{
			double r = Math.random()*2;
			res = Math.log((r-1))/-0.5;
		}while(Double.isNaN(res));
		if(res<0){
			return -s1g.round(res,2);
		}else{
			return s1g.round(res , 2);
		}	
	}
	
	private boolean asignarServidor(double tiempo){
		boolean asignado = false;
		for(int i=0; i<4; i++){
			if(!estadoServidores[i]){
				estadoServidores[i] = true;				
				tiempoServidores[i] = this.enteroServidor(i).atenderCliente(tiempo);
				asignado = true;
				i = 4;
			}
		}
		return asignado;
	}
	
	private void desasignarServidor(int servidor){
		estadoServidores[servidor] = false;
		tiempoServidores[servidor] = -1;
		this.enteroServidor(servidor).servirCliente(this.tiempoSistema);
	}
	
//	private void actualizarTiempos(double tiempo){
//		this.siguienteLlegada -= tiempo;
//		for(cliente item : this.cola){
//		    item.setTiempoEspera(tiempo);
//		}
//		for(int i = 0; i<4; i++){
//			if(this.estadoServidores[i]){
//				this.tiempoServidores[i] -= tiempo;
//			}
//		}
//		this.tiempoSistema += tiempo;
//	}
	
	
	private servidor obtenerTiempoMenorServidor(){
		servidor siguienteServidor = null;
		double tiempoMenor = 10000000;
		for(int i = 0; i<4; i++){
			if(this.estadoServidores[i] && tiempoMenor>this.tiempoServidores[i]){
				tiempoMenor = this.tiempoServidores[i];
				siguienteServidor = this.enteroServidor(i);
			}
		}
		return siguienteServidor;
	}
	
	private servidor enteroServidor(int tipo){
		if(tipo == 0 ){
			return s1g;
		}
		else if(tipo == 1){
			return s2g;
		}
		else if(tipo == 2){
			return s3e;
		}
		else{
			return s4u;
		}
	}
	
	private int servidorEntero(servidor serv){
		if (serv == s1g) {
			return 0;
		}else if (serv == s2g){
			return 1;
		}else if (serv == s3e){
			return 2;
		}else if (serv == s4u){
			return 3;
		} 
		return -1;
	}
	
	private void imprimirEstado(){
		System.out.println("----------------------------------------0----------------------------------------");
		System.out.println("TIEMPO DEL SISTEMA: "+ s1g.round((this.tiempoSistema),1));
		System.out.println("PROXIMA LLEGADA AL SISTEMA: "+s1g.round(this.siguienteLlegada,2));
		System.out.println("SERVIDORES: ");
		for(int i = 0; i<4; i++){
			if(this.estadoServidores[i]){
				System.out.println("El servidor "+ i +" servirá en " + s1g.round(this.tiempoServidores[i],2)+" minutos.");
			}else{
				System.out.println("El servidor "+ i +" está disponible y sin servicio." );
			}
		}
		System.out.println("COLA: ");
		int i = 1;
		for(cliente c : this.cola) {
			System.out.println("El cliente en la posición: "+ i +" espera hace "+s1g.round(c.tiempoEspera,1)+" minutos." );
			i++;
	    }
		System.out.println("----------------------------------------0----------------------------------------");
	}
	
	private boolean hayServidoresActivos(){
		for(int i = 0; i<4; i++){
			if(this.estadoServidores[i]){
				return true;
			}
		}
		return false;
	}
}
