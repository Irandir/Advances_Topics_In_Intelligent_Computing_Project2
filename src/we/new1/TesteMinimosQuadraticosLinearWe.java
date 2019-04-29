package we.new1;

import java.awt.Color;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import Jama.Matrix;

public class TesteMinimosQuadraticosLinearWe {
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
		// ampere hora
		double[][] ampHour = {{

			16.424, 17.008, 17.336, 16.854, 16.402, 15.726,

			15.55, 16.452, 16.11, 16.458, 17.86, 15.612,

			15.45, 16.768, 14.282, 15.854, 16.03, 16.976 }};
	
		double[][] ampHour2 = new double[1][ampHour[0].length];
		for (int j = 0; j < ampHour2[0].length; j++) {
			ampHour2[0][j] = normaliza(ampHour[0][j], 13, 17.86);
		}

		TesteMinimosQuadraticosLinearWe t = new TesteMinimosQuadraticosLinearWe();
		t.run(conf, ampHour2);
		Plot2DPanel plot = new Plot2DPanel();
		double x[] = new double[ampHour2[0].length];
		for (int i = 0; i < x.length; i++) {
			x[i]=i+1;			
		}
		double[] vectorOutputD = new double[ampHour2[0].length];
		for (int i = 0; i < ampHour2[0].length; i++) {
			vectorOutputD[i] = ampHour2[0][i];
		}
		System.out.println("EMQ__>"+t.rsme);
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
