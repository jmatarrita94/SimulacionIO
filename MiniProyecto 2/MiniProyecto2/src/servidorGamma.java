
public class servidorGamma extends servidor {
	double resultadoGamma=0;
	double lambda=0;
	
	public double function(){
		double r = Math.random()*10;
		for (int i = 0; i<7; i++) {
			resultado = Math.log((r-1))/lambda;
			resultadoGamma = resultadoGamma+resultado;
			r = Math.random()*10;
		}
		return resultadoGamma;
	}
}
