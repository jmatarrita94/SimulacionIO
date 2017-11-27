
/**
 *	Servidor con distribucion de servicio gamma
 */
public class servidorGamma extends servidor {
	double resultadoGamma=0;
	double lambda=0;
	double alpha = 0;

	
	public servidorGamma(double lambdaEntra, double alphaEntra) {
		lambda = lambdaEntra;
		alpha = alphaEntra;
	
	}
	
	public double calculoTiempoServicio(){
		do{
			resultadoGamma = 0;
			double r = Math.random()*70;
			for (int i = 0; i<alpha; i++) {
				resultadoGamma += Math.log((r-1))/-lambda;
				r = Math.random()*15;
			}
		}while(Double.isNaN(resultadoGamma));
		if(resultadoGamma<0){
			return -round(resultadoGamma, 2);
		}
		return round(resultadoGamma ,2);
	}
}
