
/**
 *	Servidor con distribucion de servicion uniforme
 */
public class servidorUniforme extends servidor {
	double a;
	double b;
	double resultado = 0;
	
	public servidorUniforme(double aEntra, double bEntra) {
		a = aEntra;
		b = bEntra;
	}
	
	public double calculoTiempoServicio() {
		double resultadoFin;
		do{
			resultadoFin = 0;
			double r = Math.random()*70;//r es el random 
			resultadoFin = (r-b)/(b-a);
		}while(Double.isNaN(resultadoFin));
		if(resultadoFin<0){
			return -round(resultadoFin,2);
		}else{
			return round(resultadoFin ,2);
		}	
	}
}
