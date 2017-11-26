
public class servidorExponencial extends servidor{
	double lambda;
	
	public servidorExponencial(double lambdaEntra) {
		lambda = lambdaEntra;
	}
	
	
	public double calculoTiempoServicio() {
		double resultadoFin;
		do{
			resultadoFin = 0;
			double r = Math.random()*70;
			resultadoFin = Math.log((r-1))/-lambda;
		}while(Double.isNaN(resultadoFin));	
		if(resultadoFin<0){
			return -round(resultadoFin,2);
		}else{
			return round(resultadoFin , 2);
		}
		
	}

}
