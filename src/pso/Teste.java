package pso;

public class Teste {

	public static void main(String[] args) {
		double input[][] = { { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 } };
		double output[] = { 50.0, 80.0, 110.0, 140.0 };
		PSO pso = new PSO(input, output, 200, 2, 2, 1, 6, 2,0);
		pso.start(100);
		double[] r = pso.getgBest();
		for (int i = 0; i < r.length; i++) {
			System.out.println(r[i]);
		}
		System.out.println("___________________________________");
		for (int i = 0; i < output.length; i++) {
			double s = (input[i][0]*r[0]+input[i][1]*r[1]);
			System.out.println(s);
		}
		
		
		
	}

}
