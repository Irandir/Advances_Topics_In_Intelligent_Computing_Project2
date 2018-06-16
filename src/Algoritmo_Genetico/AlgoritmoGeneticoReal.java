package Algoritmo_Genetico;

import java.util.Random;
import java.util.stream.DoubleStream;

public class AlgoritmoGeneticoReal {

	private Random rand = new Random();

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
}
