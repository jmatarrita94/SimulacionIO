
public class servidorGamma2 extends servidor{

	double resultadoGamma2=0;
	double lambda=0;
	
	public double function(){
		double r = Math.random()*10;
		for (int i = 0; i<7; i++) {
			resultado = Math.log((r-1))/lambda;
			resultadoGamma2 = resultadoGamma2+resultado;
			r = Math.random()*10;
		}
		return resultadoGamma2;
	}

}
