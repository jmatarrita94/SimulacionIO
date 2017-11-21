
public class servidorExponencial extends servidor{
	double lambda;
	
	public servidorExponencial(double lambdaEntra) {
		lambda = lambdaEntra;
	}
	
	
	public double calculoTiempoServicio() {
		double r = Math.random()*10;
		return Math.log((r-1))/-lambda;	
	}

}
