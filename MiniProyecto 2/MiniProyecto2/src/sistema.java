import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;


public class sistema{
 
	Queue<cliente> cola = new LinkedList<cliente>();
	double tiempoSistema = 0;
	double siguienteLlegada = 0;
	boolean[] estadoServidores = new boolean[4];
	double[] tiempoServidores = new double[4];
	int clientesPorDía = 0; //Cantidad total de llegadas
	int clientesAtendidos = 0; // Cantidad total de llegadas atentidas
	int clientesFuera = 0; //Cantidad total de llegadas no atendidas
	int totalClientesEnCola = 0; // Cantidad total de llegadas a la cola
	int clientesEnCola = 0; // Cantidad de clientes en la cola en el momento actual
	int tamColaMasGrande = 0; // Tamano maximo de la cola
	int clientesEsperan6Min = 0; // Cantidad de clientes que se salieron de la cola por que esperan 6 minutos
	double tiempoDeEsperaEnCola = 0; //Minutos sumados de espera en la cola (para sacar promedio)
	int clientesEnColaEnLLegada = 0; //Numero de clientes en cola en llegadas
	int num1Servidor = 0; // Numero de veces en que almenos 1 servidor esta disponible en llegadas
	int num2Servidor = 0; // Numero de veces en que almenos 2 servidores estan disponibles en llegadas
	int clientesEnSistema603 = 0; // Numero de clientes en el sistema a las 6:03
	double tiempoEsperaMaximo = 0; // Tiempo de espera maximo en la cola
	double tiempoRespuesta = 0; // Tiempo total que 
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
				while(!cola.isEmpty() && cola.peek().tiempoEspera >= 6) { // Saca los clientes que han esperado 6 minutos
					this.tiempoDeEsperaEnCola += 6;
					this.cola.poll();
					--this.clientesEnCola;
					++this.clientesEsperan6Min;
				}
				if (t < this.siguienteLlegada){
					//SE TOMA EL SIGUIENTE SERVICIO
					this.manejoSalida(servidorMenorTiempo, t);
					if (this.clientesEnCola > 0) {
						double tiempoC = this.cola.poll().tiempoEspera;
						if(tiempoC > this.tiempoEsperaMaximo) {
							this.tiempoEsperaMaximo = tiempoC;
						}
						this.tiempoDeEsperaEnCola += tiempoC;
						--this.clientesEnCola;
						this.manejoLlegada(0);
					}
				}else{
					//SE TOMA LA SIGUIENTE LLEGADA
					this.manejoLlegada(this.siguienteLlegada);
					this.clientesEnColaEnLLegada += this.clientesEnCola;
					this.siguienteLlegada = this.calculoTiempoLlegada();
				}
			}else{
				//NO HAY SERVIDORES EN SERVICIO
				this.manejoLlegada(this.siguienteLlegada);
				this.siguienteLlegada = this.calculoTiempoLlegada();
			}
//			this.imprimirEstado();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while(!cola.isEmpty() || this.hayServidoresActivos()){
			//SE CIERRAN LAS OFICINAS
			if (this.tiempoSistema >= 603) {
				this.clientesEnSistema603 = this.cola.size() + (4-this.numServidoresDisponibles());
			}
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
				while(!cola.isEmpty() && cola.peek().tiempoEspera >= 6) {
					this.tiempoDeEsperaEnCola += 6;
					this.cola.poll();
					--this.clientesEnCola;
					++this.clientesEsperan6Min;
				}
				this.manejoSalida(servidorMenorTiempo, t);
				if (this.clientesEnCola > 0) {
					double tiempoC = this.cola.poll().tiempoEspera;
					if(tiempoC > this.tiempoEsperaMaximo) {
						this.tiempoEsperaMaximo = tiempoC;
					}
					this.tiempoDeEsperaEnCola += tiempoC;
					--this.clientesEnCola;
					this.manejoLlegada(0);
				}
			}
//			this.imprimirEstado();
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
			this.totalClientesEnCola++;
			if (this.clientesEnCola > this.tamColaMasGrande) {
				this.tamColaMasGrande++;
			}
		}else{
			this.clientesAtendidos++;
		}
		if (tiempoLlegada > 0) {
			this.clientesPorDía++;
			if(this.numServidoresDisponibles() >= 1) {
				++this.num1Servidor;
				++this.num2Servidor;
			} else if (this.numServidoresDisponibles() >=2) {
				++this.num2Servidor;
			}
		}
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
	
	private int numServidoresDisponibles() {
		int resultado = 0;
		for(boolean b: this.estadoServidores) {
			if (!b)
				++resultado;
		}
		return resultado;
	}
	
	public double[] obtenerResultados() {
		double resultado[] = new double[17];
		resultado[0] = this.tiempoDeEsperaEnCola/this.clientesPorDía;						//a
		resultado[1] = (
				this.s1g.tiempoServicio/this.s1g.contadorClientes+
				this.s2g.tiempoServicio/this.s2g.contadorClientes+
				this.s3e.tiempoServicio/this.s3e.contadorClientes+
				this.s4u.tiempoServicio/this.s4u.contadorClientes
				)/4;																		//b
		resultado[2] = ((double)this.clientesEnColaEnLLegada)/this.clientesPorDía;		//c
		resultado[3] = this.tiempoEsperaMaximo;											//d
		resultado[4] = this.tamColaMasGrande;											//e
		resultado[5] = ((double)this.num1Servidor)/this.clientesPorDía;					//f
		resultado[6] = ((double)this.num2Servidor)/this.clientesPorDía;					//g
		resultado[7] = this.s1g.contadorClientes;											//h
		resultado[8] = this.s2g.contadorClientes;											//h
		resultado[9] = this.s3e.contadorClientes;											//h
		resultado[10] = this.s4u.contadorClientes;										//h
		resultado[11] = this.s1g.tiempoEsperaTotal;										//i
		resultado[12] = this.s2g.tiempoEsperaTotal;										//i
		resultado[13] = this.s3e.tiempoEsperaTotal;										//i
		resultado[14] = this.s4u.tiempoEsperaTotal;										//i
		resultado[15] = this.clientesEnSistema603;										//j
		resultado[16] = ((double)(this.clientesEsperan6Min*100))/this.clientesPorDía;		//k
		return resultado;
	}
}
