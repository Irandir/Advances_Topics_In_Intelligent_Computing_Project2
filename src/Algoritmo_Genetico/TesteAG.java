package Algoritmo_Genetico;

public class TesteAG {
	public static void main(String[] args) {
		double[][] input = {{1,1},{1,2},{1,3},{1,4}};
		
		// ampere hora
		double[] ampHour = { 200,400,600,800};
		
		
		AlgoritmoGeneticoReal ag = new AlgoritmoGeneticoReal();
		int lengthPopulation = 20;
		double rmse = 0;//erro medio quadratico
		double exp = 0;
		double output = 0;//saida do modelo linear
		double[] vectorRMSE = new double[lengthPopulation];//vetor dos erros medios quadraticos 
		double[][] population = ag.createPopulation(lengthPopulation, input[0].length, -1, 1);//+1 do k
		double[][] populationCruzada;
		double[][] populationMudata;
		double[][] populationSelection;
		double[][] populationEletismo;
		double[] fitness = null;
		double[] vectorOutput = new double[ampHour.length];
		double[] bestIndividual = null;
		double bestIndFit[] = new double[1000];
		for (int contGerecao = 0; contGerecao < bestIndFit.length; contGerecao++) {
			if (contGerecao > 0) {
				populationSelection = ag.selection(fitness, population, 4);
				populationCruzada = ag.crossoverAritmetico(populationSelection, 0.7);
				populationMudata = ag.mutacaoUniforme(populationCruzada, 0.2, -1, 1);
				populationEletismo = ag.eletismo(bestIndividual, bestIndFit[contGerecao - 1], populationMudata, fitness);
				population = populationEletismo;
			}
			vectorRMSE  = new double[lengthPopulation]; 
			for (int contPopulation = 0; contPopulation < population.length; contPopulation++) {
				rmse = 0;
				for (int contAmp = 0; contAmp < ampHour.length; contAmp++) {
					output = 0;
					for (int i = 0; i < input[0].length; i++) {
						output+= population[contPopulation][i]* input[contAmp][i]; 
					}
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
			for (int i = 0; i < input[0].length; i++) {
				output += bestIndividual[i]* input[contAmp][i]; 
			}
			vectorOutput[contAmp] = output;
		}
		for (int i = 0; i < bestIndFit.length; i++) {
			System.out.println(i+" "+bestIndFit[i]);
		}
		System.out.println("______________Population___________");
		for (int i = 0; i < bestIndividual.length; i++) {
			System.out.println(bestIndividual[i]);
		}

		System.out.println("----------------REsposta-----------");
		for (int contAmp = 0; contAmp < ampHour.length; contAmp++) {
			System.out.println("output --> "+vectorOutput[contAmp]+"  output D-->"+ampHour[contAmp]+" erro-->"+(vectorOutput[contAmp]-ampHour[contAmp]));
		}
	}
}
