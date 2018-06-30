package they;

import java.awt.Color;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import Jama.Matrix;

public class TesteMinimosQuadraticosExponencialThey {

	public static double[][] calcularMininoQuadratico(Matrix entradas, Matrix resposta) {
		double[][] r = (resposta.times(entradas.transpose())).times((entradas.times(entradas.transpose())).inverse())
				.getArray();
		return r;
	}

	public static double normaliza(double dadoNormal, double min, double max) {
		return (dadoNormal - min) / (max - min);
	}

	public static void main(String[] args) {
		// configurations
		// resolution, fps
		double[][] conf1 = {
				{ 320, 320, 320, 720, 720, 720, 1920, 1920, 1920 }, 
				{ 240, 240, 240, 480, 480, 480, 1080, 1080, 1080 },
				{ 15, 30, 60, 15, 30, 60, 15, 30, 60 },

		};
		double[][] conf = new double[conf1.length][conf1[0].length];
		for (int i = 0; i < conf.length; i++) {
			for (int j = 0; j < conf[0].length; j++) {
				conf[i][j] = normaliza(conf1[i][j], 15, 1920);
			}
		}
		// ampere hora
		double[][] ampHour =  {{ 0.046, 0.050, 0.056, 0.052, 0.053, 0.061, 0.054, 0.064, 0.090 }};

		Matrix matrixEntradas = new Matrix(conf);
		Matrix matrixRespostas = new Matrix(ampHour);
		double r[][] = calcularMininoQuadratico(matrixEntradas, matrixRespostas);
		for (int i = 0; i < r.length; i++) {
			for (int j = 0; j < r[0].length; j++) {
				System.out.print(r[i][j] + " ");
			}
			
		}

		double output = 0;
		double[][] aux = new double[1][ampHour[0].length];
		for (int i = 0; i < conf[0].length; i++) {
			output = 0;
			for (int j = 0; j < r[0].length; j++) {
				output += conf[j][i] * r[0][j];
			}
			aux[0][i] = output;
		}
		Matrix matrixK = new Matrix(aux);
		double[][] k = calcularMininoQuadratico(matrixK, matrixRespostas);
		System.out.println(k[0][0]+"__________________________");
		double[] vectorOutput = new double[ampHour[0].length];
		for (int i = 0; i < aux[0].length; i++) {
			vectorOutput[i] = Math.log(k[0][0])+aux[0][i];
			System.out.println(vectorOutput[i]);
		}
		
		
		Plot2DPanel plot = new Plot2DPanel();
		double x[] = new double[ampHour[0].length];
		for (int i = 0; i < x.length; i++) {
			x[i] = i + 1;
		}
		double[] vectorOutputD = new double[ampHour[0].length];
		for (int i = 0; i < ampHour[0].length; i++) {
			vectorOutputD[i] = ampHour[0][i];
		}
		plot.addLinePlot("REAL", x, vectorOutputD);
		plot.addLinePlot("Obtido", x, vectorOutput);
		JFrame frame = new JFrame("Least Squares Exponencial");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(plot);
		frame.setSize(700, 500);
		frame.setVisible(true);
	}
}
