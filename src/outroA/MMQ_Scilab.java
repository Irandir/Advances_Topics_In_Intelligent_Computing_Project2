package outroA;

public class MMQ_Scilab {
	
	public static void main(String[] args) {
		double [][]x = {
				{ 0.5, 0.5, 0.5, 0.5, 1., 1. }, 
				{ 0.28125, 0.28125, 0.28125, 0.28125, 0.5625, 0.5625 },
				{ 0.0234375, 0.046875, 0.0234375, 0.046875, 0.0234375, 0.046875 },
				{ 0., 0., 0.0007813, 0.0007813, 0.0007813, 0.0007813 } };
		double [] p = {-6.183D+16,   5.388D+16,   7.7266667,   16384};
		double [] y  = {0.4647656,   0.5453906,   0.5335156,   0.6122656,   0.614375,   0.7623437};
		double s = 0;
		for (int i = 0; i < y.length; i++) {
			s = 0;
			for (int j = 0; j < x.length; j++) {
				
				s+= x[j][i]*p[j];
				System.out.println("x[j][i]-->"+x[j][i]+"p[j]-->"+p[j]+"  s-->"+s);
			}
			System.out.println("y --> "+y[i]+"  s-->"+s);
		}
		System.out.println(-1.576D+16);
	}
	 
	 
}
