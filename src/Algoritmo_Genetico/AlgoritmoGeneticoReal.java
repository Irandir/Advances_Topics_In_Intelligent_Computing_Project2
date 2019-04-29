package Algoritmo_Genetico;

import java.util.Random;
import java.util.stream.DoubleStream;

public class AlgoritmoGeneticoReal {

	private Random rand = new Random();
	
	private double[] vectorOutput;
	private double[] bestIndividual;
	double[] bestIndFit;
	
public double startLinear(double[][] conf,double[] ampHour,int lengthPopulation,int geracao,double probCross,double probMult) {
		
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
				populationCruzada = ag.crossoverAritmetico(populationSelection, probCross);
				populationMudata = ag.mutacaoUniforme(populationCruzada, probMult, -1, 1);
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
		rsme = rsme / ampHour.length;
		return rsme;
	}
	
public double startPotencial(double[][] conf,double[] ampHour,int lengthPopulation,int geracao,double probCross,double probMult) {
	
	// function linear
	AlgoritmoGeneticoReal ag = new AlgoritmoGeneticoReal();

	double rsme = 0;// erro medio quadratico
	double output = 0;// saida do modelo linear
	double[] vectorRMSE = new double[lengthPopulation];// vetor dos erros
														// medios
														// quadraticos
	double[][] population = ag.createPopulation(lengthPopulation, conf[0].length*2, -1, 1);
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
			populationCruzada = ag.crossoverAritmetico(populationSelection, probCross);
			populationMudata = ag.mutacaoUniforme(populationCruzada, probMult, -1, 1);
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
					output += population[contPopulation][i] * Math.pow(conf[contAmp][i],population[contPopulation][i+conf[0].length-1]);
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
	rsme = rsme / ampHour.length;
	return rsme;
}

	public double startEXP(double[][] conf,double[] ampHour,int lengthPopulation,int geracao,double probCross,double probMult) {
		
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
		bestIndividual = new double[1];

		for (int contGerecao = 0; contGerecao < geracao; contGerecao++) {
			if (contGerecao > 0) {
				populationSelection = ag.selection(fitness, population, 4);
				populationCruzada = ag.crossoverAritmetico(populationSelection, probCross);
				populationMudata = ag.mutacaoUniforme(populationCruzada, probMult, -1, 1);
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
		rsme = rsme / ampHour.length;
		return rsme;
	}
	
	public double[] getBestIndividual() {
		return bestIndividual;
	}

	public void setBestIndividual(double[] bestIndividual) {
		this.bestIndividual = bestIndividual;
	}

	public double[][]createPopulation(int lengthPopulation, int lengthGene,double min,double max) {
		if (lengthPopulation % 2 != 0) {
			lengthPopulation++;
		}
		double[][] population = new double[lengthPopulation][lengthGene];
		for (int i = 0; i < population.length; i++) {
			for (int j = 0; j < population[0].length; j++) {
				population[i][j] = valueRandom(min, max);
			}
		}
		return population;
	}
	
	public double[]fitness(double[]value){
		double []fitness = new double[value.length];
		for (int i = 0; i < fitness.length; i++) {
			fitness[i] = 1000/(1+value[i]);
		}
		return fitness;
	}

	//selecao torneio
	public double[][] selection(double[] fitness, double[][] population,int lengthTournamentWindow) {
		double populationSelection[][] = new double[population.length][population[0].length];
		int indice = 0;
		int ind = 0;
		double aux = 0;
		for (int i = 0; i < fitness.length; i++) {
			indice = rand.nextInt(fitness.length);
			aux = fitness[indice];
			ind = indice;
			for (int j = 0; j < lengthTournamentWindow; j++) {
				indice = rand.nextInt(fitness.length);
				if (aux < fitness[indice]) {
					aux = fitness[indice];
					ind = indice;
				}	
			}
			for (int j = 0; j < populationSelection[0].length; j++) {
				populationSelection[i][j] = population[ind][j];
			}
		}
		return populationSelection;
	}
	//melhor individuo
	public double[] bestIndividual(double population[][],double fitness[]){
		int indice = 0;
		double value = fitness[0];
		for (int i = 0; i < fitness.length; i++) {
			if (value < fitness[i]) {
				value = fitness[i];
				indice = i;
			}
		}
		double bestIndividual[] = new double[population[0].length];
		for (int i = 0; i < population[0].length; i++) {
			bestIndividual[i] = population[indice][i];
		}
		return bestIndividual;
	}
	//melhor individuo fit
	public double bestIndividualFit(double fitness[]){
		double value = fitness[0];
		for (int i = 0; i < fitness.length; i++) {
			if (value < fitness[i]) {
				value = fitness[i];
			}
		}
		return value;
	}
	// eletismo
	public double[][] eletismo(double[] bestIndividual,double beastFitness,double population[][],double[]fitness){
		int indice = 0;
		double value = Double.MAX_VALUE;
		for (int i = 0; i < fitness.length; i++) {
			if (value > fitness[i]) {
				value = fitness[i];
				indice = i;
			}
		}
		double populationPosEletismo[][] = new double[population.length][population[0].length];
		for (int i = 0; i < populationPosEletismo.length; i++) {
			for (int j = 0; j < population[0].length; j++) {
				populationPosEletismo[i][j] = population[i][j];
			}
		}
		for (int j = 0; j < population[0].length; j++) {
			populationPosEletismo[indice][j] = bestIndividual[j];
		}
		return populationPosEletismo;
	}
	
	// crossover aritmético
	public double[][] crossoverAritmetico(double populacaoSelecionado[][], double probDoCrossover) {
		double populationPosCrossover[][] = new double[populacaoSelecionado.length][populacaoSelecionado[0].length];
		double prob = 0;
		double a = 0;
		for (int i = 0; i < populationPosCrossover.length / 2; i++) {
			prob = rand.nextDouble();
			if (prob <= probDoCrossover) {
				a = rand.nextDouble();
				for (int j = 0; j < populationPosCrossover[0].length; j++) {
					populationPosCrossover[i * 2][j] = a * populacaoSelecionado[i * 2][j] + (1 - a) * populacaoSelecionado[i * 2 + 1][j];
					populationPosCrossover[i * 2 + 1][j] = (1 - a) * populacaoSelecionado[i * 2][j]+ a * populacaoSelecionado[i * 2 + 1][j];
				}
			} else {
				for (int j = 0; j < populationPosCrossover[0].length; j++) {
					populationPosCrossover[i * 2][j] = populacaoSelecionado[i * 2][j];
					populationPosCrossover[i * 2 + 1][j] = populacaoSelecionado[i * 2 + 1][j];
				}

			}
		}
		return populationPosCrossover;
	}

	// Mutação Uniforme: x’ = x + M
	public double[][] mutacaoUniforme(double populationPosCruzamento[][], double probMutacao, double min, double max) {
		double[][] populationPosMutacao = new double[populationPosCruzamento.length][populationPosCruzamento[0].length];
		double prob = 0;
		for (int i = 0; i < populationPosMutacao.length; i++) {
			for (int j = 0; j < populationPosMutacao[0].length; j++) {
				prob = rand.nextDouble();
				if (prob <= probMutacao) {
					populationPosMutacao[i][j] = populationPosCruzamento[i][j]+valueRandom(min,max);
				}else{
					populationPosMutacao[i][j] = populationPosCruzamento[i][j];
				}
			}
		}
		return populationPosMutacao;
	}

	private double valueRandom(double min, double max) {
		DoubleStream r = rand.doubles(1,min, max);
		double[] a = r.toArray();
		return a[0];
	}

	public double[] getVectorOutput() {
		return vectorOutput;
	}

	public void setVectorOutput(double[] vectorOutput) {
		this.vectorOutput = vectorOutput;
	}

	public double[] getBestIndFit() {
		return bestIndFit;
	}

	public void setBestIndFit(double[] bestIndFit) {
		this.bestIndFit = bestIndFit;
	}
	
}
