package they;

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


public class Teste30They {

	public double[][] input() {
		TesteThey t = new TesteThey();
		double[][] conf1 = {

				{ 320, 240, 15}, { 320, 240, 30}, { 320, 240, 60}, { 720, 480, 15},
				{ 720, 480, 30}, { 720, 480, 60}, { 1920, 1080, 15}, { 1920, 1080, 30},
				{ 1920, 1080, 60}

		};
		double[][] conf = new double[conf1.length][conf1[0].length];
		for (int i = 0; i < conf.length; i++) {
			for (int j = 0; j < conf[0].length; j++) {
				conf[i][j] = t.normaliza(conf1[i][j], 15, 1920);
			}
		}
		return conf;
	}
	
	public double[][] input2(){
		TesteThey t = new TesteThey();
		double[][] conf1 = {
				{ 320, 320, 320, 720, 720, 720, 1920, 1920, 1920 }, { 240, 240, 240, 480, 480, 480, 1080, 1080, 1080 },
				{ 15, 30, 60, 15, 30, 60, 15, 30, 60 }

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
		double[][] ampHour2 = {{ 0.046, 0.050, 0.056, 0.052, 0.053, 0.061, 0.054, 0.064, 0.090 }};
		
		// teste 1
		double meanBestIndividual[] = new double[input[0].length];
		double meanBestIndFit = 0;
		double meanOutput[] = new double[input.length];
		double rsme = 0;
		// teste 2
		double meanBestIndividual2[] = new double[input[0].length + 1];
		double meanBestIndFit2 = 0;
		double meanOutput2[] = new double[input.length];
		double rsme2 = 0;
		//minimos quadraticos linear
		double meanP[] = new double[input[0].length];
		double meanOutput3[] = new double[ampHour2[0].length];
		double rsme3 = 0;
		// minimos quadraticos exp
		double meanP2[] = new double[input[0].length];
		double meanOutput4[] = new double[ampHour2[0].length];
		double rsme4 = 0;
		
		TesteThey teste = new TesteThey();
		Teste2They teste2 = new Teste2They();
		TesteMinimosQuadraticosLinearThey teste3 = new TesteMinimosQuadraticosLinearThey();
		TesteMinimosQuadraticosExponencialThey2 teste4 = new TesteMinimosQuadraticosExponencialThey2();
		
		for (int i = 0; i < 30; i++) {
			// teste 1
			teste = new TesteThey();
			teste.run(input, ampHour,20,1000);
			for (int j = 0; j < meanOutput.length; j++) {
				meanOutput[j] += teste.getVectorOutput()[j];
			}
			meanBestIndFit += teste.getBestIndFit()[teste.getBestIndFit().length - 1];
			for (int j = 0; j < meanBestIndividual.length; j++) {
				meanBestIndividual[j] += teste.getBestIndividual()[j];
			}
			rsme+=teste.getRsme();
			
			// teste 2
			teste2 = new Teste2They();
			teste2.run(input, ampHour,20,1000);
			for (int j = 0; j < meanOutput2.length; j++) {
				meanOutput2[j] += teste2.getVectorOutput()[j];
			}
			meanBestIndFit2 += teste2.getBestIndFit()[teste2.getBestIndFit().length - 1];
			for (int j = 0; j < meanBestIndividual2.length; j++) {
				meanBestIndividual2[j] += teste2.getBestIndividual()[j];
			}
			rsme2+=teste2.getRsme();
			
			teste3 = new TesteMinimosQuadraticosLinearThey();
			teste3.run(input2, ampHour2);
			for (int j = 0; j < meanOutput3.length; j++) {
				meanOutput3[j] += teste3.getVectorOutput()[j];
			}
			for (int j = 0; j < meanP.length; j++) {
				meanP[j] += teste3.getP()[j];
			}
			rsme3+=teste3.getRsme();
			
			teste4 = new TesteMinimosQuadraticosExponencialThey2();
			teste4.run(input2, ampHour2);
			for (int j = 0; j < meanOutput4.length; j++) {
				meanOutput4[j] += teste4.getVectorOutput()[j];
			}
			for (int j = 0; j < meanP2.length; j++) {
				meanP2[j] += teste4.getP()[j];
			}
			rsme4+=teste4.getRsme();
			
		}
		//divisão por 30
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
		rsme/=30;
		rsme2/=30;
		rsme3/=30;
		rsme4/=30;
		
		//prints
		System.out.println("Fitness do melhor individuo");
		System.out.println(meanBestIndFit);
		System.out.println(meanBestIndFit2);
		
		System.out.println("________P_________");
		for (int i = 0; i < meanBestIndividual.length; i++) {
			System.out.println(meanBestIndividual[i]+"  "+meanBestIndividual2[i]+" "+meanP[i]+"  "+meanP2[i]);
		}
		System.out.println("------------------- "+meanBestIndividual2[meanBestIndividual2.length-1]+"---------------\n");
		
		System.out.println("AG linear rsme -->"+rsme);
		System.out.println("AG linear exp -->"+rsme2);
		System.out.println("Mínino Quadrático linear -->"+rsme3);
		System.out.println("Mínino Quadrático Exp -->"+rsme4);
		//plot
		plot(ampHour, meanOutput, meanOutput2,meanOutput3,meanOutput4);
		//plot 2
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
		plot2(values,names);
	}
	
	public static void plot(double[] ampHour,double []meanOutput,double []meanOutput2,double []meanOutput3,double []meanOutput4){

		Plot2DPanel plot = new Plot2DPanel();
		
		double x[] = new double[ampHour.length];
		for (int i = 0; i < x.length; i++) {
			x[i] = i + 1;
		}
		plot.addLinePlot("Real", x, ampHour);
		plot.addLinePlot("AG Linear", x, meanOutput);
		plot.addLinePlot("AG Exponencial", x, meanOutput2);
		plot.addLinePlot("LS Linear", x, meanOutput3);
		plot.addLinePlot("LS Exp",Color.BLACK ,x, meanOutput4);
		JFrame frame = new JFrame("Output das 30 execuções");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(plot);
		frame.setSize(700, 500);
		frame.setVisible(true);
	}
	
	public static void plot2(double[] value,String[] name){
		DefaultCategoryDataset dados = new DefaultCategoryDataset();
		for (int i = 0; i < value.length; i++) {
			dados.addValue(value[i],name[i],"");
		}

		JFreeChart grafico = ChartFactory.createBarChart("", "metodo", "Saída", dados, PlotOrientation.VERTICAL,
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
}
