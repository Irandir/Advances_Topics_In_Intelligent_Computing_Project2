package pso;

public class PSO {
	double[][] base;

	double[][] input; /* base */
	double[] output; /* saida */
	
	double[][] particle;		 /* População */
	double[][] velocity;   		/* Velocidade de ajuste */
	double[] particleFitness;      /* Erro quadratico medio*/
	double[][] pBest;      		/* Memoria anterior */
	double[] pBestFitness;
	double[] gBest;        		/* melhor particula*/
	double[] gBestFitness;
	double c1,c2;
	//double min,max;
	int tipo = 0;
	double wInertia;
	double maxInertia;
	double minInertia;
	

	public PSO(double[][] input, double[] output,int populationSize, double c1, double c2, 
			 double wInertia, double maxInertia,double minInertia,int tipo) {
		super();
		
		this.input = input;
		this.output = output;
		this.c1 = c1;
		this.c2 = c2;
		this.wInertia = wInertia;
		this.maxInertia = maxInertia;
		this.minInertia = minInertia;
		//this.min = min;
		//this.max = max;
		this.tipo = tipo;
		//depepende do tipo de modelo exp,linear e extc
		int weights;
		if(tipo==0){
			weights = input[0].length;//linear
		}else{
			weights = input[0].length+1;//exp
		}
		
		this.particle = new double[populationSize][weights];
		this.velocity = new double[populationSize][weights];
		this.pBest = new double[populationSize][ weights];
		this.gBest = new double[weights];
		this.particleFitness = new double[populationSize];
		this.pBestFitness = new double[populationSize];
		
		this.generatePopulation();
	}
	
	public double[] start (int epooc) {
		this.gBestFitness = new double[epooc];
		for (int i = 0; i < epooc; i++) {
			if(tipo==0){
				this.calc_fitness();
			}else{
				this.calc_fitnessEXP();
			}
			this.calc_gBest(i);
			this.populationAjust();
			this.inertiaAjust(i, epooc);
		}
		double[] outputVector = new double[input.length]; 
		double outputO = 0;
		double exp = 0;
		if(tipo==0){
			for (int i = 0; i < this.input.length; i++) {
				outputO = 0;
				for (int j = 0; j < this.input[0].length; j++) {
					
					outputO += this.gBest[j] * this.input[i][j];//saida linear

				}
				outputVector[i] = outputO;
			}
		}else{
			for (int i = 0; i < this.input.length; i++) {
				outputO = 0;
				exp = 0;
				for (int j = 0; j < this.input[0].length; j++) {
					
					exp += this.gBest[j] * this.input[i][j];//saida exp

				}
				outputO =this.gBest[particle[0].length-1]*(Math.exp(exp));
				outputVector[i] = outputO;
			}
		}
		return outputVector;
		
	}

	/* Gerar pupulação, velocidade de ajuste e memoria inicial */	
	public void  generatePopulation () {
		
		for (int i = 0; i < this.particle.length; i++) {
			for (int j = 0; j < this.particle[0].length; j++) {
				this.particle[i][j] = Math.random();
				this.pBest[i][j] = this.particle[i][j];
				this.velocity[i][j] = this.particle[i][j];
			}
		}
	}
	
	/* calculando melhor particula a partir do erro quadratico medio */
	public void calc_fitness() {
		
		double outputO = 0;
		
		double error;
		double errorTotal = 0;

		for (int k = 0; k < this.particle.length; k++) {
			
			outputO = 0;
			errorTotal = 0;
			for (int i = 0; i < this.input.length; i++) {
				outputO = 0;
				for (int j = 0; j < this.input[0].length; j++) {

					outputO += this.particle[k][j] * this.input[i][j];//saida linear

				}

				error = (this.output[i] - outputO);

				errorTotal += Math.pow(error, 2); /* eé aqui meu filhoooooo */

				error = 0;

			}
			
			errorTotal = errorTotal / this.input.length;
			
			/* criando / modificando memoria anterior das particulas */
			
			if(this.particleFitness[k] > 0) {
				if(errorTotal < this.particleFitness[k]) {
					for (int i = 0; i < this.particle[0].length; i++) {
						this.pBest[k][i] = this.particle[k][i];
					}
					this.particleFitness[k] = errorTotal;
					this.pBestFitness[k] = errorTotal;
				}else {
					this.particleFitness[k] = errorTotal;
				}
			}else {
				this.particleFitness[k] = errorTotal;
				this.pBestFitness[k] = errorTotal;
			}
			
			errorTotal = 0;
		}
	}
	public void calc_fitnessEXP() {
		
		double outputO = 0;
		double exp = 0;
		double error;
		double errorTotal = 0;

		for (int k = 0; k < this.particle.length; k++) {
			
			outputO = 0;
			exp = 0;
			errorTotal = 0;
			for (int i = 0; i < this.input.length; i++) {
				outputO = 0;
				exp = 0;
				for (int j = 0; j < this.input[0].length; j++) {
					
					exp += this.particle[k][j] * this.input[i][j];//saida exp

				}
				outputO = particle[k][particle[0].length-1]*(Math.exp(exp));
				error = (this.output[i] - outputO);

				errorTotal += Math.pow(error, 2); /* eé aqui meu filhoooooo */

				error = 0;

			}
			
			errorTotal = errorTotal / this.input.length;
			
			/* criando / modificando memoria anterior das particulas */
			
			if(this.particleFitness[k] > 0) {
				if(errorTotal < this.particleFitness[k]) {
					for (int i = 0; i < this.particle[0].length; i++) {
						this.pBest[k][i] = this.particle[k][i];
					}
					this.particleFitness[k] = errorTotal;
					this.pBestFitness[k] = errorTotal;
				}else {
					this.particleFitness[k] = errorTotal;
				}
			}else {
				this.particleFitness[k] = errorTotal;
				this.pBestFitness[k] = errorTotal;
			}
			
			errorTotal = 0;
		}
	}

	
	public void calc_gBest (int index) {
	/* encontrando melhor particula da população */
		
		int i = this.minFitness(pBestFitness);
		
		if(index != 0) {

			if(gBestFitness[index-1] > this.pBestFitness[i]) {

				gBestFitness[index] = pBestFitness[i];
				
				for (int j = 0; j < this.gBest.length; j++) {
					this.gBest[j] = this.pBest[i][j];
				}
					
			}else {
				gBestFitness[index] = gBestFitness[index-1];
			}
		}else {
			
			gBestFitness[index] = pBestFitness[i];
			
			for (int j = 0; j < this.gBest.length; j++) {
				this.gBest[j] = this.pBest[i][j];
			}
		}
		
		
	}
	
	/* Metodo de ajuste da população */
	public void populationAjust() {
		
		this.velocityAjust();
		
		for (int i = 0; i < particle.length; i++) {
			for (int j = 0; j < particle[0].length; j++) {
				this.particle[i][j] = this.particle[i][j] + this.velocity[i][j];
			}
		}
	}
	/* metodo de calculo da velocidade */
	public void velocityAjust () {
		for (int i = 0; i < this.velocity.length; i++) {
			for (int j = 0; j < this.velocity[0].length; j++) {
				//System.out.println("Antes =>"+this.velocity[i][j]);
				this.velocity[i][j] = (this.wInertia * this.velocity[i][j]) + this.c1 * 
						Math.random() * (this.pBest[i][j] - this.particle[i][j]) + this.c2 * 
						Math.random() * (this.gBest[j] - this.particle[i][j]);
				
				//System.out.println("Depois =>"+this.velocity[i][j]);
			}
		}
	}
	
	
	public int minFitness (double[] value) {
		
		double min = 999999;
		int index = 0;
		
		for (int i = 0; i < value.length; i++) {
			
			if(min > value[i] ) {
				min = value[i];
                index = i;
			}
		}
		
		return index;
	}
		
	
	/*public double  denormalize (double value) {
		return value * (this.max - this.min) + this.max;
	}*/
	
	
	public void inertiaAjust(int index, int epooc) {
		this.wInertia = (this.maxInertia - index / epooc) * (this.maxInertia - this.minInertia);
	}
	
	public double[] getgBestFitness() {
		return gBestFitness;
	}

	public double[] getgBest() {
		return gBest;
	}




}
