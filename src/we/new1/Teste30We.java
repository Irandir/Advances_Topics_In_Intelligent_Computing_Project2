package we.new1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.math.plot.Plot2DPanel;

import Algoritmo_Genetico.AlgoritmoGeneticoReal;
import pso.PSO;

public class Teste30We {

	static Random rand = new Random();

	public double[][] input() {
		TesteAlgoritmoGeneticoLinear t = new TesteAlgoritmoGeneticoLinear();
		double[][] conf1 = {

				{ 320, 240, 15, 256 }, { 320, 240, 30, 256 }, { 320, 240, 60, 256 },

				{ 320, 240, 15, 512 }, { 320, 240, 30, 512 }, { 320, 240, 60, 512 },

				{ 720, 480, 15, 256 }, { 720, 480, 30, 256 }, { 720, 480, 60, 256 },

				{ 720, 480, 15, 512 }, { 720, 480, 30, 512 }, { 720, 480, 60, 512 },

				{ 1920, 1080, 15, 256 }, { 1920, 1080, 30, 256 }, { 1920, 1080, 60, 256 },

				{ 1920, 1080, 15, 512 }, { 1920, 1080, 30, 512 }, { 1920, 1080, 60, 512 }

		};
		double[][] conf = new double[conf1.length][conf1[0].length];
		for (int i = 0; i < conf.length; i++) {
			for (int j = 0; j < conf[0].length; j++) {
				conf[i][j] = t.normaliza(conf1[i][j], 15, 1920);
			}
		}
		return conf;
	}

	public double[][] input2() {
		TesteAlgoritmoGeneticoLinear t = new TesteAlgoritmoGeneticoLinear();
		double[][] conf1 = {
				{ 320, 320, 320, 320, 320, 320, 720, 720, 720, 720, 720, 720, 1920, 1920, 1920, 1920, 1920, 1920 },
				{ 240, 240, 240, 240, 240, 240, 480, 480, 480, 480, 480, 480, 1080, 1080, 1080, 1080, 1080, 1080 },
				{ 15, 30, 60, 15, 30, 60, 15, 30, 60, 15, 30, 60, 15, 30, 60, 15, 30, 60 },
				{ 256, 256, 256, 512, 512, 512, 256, 256, 256, 512, 512, 512, 256, 256, 256, 512, 512, 512 },

		};
		double[][] conf = new double[conf1.length][conf1[0].length];
		for (int i = 0; i < conf.length; i++) {
			for (int j = 0; j < conf[0].length; j++) {
				conf[i][j] = t.normaliza(conf1[i][j], 15, 1920);
			}
		}
		return conf;
	}

	public static void main(String[] args) {
		Teste30We teste30 = new Teste30We();

		double[][] input = teste30.input();
		double[][] input2 = teste30.input2();
		double[] ampHourD = { 16.424, 17.008, 17.336, 16.854, 16.402, 15.726,

				15.55, 16.452, 16.11, 16.458, 17.86, 15.612,

				15.45, 16.768, 14.282, 15.854, 16.03, 16.976 };
		double[][] ampHour2D = { { 16.424, 17.008, 17.336, 16.854, 16.402, 15.726,

				15.55, 16.452, 16.11, 16.458, 17.86, 15.612,

				15.45, 16.768, 14.282, 15.854, 16.03, 16.976 } };
		double[] ampHour =new double[ampHourD.length];
		for (int j = 0; j < ampHour.length; j++) {
			ampHour[j] = normaliza(ampHourD[j], 13, 17.86);
		}
		double[][] ampHour2 =new double[1][ampHour2D[0].length];
		for (int j = 0; j < ampHour2[0].length; j++) {
			ampHour2[0][j] = normaliza(ampHour2D[0][j], 13, 17.86);
		}
		
		// AG Linear (teste 1)
		double meanBestIndividual[] = new double[input[0].length];
		double meanBestIndFit = 0;
		double meanOutput[] = new double[input.length];
		double rsme = 0;
		double pcc = 0;
		// AG Expoencial (teste 2)
		double meanBestIndividual2[] = new double[input[0].length + 1];
		double meanBestIndFit2 = 0;
		double meanOutput2[] = new double[input.length];
		double rsme2 = 0;
		double pcc2 = 0;

		double vectorRSME[][] = new double[2][30];

		// minimos quadraticos
		double meanP[] = new double[input[0].length];
		double meanOutput3[] = new double[ampHour2[0].length];
		double rsme3 = 0;
		double pcc3 = 0;
		// minimos quadraticos exp
		double meanP2[] = new double[input[0].length + 1];
		double meanOutput4[] = new double[ampHour2[0].length];
		double rsme4 = 0;
		double pcc4 = 0;
		AlgoritmoGeneticoReal teste = new AlgoritmoGeneticoReal();
		AlgoritmoGeneticoReal teste2 = new AlgoritmoGeneticoReal();
		TesteMinimosQuadraticosLinearWe teste3 = new TesteMinimosQuadraticosLinearWe();
		TesteMinimosQuadraticosExponencialWe teste4 = new TesteMinimosQuadraticosExponencialWe();

		// PSO linear
		double mean_gBEAST[] = new double[input[0].length];
		double mean_gBEASTFit = 0;
		double meanOutput5[] = new double[ampHour2[0].length];
		double rsme5 = 0;
		double pcc5 = 0;
		double[] aux;

		// PSO EXP
		double mean_gBEAST2[] = new double[input[0].length + 1];
		double mean_gBEASTFit2 = 0;
		double meanOutput6[] = new double[ampHour2[0].length];
		double rsme6 = 0;
		double pcc6 = 0;
		double[] aux2;
		PSO pso,pso2;
		
		for (int i = 0; i < 30; i++) {
			// AG linear 1
			teste = new AlgoritmoGeneticoReal();
			rsme +=teste.startLinear(input, ampHour, 200, 200,0.1,0.8);
			for (int j = 0; j < meanOutput.length; j++) {
				meanOutput[j] += teste.getVectorOutput()[j];
			}
			meanBestIndFit += teste.getBestIndFit()[teste.getBestIndFit().length - 1];
			/*for (int j = 0; j < meanBestIndividual.length; j++) {
				meanBestIndividual[j] += teste.getBestIndividual()[j];
			}
			vectorRSME[0][i] = teste.getRsme();
			rsme += teste.getRsme();*/
			//pcc += pcc(teste.getVectorOutput(), ampHour);

			//PSO LINEAR
			pso = new PSO(input, ampHour, 200, 2, 2, 0.8, 0.8, 0.2,0);
			aux = pso.start(200);
			for (int j = 0; j < meanOutput5.length; j++) {
				meanOutput5[j] += aux[j];
			}
			mean_gBEASTFit= pso.getgBestFitness()[pso.getgBestFitness().length-1];
			for (int j = 0; j < mean_gBEAST.length; j++) {
				mean_gBEAST[j] += pso.getgBest()[j];
			}
			//vectorRSME[3][i] = mean_gBEASTFit;
			rsme5+=mean_gBEASTFit;
			
			//PSO EXP
			pso2 = new PSO(input, ampHour, 200, 2, 2, 0.8, 0.8, 0.2,1);
			aux2 = pso2.start(200);
			for (int j = 0; j < meanOutput6.length; j++) {
				meanOutput6[j] += aux2[j];
			}
			mean_gBEASTFit2= pso2.getgBestFitness()[pso2.getgBestFitness().length-1];
			/*for (int j = 0; j < mean_gBEAST2.length; j++) {
				mean_gBEAST2[j] += pso2.getgBest()[j];
			}*/
			//vectorRSME[3][i] = mean_gBEASTFit;
			rsme6+=mean_gBEASTFit2;
			
			// AG EXP 2
			teste2 = new AlgoritmoGeneticoReal();
			rsme2 +=teste2.startEXP(input, ampHour, 200, 200,0.6,0.3);
			for (int j = 0; j < meanOutput2.length; j++) {
				meanOutput2[j] += teste2.getVectorOutput()[j];
			}
			meanBestIndFit2 += teste2.getBestIndFit()[teste2.getBestIndFit().length - 1];
			/*for (int j = 0; j < meanBestIndividual2.length; j++) {
				meanBestIndividual2[j] += teste2.getBestIndividual()[j];
			}
			vectorRSME[1][i] = teste2.getRsme();
			rsme2 += teste2.getRsme();
			pcc2 += pcc(teste2.getVectorOutput(), ampHour);*/

			// teste 3
			teste3 = new TesteMinimosQuadraticosLinearWe();
			teste3.run(input2, ampHour2);
			for (int j = 0; j < meanOutput3.length; j++) {
				meanOutput3[j] += teste3.getVectorOutput()[j];
			}
			for (int j = 0; j < meanP.length; j++) {
				meanP[j] += teste3.getP()[j];
			}
			rsme3 += teste3.getRsme();
			pcc3 += pcc(teste3.getVectorOutput(), ampHour2[0]);

			// teste 4
			teste4 = new TesteMinimosQuadraticosExponencialWe();
			teste4.run(input2, ampHour2);
			for (int j = 0; j < meanOutput4.length; j++) {
				meanOutput4[j] += teste4.getVectorOutput()[j];
			}
			for (int j = 0; j < meanP2.length; j++) {
				meanP2[j] += teste4.getP()[j];
			}
			rsme4 += teste4.getRsme();
			pcc4 += pcc(teste4.getVectorOutput(), ampHour2[0]);
		}
		
		// divisão por 30
		for (int i = 0; i < meanOutput.length; i++) {
			meanOutput[i] = meanOutput[i] / 30;
		}
		meanBestIndFit = meanBestIndFit / 30;

		for (int i = 0; i < meanOutput2.length; i++) {
			meanOutput2[i] = meanOutput2[i] / 30;
		}
		meanBestIndFit2 = meanBestIndFit2 / 30;

		for (int i = 0; i < meanOutput3.length; i++) {
			meanOutput3[i] = meanOutput3[i] / 30;
		}
		for (int i = 0; i < meanOutput4.length; i++) {
			meanOutput4[i] = meanOutput4[i] / 30;
		}
		for (int i = 0; i < meanOutput5.length; i++) {
			meanOutput5[i] = meanOutput5[i] / 30;
		}
		for (int i = 0; i < meanOutput6.length; i++) {
			meanOutput6[i] = meanOutput6[i] / 30;
		}
		rsme /= 30;
		rsme2 /= 30;
		rsme3 /= 30;
		rsme4 /= 30;
		rsme5 /= 30;
		rsme6 /= 30;
		
		System.out.println("_________Saidas_______");
		System.out.print("Real --> \n");
		for (int i = 0; i < ampHour.length; i++) {
			String a = ampHour[i] + " ";
			a = a.replace(".", ",");
			System.out.println(a);
		}
		System.out.print("\nAG Linear --> \n");
		for (int i = 0; i < ampHour.length; i++) {
			String a = meanOutput[i] + " ";
			a = a.replace(".", ",");
			System.out.println(a);
		}
		System.out.print("\nAG Exp --> \n");
		for (int i = 0; i < ampHour.length; i++) {
			String a = meanOutput2[i] + " ";
			a = a.replace(".", ",");
			System.out.println(a);
		}
		System.out.print("\nMQ Linear --> \n");
		for (int i = 0; i < ampHour.length; i++) {
			String a = meanOutput3[i] + " ";
			a = a.replace(".", ",");
			System.out.println(a);
		}
		System.out.print("\nMQ Exp --> \n");
		for (int i = 0; i < ampHour.length; i++) {
			String a = meanOutput4[i] + " ";
			a = a.replace(".", ",");
			System.out.println(a);
		}
		System.out.print("\nPSO Linear --> \n");
		for (int i = 0; i < ampHour.length; i++) {
			String a = meanOutput5[i] + " ";
			a = a.replace(".", ",");
			System.out.println(a);
		}
		System.out.print("\nPSO Exp --> \n");
		for (int i = 0; i < ampHour.length; i++) {
			String a = meanOutput6[i] + " ";
			a = a.replace(".", ",");
			System.out.println(a);
		}
		
		System.out.println("\n_________EMQ__________");
		System.out.println("AG linear rsme -->" + rsme);
		System.out.println("AG linear exp -->" + rsme2);
		System.out.println("Mínino Quadrático linear -->" + rsme3);
		System.out.println("Mínino Quadrático Exp -->" + rsme4);
		System.out.println("PSO linear -->" + rsme5);
		System.out.println("PSO Exp -->" + rsme6);
		
		// plot
		plot(ampHour, meanOutput, meanOutput2, meanOutput3, meanOutput4,meanOutput5,meanOutput6);
		
		// plot 2
		double values[] = new double[6];
		String names[] = new String[6];
		values[0] = rsme;
		values[1] = rsme2;
		values[2] = rsme3;
		values[3] = rsme4;
		values[4] = rsme5;
		values[5] = rsme6;
		
		names[0] = "AG Linear";
		names[1] = "AG Exponencial";
		names[2] = "LS Linear";
		names[3] = "LS Exp";
		names[4] = "PSO Linear";
		names[5] = "PSO EXP";
		
		plot2(values, names, "Erro Medio Quadrático");
		
	}

	public static void plot(double[] ampHour, double[] meanOutput, double[] meanOutput2, double[] meanOutput3,
			double[] meanOutput4,double[] meanOutput5,double[] meanOutput6) {

		Plot2DPanel plot = new Plot2DPanel();

		double x[] = new double[ampHour.length];
		for (int i = 0; i < x.length; i++) {
			x[i] = i + 1;
		}
		plot.addLinePlot("Real", x, ampHour);
		plot.addLinePlot("AG Linear", x, meanOutput);
		plot.addLinePlot("AG Exponencial", x, meanOutput2);
		plot.addLinePlot("LS Linear", x, meanOutput3);
		plot.addLinePlot("LS Exp", Color.BLACK, x, meanOutput4);
		plot.addLinePlot("PSO Linear", x, meanOutput5);
		plot.addLinePlot("PSO EXP", x, meanOutput6);
		JFrame frame = new JFrame("Output das 30 execuções");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(plot);
		frame.setSize(700, 500);
		frame.setVisible(true);
	}

	public static void plot2(double[] value, String[] name, String titulo) {
		DefaultCategoryDataset dados = new DefaultCategoryDataset();
		for (int i = 0; i < value.length; i++) {
			dados.addValue(value[i], name[i], "");
		}

		JFreeChart grafico = ChartFactory.createBarChart(titulo, "metodo", "Saída", dados, PlotOrientation.VERTICAL,
				true, true, true);
		CategoryPlot plot = (CategoryPlot) grafico.getPlot();
		CategoryItemRenderer itemRerender = plot.getRenderer();
		// Caso vc queira mudar a cor das barras
		itemRerender.setSeriesPaint(0, Color.RED);
		itemRerender.setSeriesPaint(1, Color.GREEN);
		itemRerender.setSeriesPaint(2, Color.YELLOW);
		itemRerender.setSeriesPaint(3, Color.BLACK);
		JFrame frame = new JFrame();
		frame.add(new ChartPanel(grafico));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public static double mean(double[] vector) {
		double mean = 0;
		for (int i = 0; i < vector.length; i++) {
			mean += vector[i];
		}
		mean /= vector.length;
		return mean;
	}

	// pcc
	public static double pcc(double vectorOutput[], double ampHour[]) {
		double xbarra = mean(vectorOutput);
		double ybarra = mean(ampHour);
		double numerador = 0;
		double numerador1 = 0;
		double numerador2 = 0;
		double denominador1 = 0;
		double denominador2 = 0;
		double denominador = 0;
		for (int i = 0; i < ampHour.length; i++) {
			numerador1 = (vectorOutput[i] - xbarra);
			numerador2 = (ampHour[i] - ybarra);
			numerador += numerador1 * numerador2;
			denominador1 += Math.pow((vectorOutput[i] - xbarra), 2);
			denominador2 += Math.pow((ampHour[i] - ybarra), 2);
		}
		denominador = Math.sqrt(denominador1 * denominador2);
		double r = numerador / denominador;
		return r;
	}
	public static double normaliza(double dadoNormal, double min, double max) {
		return (dadoNormal - min) / (max - min);
	}

}
