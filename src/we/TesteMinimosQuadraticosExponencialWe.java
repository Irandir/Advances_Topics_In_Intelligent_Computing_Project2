package we;


import java.awt.Color;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import Jama.Matrix;
import we.TesteMinimosQuadraticosExponencialWe;

public class TesteMinimosQuadraticosExponencialWe {

	private double[]p;
	private double rsme;
	private double vectorOutput[];
	
	public void run(double[][] conf,double[][] ampHour2){
		
		double[][] ampHour = new double[ampHour2.length][ampHour2[0].length];
		
		for (int i = 0; i < ampHour[0].length; i++) {
			ampHour[0][i] = Math.log(ampHour2[0][i]);
		}
		
		int n = conf[0].length;
		
		double[] sumX = new double[conf.length+1];
		for (int i = 0; i < conf.length; i++) {
			for (int j = 0; j < conf[0].length; j++) {
				sumX[i]+=conf[i][j];
			}
		}
		for (int i = 0; i < ampHour[0].length; i++) {
			sumX[sumX.length-1]+= ampHour[0][i];
		}
		
		double matrix[][] = new double[conf.length+1][conf.length+2];
		int aux = 0;
		double s = 0;
		for (int i = 0; i < matrix.length; i++) {
			if(i == 0){
				matrix[i][0]  = n;
				for (int j = 0; j < sumX.length; j++) {
					matrix[i][j+1] = sumX[j];
				}
			}else{
				matrix[i][0]  = sumX[i-1];
				for (int j = 0; j < matrix[0].length-2; j++) {
					s = 0;
					for (int j2 = 0; j2 < conf[0].length; j2++) {
						s+= conf[j][j2]*conf[aux][j2];
					}
					matrix[i][j+1] = s;
				}
				s = 0;
				for (int j2 = 0; j2 < conf[0].length; j2++) {
					s+= ampHour[0][j2]*conf[aux][j2];
				}
				matrix[i][matrix[0].length-1]  = s;
				aux++;
			}
			
		}
		
		// mininmos quadraticos ativar
		double [][]entrada = new double[matrix.length][matrix[0].length-1];
		double saida[][] = new double[1][matrix.length];
		
		for (int i = 0; i < entrada.length; i++) {
			for (int j = 0; j < entrada[0].length; j++) {
				entrada[i][j] = matrix[j][i];
			}
			saida[0][i] = matrix[i][matrix[0].length-1];
		}
		
		Matrix matrixEntradas = new Matrix(entrada);
		Matrix matrixRespostas = new Matrix(saida);
		double r[][] = calcularMininoQuadratico(matrixEntradas, matrixRespostas);
		p = new double[r[0].length];
		for (int i = 0; i < r.length; i++) {
			for (int j = 0; j < r[0].length; j++) {
				p[j] = r[i][j];
			}
		}
		
		p[0] = Math.exp(r[0][0]);
		
		double exp = 0;
		double rsme = 0;
		vectorOutput = new double[ampHour[0].length];
		for (int i = 0; i < conf[0].length; i++) {
			exp = 0;
			for (int j = 0; j < conf.length; j++) {
				exp += conf[j][i] * r[0][j+1];
			}
			vectorOutput[i] = p[0]*Math.exp(exp);
			rsme += Math.pow(ampHour2[0][i] - vectorOutput[i], 2);
		}
		this.rsme = rsme / ampHour.length;
		
	}
	
	public static void main(String[] args) {
		double[][] conf1 = {
				{ 320, 320, 320, 320, 320, 320, 720, 720, 720, 720, 720, 720, 1920, 1920, 1920, 1920, 1920, 1920 },
				{ 240, 240, 240, 240, 240, 240, 480, 480, 480, 480, 480, 480, 1080, 1080, 1080, 1080, 1080, 1080 },
				{ 15, 30, 60, 15, 30, 60, 15, 30, 60, 15, 30, 60, 15, 30, 60, 15, 30, 60 },
				{ 256, 256, 256, 512, 512, 512, 256, 256, 256, 512, 512, 512, 256, 256, 256, 512, 512, 512 },

		};
		double[][] conf = new double[conf1.length][conf1[0].length];

		for (int i = 0; i < conf.length; i++) {
			for (int j = 0; j < conf[0].length; j++) {
				conf[i][j] = normaliza(conf1[i][j], 15, 1920);
			}
		}
		double[][] ampHour2 =  { { 0.422, 0.446, 0.522, 0.158, 0.662, 0.368, 0.630, 0.276, 0.430, 0.528, 0.450, 0.410,
			0.490, 0.452, 0.324, 0.648, 0.378, 0.416 } };
		TesteMinimosQuadraticosExponencialWe t = new TesteMinimosQuadraticosExponencialWe();
		t.run(conf, ampHour2);
		double[] vectorOutputD = new double[ampHour2[0].length];
		for (int i = 0; i < ampHour2[0].length; i++) {
			vectorOutputD[i] = ampHour2[0][i];
		}
		
		Plot2DPanel plot = new Plot2DPanel();
		double x[] = new double[ampHour2[0].length];
		for (int i = 0; i < x.length; i++) {
			x[i] = i + 1;
		}
		plot.addLinePlot("REAL", x, vectorOutputD);
		plot.addLinePlot("Obtido", x, t.getVectorOutput());
		JFrame frame = new JFrame("Least Squares Exponencial");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(plot);
		frame.setSize(700, 500);
		frame.setVisible(true);
		System.out.println(t.rsme);
		for (int i = 0; i < t.p.length; i++) {
			System.out.println(t.p[i]);
		}
	}
	
	public static double[][] calcularMininoQuadratico(Matrix entradas, Matrix resposta) {
		double[][] r = (resposta.times(entradas.transpose())).times((entradas.times(entradas.transpose())).inverse())
				.getArray();
		return r;
	}

	public static double normaliza(double dadoNormal, double min, double max) {
		return (dadoNormal - min) / (max - min);
	}	
	
	public double[] getP() {
		return p;
	}

	public void setP(double[] p) {
		this.p = p;
	}

	public double getRsme() {
		return rsme;
	}

	public void setRsme(double rsme) {
		this.rsme = rsme;
	}

	public double[] getVectorOutput() {
		return vectorOutput;
	}

	public void setVectorOutput(double[] vectorOutput) {
		this.vectorOutput = vectorOutput;
	}
	
	
}
