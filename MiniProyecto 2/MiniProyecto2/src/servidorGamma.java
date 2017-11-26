
public class servidorGamma extends servidor {
	double resultadoGamma=0;
	double lambda=0;
	double alpha = 0;

	
	public servidorGamma(double lambdaEntra, double alphaEntra) {
		lambda = lambdaEntra;
		alpha = alphaEntra;
	
	}
	
	public double calculoTiempoServicio(){
		double r = Math.random()*10;
		for (int i = 0; i<alpha; i++) {
			resultado = Math.log((r-1))/-lambda;
			resultadoGamma += resultado;
			r = Math.random()*10;
		}
		return resultadoGamma;
	}
}
