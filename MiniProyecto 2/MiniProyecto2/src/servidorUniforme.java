
public class servidorUniforme extends servidor {
	double a;
	double b;
	double resultado = 0;
	
	public servidorUniforme(double aEntra, double bEntra) {
		a = aEntra;
		b = bEntra;
	}
	
	public double calculoTiempoServicio() {
		double r = Math.random()*10;//r es el random 
		return (r-b)/(b-a);		
	}
}
