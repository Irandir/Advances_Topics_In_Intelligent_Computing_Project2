package they;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Locale.Category;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.math.plot.Plot2DPanel;

public class Teste30They {
	
	static Random rand = new Random();
	
	public double[][] input() {
		TesteThey t = new TesteThey();
		double[][] conf1 = {

				{ 320, 240, 15 }, { 320, 240, 30 }, { 320, 240, 60 }, { 720, 480, 15 }, { 720, 480, 30 },
				{ 720, 480, 60 }, { 1920, 1080, 15 }, { 1920, 1080, 30 }, { 1920, 1080, 60 }

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
		TesteThey t = new TesteThey();
		double[][] conf1 = { { 320, 320, 320, 720, 720, 720, 1920, 1920, 1920 },
				{ 240, 240, 240, 480, 480, 480, 1080, 1080, 1080 }, { 15, 30, 60, 15, 30, 60, 15, 30, 60 }

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
		Teste30They teste30 = new Teste30They();

		double[][] input = teste30.input();
		double[][] input2 = teste30.input2();
		double[] ampHour = { 0.046, 0.050, 0.056, 0.052, 0.053, 0.061, 0.054, 0.064, 0.090 };
		double[][] ampHour2 = { { 0.046, 0.050, 0.056, 0.052, 0.053, 0.061, 0.054, 0.064, 0.090 } };

		// teste 1
		double meanBestIndividual[] = new double[input[0].length];
		double meanBestIndFit = 0;
		double meanOutput[] = new double[input.length];
		double rsme = 0;
		double pcc = 0;
		// teste 2
		double meanBestIndividual2[] = new double[input[0].length + 1];
		double meanBestIndFit2 = 0;
		double meanOutput2[] = new double[input.length];
		double rsme2 = 0;
		double pcc2 = 0;
		
		double[][] vectorRSME = new double[2][30];
		
		// minimos quadraticos linear
		double meanP[] = new double[input[0].length];
		double meanOutput3[] = new double[ampHour2[0].length];
		double rsme3 = 0;
		double pcc3 = 0;
		// minimos quadraticos exp
		double meanP2[] = new double[input[0].length + 1];
		double meanOutput4[] = new double[ampHour2[0].length];
		double rsme4 = 0;
		double pcc4 = 0;

		TesteThey teste = new TesteThey();
		Teste2They teste2 = new Teste2They();
		TesteMinimosQuadraticosLinearThey teste3 = new TesteMinimosQuadraticosLinearThey();
		TesteMinimosQuadraticosExponencialThey2 teste4 = new TesteMinimosQuadraticosExponencialThey2();

		for (int i = 0; i < 30; i++) {
			// teste 1
			teste = new TesteThey();
			teste.run(input, ampHour, 20, 200);
			for (int j = 0; j < meanOutput.length; j++) {
				meanOutput[j] += teste.getVectorOutput()[j];
			}
			meanBestIndFit += teste.getBestIndFit()[teste.getBestIndFit().length - 1];
			for (int j = 0; j < meanBestIndividual.length; j++) {
				meanBestIndividual[j] += teste.getBestIndividual()[j];
			}
			vectorRSME[0][i] = teste.getRsme();
			rsme += teste.getRsme();
			pcc += pcc(teste.getVectorOutput(), ampHour);

			// teste 2
			teste2 = new Teste2They();
			teste2.run(input, ampHour, 20, 200);
			for (int j = 0; j < meanOutput2.length; j++) {
				meanOutput2[j] += teste2.getVectorOutput()[j];
			}
			meanBestIndFit2 += teste2.getBestIndFit()[teste2.getBestIndFit().length - 1];
			for (int j = 0; j < meanBestIndividual2.length; j++) {
				meanBestIndividual2[j] += teste2.getBestIndividual()[j];
			}
			vectorRSME[1][i] = teste2.getRsme();
			rsme2 += teste2.getRsme();
			pcc2 += pcc(teste2.getVectorOutput(), ampHour);

			teste3 = new TesteMinimosQuadraticosLinearThey();
			teste3.run(input2, ampHour2);
			for (int j = 0; j < meanOutput3.length; j++) {
				meanOutput3[j] += teste3.getVectorOutput()[j];
			}
			for (int j = 0; j < meanP.length; j++) {
				meanP[j] += teste3.getP()[j];
			}
			rsme3 += teste3.getRsme();
			pcc3 += pcc(teste3.getVectorOutput(), ampHour2[0]);

			teste4 = new TesteMinimosQuadraticosExponencialThey2();
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
		for (int i = 0; i < meanBestIndividual.length; i++) {
			meanBestIndividual[i] = meanBestIndividual[i] / 30;
		}
		for (int i = 0; i < meanOutput2.length; i++) {
			meanOutput2[i] = meanOutput2[i] / 30;
		}
		meanBestIndFit2 = meanBestIndFit2 / 30;
		for (int i = 0; i < meanBestIndividual2.length; i++) {
			meanBestIndividual2[i] = meanBestIndividual2[i] / 30;
		}
		for (int i = 0; i < meanOutput3.length; i++) {
			meanOutput3[i] = meanOutput3[i] / 30;
		}
		for (int i = 0; i < meanP.length; i++) {
			meanP[i] = meanP[i] / 30;
		}

		for (int i = 0; i < meanOutput4.length; i++) {
			meanOutput4[i] = meanOutput4[i] / 30;
		}
		for (int i = 0; i < meanP2.length; i++) {
			meanP2[i] = meanP2[i] / 30;
		}
		rsme /= 30;
		rsme2 /= 30;
		rsme3 /= 30;
		rsme4 /= 30;
		pcc /= 30;
		pcc2 /= 30;
		pcc3 /= 30;
		pcc4 /= 30;

		// prints
		System.out.println("Fitness do melhor individuo");
		System.out.println(meanBestIndFit);
		System.out.println(meanBestIndFit2);

		System.out.println("________P_________");
		System.out.print("Ag linear --> ");
		for (int i = 0; i < meanBestIndividual.length; i++) {
			System.out.print(meanBestIndividual[i] + "  ");
		}
		System.out.print("\nAg EXP --> ");
		for (int i = 0; i < meanBestIndividual2.length; i++) {
			System.out.print(meanBestIndividual2[i] + " ");
		}
		System.out.print("\nMQ linear --> ");
		for (int i = 0; i < meanP.length; i++) {
			System.out.print(meanP[i] + "  ");
		}
		System.out.print("\nMQ EXP --> ");
		for (int i = 0; i < meanP2.length; i++) {
			System.out.print(meanP2[i] + "  ");
		}

		System.out.println("\n_________EMQ__________");
		System.out.println("AG linear rsme -->" + rsme);
		System.out.println("AG linear exp -->" + rsme2);
		System.out.println("Mínino Quadrático linear -->" + rsme3);
		System.out.println("Mínino Quadrático Exp -->" + rsme4);

		System.out.println("_________Saidas_______");
		System.out.print("Real --> ");
		for (int i = 0; i < ampHour.length; i++) {
			String a = ampHour[i] + " ";
			a = a.replace(".", ",");
			System.out.println(a);
		}
		System.out.print("\nAG Linear --> ");
		for (int i = 0; i < ampHour.length; i++) {
			String a = meanOutput[i] + " ";
			a = a.replace(".", ",");
			System.out.println(a);
		}
		System.out.print("\nAG Exp --> ");
		for (int i = 0; i < ampHour.length; i++) {
			String a = meanOutput2[i] + " ";
			a = a.replace(".", ",");
			System.out.println(a);
		}
		System.out.print("\nMQ Linear --> ");
		for (int i = 0; i < ampHour.length; i++) {
			String a = meanOutput3[i] + " ";
			a = a.replace(".", ",");
			System.out.println(a);
		}
		System.out.print("\nMQ Exp --> ");
		for (int i = 0; i < ampHour.length; i++) {
			String a = meanOutput4[i] + " ";
			a = a.replace(".", ",");
			System.out.println(a);
		}

		// plot
		plot(ampHour, meanOutput, meanOutput2, meanOutput3, meanOutput4);
		
		// plot 2
		double values[] = new double[4];
		String names[] = new String[4];
		values[0] = rsme;
		values[1] = rsme2;
		values[2] = rsme3;
		values[3] = rsme4;
		names[0] = "AG Linear";
		names[1] = "AG Exponencial";
		names[2] = "LS Linear";
		names[3] = "LS Exp";
		plot2(values, names, "Erro Medio Quadrático");

		double pccs[] = new double[4];
		pccs[0] = pcc;
		pccs[1] = pcc2;
		pccs[2] = pcc3;
		pccs[3] = pcc4;
		System.out.println("\n_________Pcc___________");
		for (int i = 0; i < pccs.length; i++) {
			System.out.println(names[i] + " --> " + pccs[i]);
		}
		plot2(pccs, names, "PCC");

		// bootstrap
		System.out.println("______________________");
		List<Double> meanT = new ArrayList<>();
		List<Double> meanT2 = new ArrayList<>();
		double mean = 0;
		int valueRand = 0;
		int b = 1000;
		// teste 1
		for (int i = 0; i < b; i++) {
			mean = 0;
			// valores rands
			for (int j = 0; j < vectorRSME[0].length; j++) {
				valueRand = rand.nextInt(vectorRSME[0].length);
				mean += vectorRSME[0][valueRand];
			}
			mean /= vectorRSME[0].length;
			meanT.add(mean);
		}
		// teste 2
		for (int i = 0; i < b; i++) {
			mean = 0;
			// valores rands
			for (int j = 0; j < vectorRSME[1].length; j++) {
				valueRand = rand.nextInt(vectorRSME[1].length);
				mean += vectorRSME[1][valueRand];
			}
			mean /= vectorRSME[1].length;
			meanT2.add(mean);
		}

		// ordenado
		Collections.sort(meanT);
		Collections.sort(meanT2);

		// intervalo de confianca
		double a = 0.1;
		int q1 = (int) (b * a / 2);
		int q2 = b - q1 + 1;
		System.out.println("\n____________Teste ______________");
		System.out.println("q1-->" + q1 + "   mean-->" + meanT.get(q1));
		System.out.println("q2-->" + q2 + "   mean-->" + meanT.get(q2));
		System.out.println("____________Teste 2______________");
		System.out.println("nq1-->" + q1 + "   mean-->" + meanT2.get(q1));
		System.out.println("q2-->" + q2 + "   mean-->" + meanT2.get(q2));
		System.out.println("________R________");
		for (int i = 0; i < pccs.length; i++) {
			
		}
	}

	public static void plot(double[] ampHour, double[] meanOutput, double[] meanOutput2, double[] meanOutput3,
			double[] meanOutput4) {

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
}
