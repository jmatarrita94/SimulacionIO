
public class Main {

	public static void main(String[] args) {
		System.out.println("EMPIEZA SIMULACION");
		double resultados[] = new double[17];
		for(int i = 0; i < 1000; ++i) {
			System.out.println(i);
			sistema sistemaSimulado = new sistema();
			sistemaSimulado.run();
			double r[] = sistemaSimulado.obtenerResultados();
			for(int j = 0; j < 17; ++j)
				resultados[j] += r[j];
		}
		for(int j = 0; j < 17; ++j)
			System.out.println("Resultado "+j+": "+resultados[j]/1000);
	}

}
