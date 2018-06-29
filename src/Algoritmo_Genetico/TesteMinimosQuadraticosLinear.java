package Algoritmo_Genetico;

import java.awt.Color;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import Jama.Matrix;

public class TesteMinimosQuadraticosLinear {

	public static double[][] calcularMininoQuadratico(Matrix entradas, Matrix resposta) {
		double[][] r = (resposta.times(entradas.transpose())).times((entradas.times(entradas.transpose())).inverse()).getArray();
		return r;
	}
	public static double normaliza(double dadoNormal, double min, double max) {
		return (dadoNormal - min) / (max - min);
	}
	public static void main(String[] args) {
		// configurations
		// resolution, fps, taxa de bit,largura de banda
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
		double[][] ampHour = { { 0.422, 0.446, 0.522, 0.158, 0.662, 0.368, 0.630, 0.276, 0.430, 0.528, 0.450, 0.410,
				0.490, 0.452, 0.324, 0.648, 0.378, 0.416 } };

		Matrix matrixEntradas = new Matrix(conf);
		Matrix matrixRespostas = new Matrix(ampHour);
		double r[][] = calcularMininoQuadratico(matrixEntradas, matrixRespostas);
		for (int i = 0; i < r.length; i++) {
			for (int j = 0; j < r[0].length; j++) {
				System.out.print(r[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("__________________________");
		double linear = 0;
		double[] vectorOutput = new double[ampHour[0].length];
		for (int i = 0; i < conf[0].length; i++) {
			linear = 0;
			for (int j = 0; j < r[0].length; j++) {
				linear+= conf[j][i] * r[0][j];
			}
			System.out.println(linear);
			vectorOutput[i] = linear;
		}
		Plot2DPanel plot = new Plot2DPanel();
		double x[] = new double[ampHour[0].length];
		for (int i = 0; i < x.length; i++) {
			x[i]=i+1;			
		}
		double[] vectorOutputD = new double[ampHour[0].length];
		for (int i = 0; i < ampHour[0].length; i++) {
			vectorOutputD[i] = ampHour[0][i];
		}
		plot.addLinePlot("REAL", x,vectorOutputD);
		plot.addLinePlot("Obtido", x,vectorOutput);
		JFrame frame = new JFrame("Least Squares linear");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(plot);
		frame.setSize(700, 500);
		frame.setVisible(true);
	}
}
