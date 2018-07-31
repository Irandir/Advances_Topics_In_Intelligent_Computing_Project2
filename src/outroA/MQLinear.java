package outroA;

import java.awt.Color;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import Jama.Matrix;

public class MQLinear {
	private double[]p;
	private double rsme;
	private double vectorOutput[];
	
	public void run(double[][] conf,double[][] ampHour){
		Matrix matrixEntradas = new Matrix(conf);
		Matrix matrixRespostas = new Matrix(ampHour);
		double r[][] = calcularMininoQuadratico(matrixEntradas, matrixRespostas);
		p = new double[r[0].length];
		for (int i = 0; i < r.length; i++) {
			for (int j = 0; j < r[0].length; j++) {
				p[j] = r[i][j];
			}
		}
		double linear = 0;
		vectorOutput = new double[ampHour[0].length];
		double rsme = 0;
		for (int i = 0; i < conf[0].length; i++) {
			linear = 0;
			for (int j = 0; j < r[0].length; j++) {
				linear+= conf[j][i] * r[0][j];
			}
			rsme += Math.pow(ampHour[0][i] - linear, 2);
			vectorOutput[i] = linear;
		}
		this.rsme = rsme / ampHour.length;
	}
	
	public static double[][] calcularMininoQuadratico(Matrix entradas, Matrix resposta) {
		double[][] r = (resposta.times(entradas.transpose())).times((entradas.times(entradas.transpose())).inverse()).getArray();
		return r;
	}
	public static double normaliza(double dadoNormal, double min, double max) {
		return (dadoNormal - min) / (max - min);
	}
	public static void main(String[] args) {
		double[][] conf1 = { { 640, 640, 640, 640, 1280, 1280 }, { 360, 360, 360, 360, 720, 720 },
				{ 30, 60, 30, 60, 30, 60 }

		};
		//double[][] conf = { { -1.0, -0.7, -0.4, -0.1, 0.2, 0.5, 0.8, 1.0 } };
		double[][] conf = new double[conf1.length][conf1[0].length];

		for (int i = 0; i < conf.length; i++) {
			for (int j = 0; j < conf[0].length; j++) {
				conf[i][j] = normaliza(conf1[i][j], 30, 1280 );
			}
		}

		double[][] ampHour =  { { 594.9, 698.1, 682.9, 783.7,786.4, 975.8 } };
		//double[][] ampHourN = { { 36.547, 17.264, 8.155, 3.852, 1.820, 0.860, 0.406, 0.246} };
		
		double[][] ampHourN = new double[1][ampHour[0].length];
		for (int i = 0; i < ampHourN[0].length; i++) {
			 ampHourN[0][i] = normaliza(ampHour[0][i],30,1280  );
		}

		MQLinear t = new MQLinear();
		t.run(conf, ampHourN);
		Plot2DPanel plot = new Plot2DPanel();
		double x[] = new double[ampHour[0].length];
		for (int i = 0; i < x.length; i++) {
			x[i]=i+1;			
		}
		double[] vectorOutputD = new double[ampHour[0].length];
		for (int i = 0; i < ampHour[0].length; i++) {
			vectorOutputD[i] = ampHourN[0][i];
		}
		plot.addLinePlot("REAL", x,vectorOutputD);
		plot.addLinePlot("Obtido", x,t.vectorOutput);
		JFrame frame = new JFrame("Least Squares linear");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(plot);
		frame.setSize(700, 500);
		frame.setVisible(true);
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
