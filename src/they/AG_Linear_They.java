package they;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import Algoritmo_Genetico.AlgoritmoGeneticoReal;

public class AG_Linear_They {
	
	private double[]bestIndividual;
	private double[]bestIndFit;
	private double rsme;
	private double vectorOutput[];
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

				{ 320, 240, 15}, { 320, 240, 30}, { 320, 240, 60}, { 720, 480, 15},
				{ 720, 480, 30}, { 720, 480, 60}, { 1920, 1080, 15}, { 1920, 1080, 30},
				{ 1920, 1080, 60}
		};
		double[][] conf = new double[conf1.length][conf1[0].length];
		for (int i = 0; i < conf.length; i++) {
			for (int j = 0; j < conf[0].length; j++) {
				conf[i][j] = normaliza(conf1[i][j], 15, 1920);
			}
		}
		// ampere hora 
		double[] ampHour =  { 0.046, 0.050, 0.056, 0.052, 0.053, 0.061, 0.054, 0.064, 0.090 };
		
		AlgoritmoGeneticoReal t = new AlgoritmoGeneticoReal();
		int cont = 0;
		double[] pc = {0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8};
		double[] pm = {0.01,0.03,0.05,0.1,0.3,0.5,0.7,0.8};
		
		String [] ps = new String[pc.length*pm.length];
		for (int i = 0; i < pc.length; i++) {
			for (int j = 0; j < pm.length; j++) {
				ps[cont] = new String("cros = "+pc[i]+" mult = "+pm[j]);
				cont++;
			}
			
		}
	
		double[][] bestIndFit10 = new double[100][pc.length*pm.length];
		double[] bestIndFitMedia = new double[64];
		for (int k = 0; k < bestIndFit10.length; k++) {
			cont = 0;
			for (int i = 0; i < pc.length; i++) {
				
				for (int j = 0; j < pm.length; j++) {
					t = new AlgoritmoGeneticoReal();
					t.startLinear(conf, ampHour,20,200,pc[i],pm[j]);
					bestIndFit10[k][cont] = t.getBestIndFit()[t.getBestIndFit().length-1];
					cont++;
				}
			}

		}
		
		/*for (int i = 0; i < bestIndFit10.length; i++) {
			for (int j = 0; j < bestIndFit10[0].length; j++) {
				System.out.print(bestIndFit10[i][j]+" ");
			}
			System.out.println();
		}*/
		
		
		for (int i = 0; i < bestIndFit10[0].length; i++) {
			for (int j = 0; j < bestIndFit10.length; j++) {
				bestIndFitMedia[i]+= bestIndFit10[j][i];
			}
		}
		double max = 0;
		for (int i = 0; i < bestIndFitMedia.length; i++) {
			bestIndFitMedia[i] = bestIndFitMedia[i]/bestIndFit10.length;
			if(max<bestIndFitMedia[i]){
				max = bestIndFitMedia[i];
			}
			
		}
		String aux ;
		for (int i = 0; i < bestIndFitMedia.length; i++) {
			if(max == bestIndFitMedia[i]){
				System.out.print("--> "+" "+ps[i]+" |");
			}
			aux = new String(bestIndFitMedia[i]+"");
			aux = aux.replace(".", ",");
			System.out.println(aux);
		}
		System.out.println("----------------REsposta-----------");
		for (int contAmp = 0; contAmp < ampHour.length; contAmp++) {
			System.out.println("output --> "+t.getVectorOutput()[contAmp]+"  output D-->"+ampHour[contAmp]+" erro-->"+(t.getVectorOutput()[contAmp]-ampHour[contAmp]));
		}

		/*Plot2DPanel plot = new Plot2DPanel();
		double x[] = new double[ampHour.length];
		for (int i = 0; i < x.length; i++) {
			x[i] = i + 1;
		}
		System.out.println("EQM__>"+t.rsme);
		plot.addLinePlot("A/H", x, ampHour);
		plot.addLinePlot("A/H", x, t.vectorOutput);
		JFrame frame = new JFrame("Output Linear");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(plot);
		frame.setSize(700, 500);
		frame.setVisible(true);*/
		
		Plot2DPanel plot = new Plot2DPanel();
		double x[] = new double[bestIndFitMedia.length];
		for (int i = 0; i < x.length; i++) {
			x[i]=i+1;			
		}
		
		plot.addLinePlot("EMQ", x,bestIndFitMedia);
		JFrame frame = new JFrame("Output Linear AG");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(plot);
		frame.setSize(700, 500);
		frame.setVisible(true);
	}

	public void run(double[][] conf,double[] ampHour,int lengthPopulation,int geracao) {
		
		// function linear
		AlgoritmoGeneticoReal ag = new AlgoritmoGeneticoReal();

		double rsme = 0;// erro medio quadratico
		double output = 0;// saida do modelo linear
		double[] vectorRMSE = new double[lengthPopulation];// vetor dos erros
															// medios
															// quadraticos
		double[][] population = ag.createPopulation(lengthPopulation, conf[0].length, -1, 1);
		double[][] populationCruzada;
		double[][] populationMudata;
		double[][] populationSelection;
		double[][] populationEletismo;
		double[] fitness = null;
		vectorOutput = new double[ampHour.length];
		bestIndFit = new double[geracao];
		for (int contGerecao = 0; contGerecao < geracao; contGerecao++) {
			if (contGerecao > 0) {
				populationSelection = ag.selection(fitness, population, 3);
				populationCruzada = ag.crossoverAritmetico(populationSelection, 0.7);
				populationMudata = ag.mutacaoUniforme(populationCruzada, 0.3, -1, 1);
				populationEletismo = ag.eletismo(bestIndividual, bestIndFit[contGerecao - 1], populationMudata,
						fitness);
				population = populationEletismo;
			}
			vectorRMSE = new double[lengthPopulation];
			for (int contPopulation = 0; contPopulation < population.length; contPopulation++) {
				rsme = 0;
				for (int contAmp = 0; contAmp < ampHour.length; contAmp++) {
					output = 0;
					for (int i = 0; i < conf[0].length; i++) {
						output += population[contPopulation][i] * conf[contAmp][i];
					}
					rsme += Math.pow(ampHour[contAmp] - output, 2);
				}
				rsme /= ampHour.length;
				vectorRMSE[contPopulation] = rsme;
			}
			fitness = ag.fitness(vectorRMSE);
			bestIndividual = ag.bestIndividual(population, fitness);
			bestIndFit[contGerecao] = ag.bestIndividualFit(fitness);
		}
		rsme = 0;
		for (int contAmp = 0; contAmp < ampHour.length; contAmp++) {
			output = 0;
			for (int i = 0; i < conf[0].length; i++) {
				output += bestIndividual[i] * conf[contAmp][i];
			}
			vectorOutput[contAmp] = output;
			rsme += Math.pow(ampHour[contAmp] - output, 2);
		}
		this.rsme = rsme / ampHour.length;
	}
	
	public double getRsme() {
		return rsme;
	}

	public void setRsme(double rsme) {
		this.rsme = rsme;
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
