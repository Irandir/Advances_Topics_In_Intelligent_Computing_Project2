package teste;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import Algoritmo_Genetico.AlgoritmoGeneticoReal;

public class Teste2We {
	private double[] bestIndividual = null;
	private double bestIndFit[];
	private double vectorOutput[];
	private double rsme=0;

	public static double normaliza(double dadoNormal, double min, double max) {
		return (dadoNormal - min) / (max - min);
	}

	private static double desnormaliza(double dadoNormalizado, double min, double max) {
		return dadoNormalizado * (max - min) + min;
	}
	
	public static void main(String[] args) {

		// configurations
		// resolution, fps, taxa de bit,largura de banda
		double[][] conf1 = {

				{ 320, 240, 15, 256 }, 
				{ 320, 240, 30, 256 }, 
				{ 320, 240, 60, 256 },
				
				{ 320, 240, 15, 512}, 
				{ 320, 240, 30, 512}, 
				{ 320, 240, 60, 512},
				
				{ 720, 480, 15, 256},
				{ 720, 480, 30, 256}, 
				{ 720, 480, 60, 256}, 
				
				{ 720, 480, 15, 512 },
				{ 720, 480, 30, 512 }, 
				{ 720, 480, 60, 512 }, 
				
				{ 1920, 1080, 15, 256 }, 
				{ 1920, 1080, 30, 256},
				{ 1920, 1080, 60, 256},
				
				{ 1920, 1080, 15, 512 }, 
				{ 1920, 1080, 30, 512 },
				{ 1920, 1080, 60, 512 }
		};
		double[][] conf = new double[conf1.length][conf1[0].length];
		for (int i = 0; i < conf.length; i++) {
			for (int j = 0; j < conf[0].length; j++) {
				conf[i][j] = normaliza(conf1[i][j], 15, 1920);
			}
		}
		// ampere hora 
		double[] ampHour = { 0.422, 0.446, 0.522, 0.158, 0.662, 0.368, 0.630, 0.276, 0.430, 0.528, 0.450, 0.410, 0.490, 0.452, 0.324, 0.648, 0.378, 0.416 };
		
		Teste2We t = new Teste2We();
		t.run(conf, ampHour,20,1000);
		for (int i = 0; i < t.bestIndFit.length; i++) {
			System.out.println(i+" "+t.bestIndFit[i]);
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
		System.out.println("EMQ-->"+t.rsme);
		plot.addLinePlot("A/H", x,ampHour);
		plot.addLinePlot("A/H", x,t.vectorOutput);
		JFrame frame = new JFrame("Output Exponencial");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(plot);
		frame.setSize(700, 500);
		frame.setVisible(true);
	}
	
	public void run(double[][] conf,double[] ampHour,int lengthPopulation,int geracao) {
	
		//function expenencial
		
		AlgoritmoGeneticoReal ag = new AlgoritmoGeneticoReal();
		double rsme = 0;//erro medio quadratico
		double exp = 0;
		double output = 0;//saida do modelo linear
		double[] vectorRMSE = new double[lengthPopulation];//vetor dos erros medios quadraticos 
		double[][] population = ag.createPopulation(lengthPopulation, conf[0].length+1, -1, 1);//+1 do k
		double[][] populationCruzada;
		double[][] populationMudata;
		double[][] populationSelection;
		double[][] populationEletismo;
		double[] fitness = null;
		vectorOutput = new double[ampHour.length];
		bestIndFit = new double[geracao];

		for (int contGerecao = 0; contGerecao < geracao; contGerecao++) {
			if (contGerecao > 0) {
				populationSelection = ag.selection(fitness, population, 4);
				populationCruzada = ag.crossoverAritmetico(populationSelection, 0.7);
				populationMudata = ag.mutacaoUniforme(populationCruzada, 0.2, -1, 1);
				populationEletismo = ag.eletismo(bestIndividual, bestIndFit[contGerecao - 1], populationMudata, fitness);
				population = populationEletismo;
			}
			vectorRMSE  = new double[lengthPopulation]; 
			for (int contPopulation = 0; contPopulation < population.length; contPopulation++) {
				rsme = 0;
				for (int contAmp = 0; contAmp < ampHour.length; contAmp++) {
					output = 0;
					exp = 0;
					for (int i = 0; i < conf[0].length; i++) {
						exp += population[contPopulation][i]* conf[contAmp][i]; 
					}
					output= population[contPopulation][population[0].length-1]*(Math.exp(exp));
					rsme += Math.pow(ampHour[contAmp]-output,2);
				}
				rsme/= ampHour.length;
				vectorRMSE[contPopulation] = rsme;
			}
			fitness = ag.fitness(vectorRMSE);
			bestIndividual = ag.bestIndividual(population, fitness);
			bestIndFit[contGerecao] = ag.bestIndividualFit(fitness);
		}	
		rsme = 0;
		for (int contAmp = 0; contAmp < ampHour.length; contAmp++) {
			output = 0;
			exp = 0;
			for (int i = 0; i < conf[0].length; i++) {
				exp += bestIndividual[i]* conf[contAmp][i]; 
			}
			output= bestIndividual[bestIndividual.length-1]*(Math.exp(exp));
			rsme += Math.pow(ampHour[contAmp] - output, 2);
			vectorOutput[contAmp] = output;
		}
		this.rsme = rsme / ampHour.length;
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

	public double getRsme() {
		return rsme;
	}

	public void setRsme(double rsme) {
		this.rsme = rsme;
	}
	
	
}
