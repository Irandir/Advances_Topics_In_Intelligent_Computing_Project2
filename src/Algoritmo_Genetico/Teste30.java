package Algoritmo_Genetico;

import java.awt.Color;
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

public class Teste30 {

	public double[][] input() {
		Teste t = new Teste();
		double[][] conf1 = {

				{ 320, 240, 15, 20 }, { 320, 240, 30, 20 }, { 320, 240, 60, 20 }, { 720, 480, 15, 20 },
				{ 720, 480, 30, 20 }, { 720, 480, 60, 20 }, { 1920, 1080, 15, 20 }, { 1920, 1080, 30, 20 },
				{ 1920, 1080, 60, 20 }

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
		Teste30 teste30 = new Teste30();

		double[][] input = teste30.input();
		double[] ampHour = { 0.046, 0.050, 0.056, 0.052, 0.053, 0.061, 0.054, 0.064, 0.090 };

		// teste 1
		double meanBestIndividual[] = new double[input[0].length];
		double meanBestIndFit = 0;
		double meanOutput[] = new double[input.length];

		// teste 2
		double meanBestIndividual2[] = new double[input[0].length + 1];
		double meanBestIndFit2 = 0;
		double meanOutput2[] = new double[input.length];

		Teste teste = new Teste();
		Teste2 teste2 = new Teste2();
		for (int i = 0; i < 30; i++) {
			// teste 1
			teste = new Teste();
			teste.run(input, ampHour);
			for (int j = 0; j < meanOutput.length; j++) {
				meanOutput[j] += teste.getVectorOutput()[j];
			}
			meanBestIndFit += teste.getBestIndFit()[teste.getBestIndFit().length - 1];
			for (int j = 0; j < meanBestIndividual.length; j++) {
				meanBestIndividual[j] += teste.getBestIndividual()[j];
			}

			// teste 2
			teste2 = new Teste2();
			teste2.run(input, ampHour);
			for (int j = 0; j < meanOutput2.length; j++) {
				meanOutput2[j] += teste2.getVectorOutput()[j];
			}
			meanBestIndFit2 += teste2.getBestIndFit()[teste2.getBestIndFit().length - 1];
			for (int j = 0; j < meanBestIndividual2.length; j++) {
				meanBestIndividual2[j] += teste2.getBestIndividual()[j];
			}
		}
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
		System.out.println("Fitness do melhor individuo");
		System.out.println(meanBestIndFit);
		System.out.println(meanBestIndFit2);
		plot(ampHour, meanOutput, meanOutput2);
		//plot2(meanBestIndividual,meanBestIndividual2);
	}
	
	public static void plot(double[] ampHour,double []meanOutput,double []meanOutput2){

		Plot2DPanel plot = new Plot2DPanel();
		
		double x[] = new double[ampHour.length];
		for (int i = 0; i < x.length; i++) {
			x[i] = i + 1;
		}
		plot.addLinePlot("Real", x, ampHour);
		plot.addLinePlot("Linear", x, meanOutput);
		plot.addLinePlot("Exponecial", x, meanOutput2);
		JFrame frame = new JFrame("Output das 30 execuções");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(plot);
		frame.setSize(700, 500);
		frame.setVisible(true);
	}
	
	public static void plot2(double[] value,double []value2){
		DefaultCategoryDataset dados = new DefaultCategoryDataset();
		for (int i = 0; i < value.length; i++) {
			dados.addValue(value[i],""+i,""+(i*2));
		}
		for (int i = 0; i < value2.length; i++) {
			dados.addValue(value2[i],""+i,""+(i*2+1));
		}

		JFreeChart grafico = ChartFactory.createBarChart("", "metodo", "Saída", dados, PlotOrientation.VERTICAL,
				true, true, true);
		CategoryPlot plot = (CategoryPlot) grafico.getPlot();
		CategoryItemRenderer itemRerender = plot.getRenderer();
		//Caso vc queira mudar a cor das barras
		itemRerender.setSeriesPaint(0, Color.blue);
		itemRerender.setSeriesPaint(1, Color.RED);

		JFrame frame = new JFrame();
		frame.add(new ChartPanel(grafico));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
