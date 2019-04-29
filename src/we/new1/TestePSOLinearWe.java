package we.new1;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import pso.PSO;

public class TestePSOLinearWe {
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
		double[] ampHour =  {

				16.424, 17.008, 17.336, 16.854, 16.402, 15.726,

				15.55, 16.452, 16.11, 16.458, 17.86, 15.612,

				15.45, 16.768, 14.282, 15.854, 16.03, 16.976 };
		
		double[] ampHour2 =new double[ampHour.length];
		for (int j = 0; j < ampHour2.length; j++) {
			ampHour2[j] = normaliza(ampHour[j], 13, 17.86);
		}

		PSO pso = new PSO(conf, ampHour2, 200, 2, 2, 1, 6, 2,0);
		double[] output = pso.start(200);

		TestePSOLinearWe t = new TestePSOLinearWe();
		
		for (int i = 0; i < pso.getgBestFitness().length; i++) {
			System.out.println(pso.getgBestFitness()[i]);
		}
		
		/*for (int i = 0; i < t.bestIndFit.length; i++) {
			System.out.println(i+" "+t.bestIndFit[i]);
		}
		System.out.println("______________Population___________");
		for (int i = 0; i < t.bestIndividual.length; i++) {
			System.out.println(t.bestIndividual[i]);
		}

		System.out.println("----------------REsposta-----------");
		for (int contAmp = 0; contAmp < ampHour.length; contAmp++) {
			System.out.println("output --> "+t.vectorOutput[contAmp]+"  output D-->"+ampHour[contAmp]+" erro-->"+(t.vectorOutput[contAmp]-ampHour[contAmp]));
		}*/

		Plot2DPanel plot = new Plot2DPanel();
		double x[] = new double[ampHour2.length];
		for (int i = 0; i < x.length; i++) {
			x[i]=i+1;			
		}
		
		plot.addLinePlot("A/H", x,ampHour2);
		plot.addLinePlot("A/H", x,output);
		JFrame frame = new JFrame("Output PSO LINEAR");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(plot);
		frame.setSize(700, 500);
		frame.setVisible(true);
	}
	
	/*public void run(double[][] conf,double[] ampHour,int lengthPopulation,int geracao) {
	
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
	}*/

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
