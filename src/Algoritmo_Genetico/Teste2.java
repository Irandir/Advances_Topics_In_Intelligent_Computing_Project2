package Algoritmo_Genetico;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class Teste2 {
	private double[] bestIndividual = null;
	private double bestIndFit[] = new double[1000];
	private double vectorOutput[];

	public static double normaliza(double dadoNormal, double min, double max) {
		return (dadoNormal - min) / (max - min);
	}

	private static double desnormaliza(double dadoNormalizado, double min, double max) {
		return dadoNormalizado * (max - min) + min;
	}
	
	public static void main(String[] args) {

		// configurations
		// resolution, taxa de bits, fps, largura de banda
		double[][] conf1 = {

				{ 320, 240, 15, 20 }, { 320, 240, 30, 20 }, { 320, 240, 60, 20 }, { 720, 480, 15, 20 },
				{ 720, 480, 30, 20 }, { 720, 480, 60, 20 }, { 1920, 1080, 15, 20 }, { 1920, 1080, 30, 20 },
				{ 1920, 1080, 60, 20 }

		};
		double[][] configurations = new double[conf1.length][conf1[0].length];
		for (int i = 0; i < configurations.length; i++) {
			for (int j = 0; j < configurations[0].length; j++) {
				configurations[i][j] = normaliza(conf1[i][j], 15, 1920);
			}
		}
		// ampere hora
		double[] ampHour = { 0.046, 0.050, 0.056, 0.052, 0.053, 0.061, 0.054, 0.064, 0.090 };

		Teste2 t = new Teste2();
		t.run(configurations, ampHour);
		for (int i = 0; i < t.bestIndFit.length; i++) {
			System.out.println(t.bestIndFit[i]);
		}
		System.out.println("______________Population___________");
		for (int i = 0; i < t.bestIndividual.length; i++) {
			System.out.println(t.bestIndividual[i]);
		}

		System.out.println("----------------REsposta-----------");
		for (int contAmp = 0; contAmp < ampHour.length; contAmp++) {
			System.out.println("output --> "+t.vectorOutput[contAmp]+"  output D-->"+ampHour[contAmp]+" erro-->"+(t.vectorOutput[contAmp]-ampHour[contAmp]));
		}

		Plot2DPanel plot = new Plot2DPanel();
		double x[] = new double[ampHour.length];
		for (int i = 0; i < x.length; i++) {
			x[i]=i+1;			
		}
		
		plot.addLinePlot("A/H", x,ampHour);
		plot.addLinePlot("A/H", x,t.vectorOutput);
		JFrame frame = new JFrame("Output Exponencial");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(plot);
		frame.setSize(700, 500);
		frame.setVisible(true);
	}
	
	public void run(double[][] configurations,double[] ampHour) {
	
		//function expenencial
		
		AlgoritmoGeneticoReal ag = new AlgoritmoGeneticoReal();
		int lengthPopulation = 20;
		double rmse = 0;//erro medio quadratico
		double exp = 0;
		double output = 0;//saida do modelo linear
		double[] vectorRMSE = new double[lengthPopulation];//vetor dos erros medios quadraticos 
		double[][] population = ag.createPopulation(lengthPopulation, configurations[0].length+1, -1, 1);//+1 do k
		double[][] populationCruzada;
		double[][] populationMudata;
		double[][] populationSelection;
		double[][] populationEletismo;
		double[] fitness = null;
		vectorOutput = new double[ampHour.length];

		for (int contGerecao = 0; contGerecao < 1000; contGerecao++) {
			if (contGerecao > 0) {
				populationSelection = ag.selection(fitness, population, 3);
				populationCruzada = ag.crossoverAritmetico(populationSelection, 0.7);
				populationMudata = ag.mutacaoUniforme(populationCruzada, 0.3, -1, 1);
				populationEletismo = ag.eletismo(bestIndividual, bestIndFit[contGerecao - 1], populationMudata, fitness);
				population = populationEletismo;
			}
			vectorRMSE  = new double[lengthPopulation]; 
			for (int contPopulation = 0; contPopulation < population.length; contPopulation++) {
				rmse = 0;
				for (int contAmp = 0; contAmp < ampHour.length; contAmp++) {
					output = 0;
					exp = 0;
					for (int i = 0; i < configurations[0].length; i++) {
						exp += population[contPopulation][i]* configurations[contAmp][i]; 
					}
					output= population[contPopulation][population[0].length-1]*(Math.exp(exp));
					rmse += Math.pow(ampHour[contAmp]-output,2);
				}
				rmse/= ampHour.length;
				vectorRMSE[contPopulation] = rmse;
			}
			fitness = ag.fitness(vectorRMSE);
			bestIndividual = ag.bestIndividual(population, fitness);
			bestIndFit[contGerecao] = ag.bestIndividualFit(fitness);
		}	
		for (int contAmp = 0; contAmp < ampHour.length; contAmp++) {
			output = 0;
			exp = 0;
			for (int i = 0; i < configurations[0].length; i++) {
				exp += bestIndividual[i]* configurations[contAmp][i]; 
			}
			output= bestIndividual[bestIndividual.length-1]*(Math.exp(exp));
			vectorOutput[contAmp] = output;
		}
		
	}

	public double[] getBestIndividual() {
		return bestIndividual;
	}

	public void setBestIndividual(double[] bestIndividual) {
		this.bestIndividual = bestIndividual;
	}

	public double[] getBestIndFit() {
		return bestIndFit;
	}

	public void setBestIndFit(double[] bestIndFit) {
		this.bestIndFit = bestIndFit;
	}

	public double[] getVectorOutput() {
		return vectorOutput;
	}

	public void setVectorOutput(double[] vectorOutput) {
		this.vectorOutput = vectorOutput;
	}
	
	
}
