package outroA;

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

public class Teste30 {

	static Random rand = new Random();
	static double values[][] = new double[4][4];
	public double[][] input() {
		AG_Linear t = new AG_Linear();
		double[][] conf1 = { { 640,360, 30}, { 640,360, 60}, { 640, 360,30}, { 640,360, 60}, { 1280,720, 30},
				{ 1280, 720,60} };
		double[][] conf = new double[conf1.length][conf1[0].length];
		
		for (int i = 0; i < conf.length; i++) {
			for (int j = 0; j < conf[0].length; j++) {
				conf[i][j] = t.normaliza(conf1[i][j], 30, 1280);
			}
		}
		return conf;
	}

	public double[][] input2() {
		AG_Linear t = new AG_Linear();
		double[][] conf1 = { { 640, 640, 640, 640, 1280, 1280 }, { 360, 360, 360, 360, 720, 720 },
				{ 30, 60, 30, 60, 30, 60 }

		};
		double[][] conf = new double[conf1.length][conf1[0].length];
		for (int i = 0; i < conf.length; i++) {
			for (int j = 0; j < conf[0].length; j++) {
				conf[i][j] = t.normaliza(conf1[i][j], 30, 1280 );
			}
		}
		return conf;
	}

	public void all(double[][] input, double[][] input2, double[] ampHour, double[][] ampHour2,String nome,String nome2,int met) {
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
		AG_Linear teste = new AG_Linear();
		AG_EXP teste2 = new AG_EXP();
		MQLinear teste3 = new MQLinear();
		MQ_EXP teste4 = new MQ_EXP();

		for (int i = 0; i < 30; i++) {
			// teste 1
			teste = new AG_Linear();
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
			teste2 = new AG_EXP();
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

			// teste 3
			teste3 = new MQLinear();
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
			teste4 = new MQ_EXP();
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
		for (int i = 0; i < meanBestIndividual.length; i++) {
			System.out
					.println(meanBestIndividual[i] + "  " + meanBestIndividual2[i] + " " + meanP[i] + "  " + meanP2[i]);
		}
		System.out.println("					" + meanBestIndividual2[meanBestIndividual2.length - 1]
				+ "					" + meanP2[meanP2.length - 1] + "\n");

		System.out.println("AG linear rsme -->" + rsme);
		System.out.println("AG linear exp -->" + rsme2);
		System.out.println("Mínino Quadrático linear -->" + rsme3);
		System.out.println("Mínino Quadrático Exp -->" + rsme4);
		// plot
		plot(ampHour, meanOutput, meanOutput2, meanOutput3, meanOutput4,nome);
		// plot 2
		
		
		values[met][0] = rsme;
		values[met][1] = rsme2;
		values[met][2] = rsme3;
		values[met][3] = rsme4;
		
		
		/*double pccs[] = new double[4];
		pccs[0] = pcc;
		pccs[1] = pcc2;
		pccs[2] = pcc3;
		pccs[3] = pcc4;
		System.out.println("_________Pcc___________");
		for (int i = 0; i < pccs.length; i++) {
			System.out.println(names[i] + " --> " + pccs[i]);
		}
		//plot2(pccs, names, "PCC");*/
		
		// boottrap
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
	}

	public static void main(String[] args) {
		Teste30 teste30 = new Teste30();
		AG_Linear t = new AG_Linear();
		double[][] input = teste30.input();
		double[][] input2 = teste30.input2();

		//music
		double[] music = { 594.9, 698.1, 682.9, 783.7,786.4, 975.8};
		double[][] music2 = {{ 594.9, 698.1, 682.9, 783.7,786.4, 975.8}};
		double[] musicN = new double[music.length];
		double[][] musicN2 = new double[1][music.length];
		for (int i = 0; i < musicN .length; i++) {	
			musicN[i] = t.normaliza(music[i], 30, 1124.3);
			musicN2[0][i] = t.normaliza(music2[0][i],30, 1124.3);
		}
		
		// sports
		double[] sporta = { 652.4, 751.9,855.3, 953.2, 960.4, 1172.1 };
		double[][] sporta2 = { { 652.4, 751.9,855.3, 953.2, 960.4, 1172.1 }};
		double[] sportaN = new double[music.length];
		double[][] sportaN2 = new double[1][music.length];
		for (int i = 0; i < sporta.length; i++) {	
			sportaN[i] = t.normaliza(sporta[i], 30, 1172.1 );
			sportaN2[0][i] = t.normaliza(sporta2[0][i], 30, 1172.1 );
		}
		
		// game
		double[] game = { 628.2, 730.8, 786.5, 889.1,869.0, 1061.2  };
		double[][] game2 = { {628.2, 730.8, 786.5, 889.1,869.0, 1061.2 } };
		double[] gameN = new double[music.length];
		double[][] gameN2 = new double[1][music.length];
		for (int i = 0; i < sporta.length; i++) {	
			gameN[i] = t.normaliza(game[i], 30, 1124.3);
			gameN2[0][i] = t.normaliza(game2[0][i], 30, 1124.3);
		}
		
		// news
		double[] news = { 612.3, 725.5, 802.7, 915.7,901.3, 1124.3 };
		double[][] news2 = { { 612.3, 725.5, 802.7, 915.7,901.3, 1124.3 }};
		double[] newsN = new double[music.length];
		double[][] newsN2 = new double[1][music.length];
		for (int i = 0; i < sporta.length; i++) {	
			newsN[i] = t.normaliza(news[i], 30, 1124.3);
			newsN2[0][i] = t.normaliza(news2[0][i], 30, 1124.3);
		}
		
		teste30.all(input, input2, musicN, musicN2,"music","music EMQ",0);
		teste30.all(input, input2, sportaN, sportaN2,"sporta","sporta EMQ",1);
		teste30.all(input, input2, gameN, gameN2,"game","game EMQ",2);
		teste30.all(input, input2, newsN, newsN2,"news","news EMQ",3);
		
		System.out.println("___________EQM__________");
		for (int i = 0; i < values.length; i++) {
			System.out.println("_____________________");
			for (int j = 0; j < values[0].length; j++) {
				System.out.print(values[i][j]+" ");
			}
		}
		String names[] = new String[4];
		names[0] = "AG Linear";
		names[1] = "AG Exponencial";
		names[2] = "LS Linear";
		names[3] = "LS Exp";
		plot2(values, names, "Métrica de Avaliação");
	/*	String names[] = new String[4];
		values[met][0] = rsme;
		values[met][1] = rsme2;
		values[met][2] = rsme3;
		values[met][3] = rsme4;
		namesmet] = "AG Linear";
		names[1] = "AG Exponencial";
		names[2] = "LS Linear";
		names[3] = "LS Exp";
		plot2(values, names, nome2);*/
	}

	public static void plot(double[] ampHour, double[] meanOutput, double[] meanOutput2, double[] meanOutput3,
			double[] meanOutput4,String nome) {

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
		JFrame frame = new JFrame(nome);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(plot);
		frame.setSize(700, 500);
		frame.setVisible(true);
	}

	public static void plot2(double[][] value, String[] name, String titulo) {
		DefaultCategoryDataset dados = new DefaultCategoryDataset();
		int cont= 0;
		for (int i = 0; i < value.length; i++) {
			for (int j = 0; j < value[0].length; j++) {
				dados.addValue(value[i][j], cont+"", "");
				cont++;
			}
		}

		JFreeChart grafico = ChartFactory.createBarChart(titulo, "metodo", "Erro Médio Quadrático", dados, PlotOrientation.VERTICAL,
				true, true, true);
		CategoryPlot plot = (CategoryPlot) grafico.getPlot();
		CategoryItemRenderer itemRerender = plot.getRenderer();
		// Caso vc queira mudar a cor das barras
		itemRerender.setSeriesPaint(0, Color.RED);
		itemRerender.setSeriesPaint(1, Color.GREEN);
		itemRerender.setSeriesPaint(2, Color.YELLOW);
		itemRerender.setSeriesPaint(3, Color.BLACK);
		//
		itemRerender.setSeriesPaint(4, Color.RED);
		itemRerender.setSeriesPaint(5, Color.GREEN);
		itemRerender.setSeriesPaint(6, Color.YELLOW);
		itemRerender.setSeriesPaint(7, Color.BLACK);
		//
		itemRerender.setSeriesPaint(8, Color.RED);
		itemRerender.setSeriesPaint(9, Color.GREEN);
		itemRerender.setSeriesPaint(10, Color.YELLOW);
		itemRerender.setSeriesPaint(11, Color.BLACK);
		//
		itemRerender.setSeriesPaint(12, Color.RED);
		itemRerender.setSeriesPaint(13, Color.GREEN);
		itemRerender.setSeriesPaint(14, Color.YELLOW);
		itemRerender.setSeriesPaint(15, Color.BLACK);
		
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
//mw
}
