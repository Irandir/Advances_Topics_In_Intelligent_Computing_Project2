package Algoritmo_Genetico;

public class Teste {
	
	//values are fitconal
	public double[] date(){
		double []dates = new double[9];
		return dates;
	}
	
	public static void main(String[] args) {
		//configurations
		//resolution, taxa de bits, fps, largura de banda
		double[][] conf = {
				//resolution      |fps      | taxa de bits
				//320x240 == 1    |15  == 1 | 10 == 1
				//720x480 == 2    |30  == 2 | 20 == 2
				//1920x1080 == 3  |60  == 3 |
				{1, 1, 1},
				{2, 1, 2},
				{3, 1, 3},
				{1, 1, 1},
				{2, 1, 2},
				{3, 1, 3},
				{1, 1, 1},
				{2, 1, 2},
				{3, 1, 3},
				//taxa bits alterada
				{1, 2, 1},
				{2, 2, 2},
				{3, 2, 3},
				{1, 2, 1},
				{2, 2, 2},
				{3, 2, 3},
				{1, 2, 1},
				{2, 2, 2},
				{3, 2, 3},
		};
		//ampere hora
		double [] ampHour = {0.046,0.050,0.056,0.052,0.053,0.061,0.054,0.064,0.090,
				0.046,0.050,0.056,0.052,0.053,0.061,0.054,0.064,0.090};/*{46,50,56,52,53,61,54,64,90,46,50,56,52,53,61,54,64,90};*/
		//function linear
		
		AlgoritmoGeneticoReal ag = new AlgoritmoGeneticoReal();
		int lengthPopulation = 20;
		double rmse = 0;//erro medio quadratico
		double output = 0;//saida do modelo linear
		double[] vectorRMSE = new double[lengthPopulation];//vetor dos erros medios quadraticos 
		double[][] population = ag.createPopulation(lengthPopulation, conf[0].length, -1, 1);
		double[][] populationCruzada;
		double[][] populationMudata;
		double[][] populationSelection;
		double[][] populationEletismo;
		double[] fitness = null;
		double[] bestIndividual = null;
		double bestIndFit[] = new double[20000];

		for (int contGerecao = 0; contGerecao < 20000; contGerecao++) {
			if (contGerecao > 0) {
				populationSelection = ag.selection(fitness, population, 3);
				populationCruzada = ag.crossoverAritmetico(populationSelection, 0.7);
				populationMudata = ag.mutacaoUniforme(populationCruzada, 0.1, -1, 1);
				populationEletismo = ag.eletismo(bestIndividual, bestIndFit[contGerecao - 1], populationMudata, fitness);
				population = populationEletismo;
			}
			vectorRMSE  = new double[lengthPopulation]; 
			for (int contPopulation = 0; contPopulation < population.length; contPopulation++) {
				rmse = 0;
				for (int contAmp = 0; contAmp < ampHour.length; contAmp++) {
					output = 0;
					for (int i = 0; i < conf[0].length; i++) {
						output += population[contPopulation][i]* conf[contAmp][i]; 
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
		for (int i = 0; i < bestIndFit.length; i++) {
			System.out.println(bestIndFit[i]);
		}
		System.out.println("______________P___________");
		for (int i = 0; i < bestIndividual.length; i++) {
			System.out.println(bestIndividual[i]);
		}
		System.out.println("----------------REsposta-----------");
		for (int contAmp = 0; contAmp < ampHour.length; contAmp++) {
			output = 0;
			for (int i = 0; i < conf[0].length; i++) {
				output += bestIndividual[i]* conf[contAmp][i]; 
			}
			System.out.println(output);
		}
	}
}
