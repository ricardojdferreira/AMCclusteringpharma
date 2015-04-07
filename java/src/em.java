
public class em {
	private amostra sample;
	private mix tt;
	private double[][] Xij;
	private double[] Xijsoma;
	private double[] wjnew;
	private double[] ajnew;
	private double[] h1j;
	private double[] h1jlinha;
	private double[] b1j;
	private double[] b2j;
	private double[] h2j;
	private double[] h2jlinha;
	private double[] b1jiter;
	private double[] b2jiter;
	private double[][] tempconc;
	private double[] sigmajnew;
	private double[][] actualizar;
	private int convergencia = 0;
	private int iteracao = 0;
	private int n;
	private int K;
	private int M;

	// Construtor do algoritmo
	public mix alg(amostra sample1,mix tt1){

		//Inicializacao de variaveis
		sample=sample1;
		tt=tt1;

		//Vector auxiliar com todas as pessoas
		double[] pessoas = sample.individuos(); 

		//Comprimento da mistura, usado muitas vezes e por isso definido aqui
		M = tt.theta().length;

		//Numero de pessoas
		K = pessoas.length;

		//Matriz auxiliar de tempos e concentracoes
		tempconc= new double[2*K][sample.indice(pessoas[0]).length/2];
		
		//Numero de tempos
		n = tempconc[0].length;

		//Actualização da matriz de tempos e concentracoes
		System.out.println("TESTE TEMPCONC");
		for(int qq=0;qq<tempconc.length/2;qq++){
			for(int yy = 0; yy<(sample.indice(pessoas[qq]).length)/2; yy++){
				tempconc[2*qq][yy]=sample.indice(qq)[2*yy];
				tempconc[2*qq+1][yy]=sample.indice(qq)[2*yy+1];
			}
		}
		
		iteracao=0;

		boolean parar = true;
		while(parar){

			Xij = new double[M][K];
			Xijsoma = new double[K];
			wjnew= new double[M];
			ajnew= new double[M];
			b1j = new double[M];
			b2j = new double[M];					
			h1j=new double[M];
			h1jlinha=new double[M];
			h2j=new double[M];
			h2jlinha=new double[M];
			b1jiter = new double[M];
			b2jiter = new double[M];
			sigmajnew=new double [M];
			actualizar=new double[M][6];

			System.out.println("ITERACAO "+iteracao);


			//Calculo do Xij(k) para todos os individuos para todos os thetas
			//Realizacao de matriz com linhas de thetas e colunas de individuos
			//Para cada coluna a ultima linha é a soma para todos os thetas
			System.out.println("TESTE XIJ");
			for(int i=0;i<K;i++){
				for(int j = 0;j<M;j++){
					Xij[j][i]=tt.theta()[j][0]*tt.prob(tempconc[2*i], tempconc[2*i+1], j)*Math.exp(500);
					Xijsoma[i]+=Xij[j][i];
				}
			}


			//Calculo do Xij
			for(int i=0;i<K;i++){
				for(int j = 0;j<M;j++){
					Xij[j][i]=Xij[j][i]/Xijsoma[i];
				}
			}

			System.out.println("TESTE WJNEW");
			//Actualizacao do wj(k+1)
			for(int e=0;e<M;e++){
				for(int r =0;r<K;r++){
					wjnew[e]+=Xij[e][r];
				}
				wjnew[e]=wjnew[e]/K;
			}

			//Condicao inicial dos b1j e b2j
			for(int x1=0; x1<M;x1++){
				b1j[x1]=tt.theta()[x1][4];
			}
			for(int x2=0; x2<M;x2++){
				b2j[x2]=tt.theta()[x2][5];
			}


			//Actualizacao de b1j e b2j usando o metodo de Newton

			System.out.println("TESTE B1J E B2J");
			//Iteracao do metodo de Newton para cada gaussiana
			int iter1;
			int flag1;
			int iter2;
			int flag2;
			for(int z = 0;z<M;z++){

				//###################################
				//B1J
				//###################################

				flag1=0;

				//###################################
				//10000 ITERADAS NORMAIS
				//###################################

				iter1=0;

				b1jiter[z]=b1j[z];

				while(iter1<1000){

					//Actualizacao do h1j
					double resup5=0;
					for(int a = 0; a<K;a++){
						for(int d = 0; d<n;d++){
							resup5+=Xij[z][a]*tempconc[2*a][d]*Math.exp(-b1jiter[z]*tempconc[2*a][d])*(tempconc[2*a+1][d]-tt.theta()[z][2]*(Math.exp(-b1jiter[z]*tempconc[2*a][d])-Math.exp(-b2j[z]*tempconc[2*a][d])));
						}
					}
					h1j[z]=resup5;

					//Actualizacao do h1jlinha
					double resup6=0;
					for(int g = 0; g<K;g++){
						for(int l = 0; l<n;l++){
							resup6+=Xij[z][g]*Math.pow(tempconc[2*g][l],2)*Math.exp(-b1jiter[z]*tempconc[2*g][l])*(tt.theta()[z][2]*(2*Math.exp(-b1jiter[z]*tempconc[2*g][l])-Math.exp(-b2j[z]*tempconc[2*g][l]))-tempconc[2*g+1][l]);
						}
					}
					h1jlinha[z]=resup6;

					b1jiter[z]=b1jiter[z]-(h1j[z]/h1jlinha[z]);

					//Verifica as condições de paragem e actualiza os valores para recomecar
					if(b1jiter[z]<0 || b1jiter[z]>b2j[z] ){

						b1jiter[z]=b1j[z];

						flag1 = 1;
						break;
					}
					
					if(h1j[z]==0){
						break;
					}

					//Avanca na iteracao do Metodo de Newton
					iter1++;

				}
				System.out.println("PASSOU NORMAL "+z+" b1");

				//###################################
				//10000 ITERADAS COM OS LIMITES ULTRAPASSADOS
				//###################################

				if(flag1 == 1){
					iter1=0;
					double beta1 = -0.3;
					while(iter1 < 9999 ){

						//Actualizacao do h1j
						double resup5=0;
						for(int a = 0; a<K;a++){
							for(int d = 0; d<n;d++){
								resup5+=Xij[z][a]*tempconc[2*a][d]*Math.exp(-b1jiter[z]*tempconc[2*a][d])*(tempconc[2*a+1][d]-tt.theta()[z][2]*(Math.exp(-b1jiter[z]*tempconc[2*a][d])-Math.exp(-b2j[z]*tempconc[2*a][d])));
							}
						}
						h1j[z]=resup5;

						//Actualizacao do h1jlinha
						double resup6=0;
						for(int g = 0; g<K;g++){
							for(int l = 0; l<n;l++){
								resup6+=Xij[z][g]*Math.pow(tempconc[2*g][l],2)*Math.exp(-b1jiter[z]*tempconc[2*g][l])*(tt.theta()[z][2]*(2*Math.exp(-b1jiter[z]*tempconc[2*g][l])-Math.exp(-b2j[z]*tempconc[2*g][l]))-tempconc[2*g+1][l]);
							}
						}
						h1jlinha[z]=resup6;

						b1jiter[z]=b1jiter[z]-(h1j[z]/h1jlinha[z]);
						if(h1jlinha[z]>beta1){
							break;
						}
						if(b1jiter[z]<0 || b1jiter[z]>b2j[z]){
							b1jiter[z]=b1j[z];
							iter1=0;
							beta1-=0.2;
						}
						if(beta1<-5){
							b1jiter[z]=b1j[z];
							break;
						}

						iter1++;
					}
					System.out.println("PASSOU FORA LIMIT "+z+" b1");
					flag1=0;
				}
			}


			//#######################################################################################################################################################


			System.out.println("TESTE AJNEW");
			//Actualizacao do aj(k+1) (actualiza para todas as misturas)
			double resup = 0;
			double resdown = 0;
			for(int t=0;t<M;t++){
				for(int y=0;y<K;y++){
					for(int u=0;u<n;u++){
						resup+=Xij[t][y]*tempconc[2*y+1][u]*(Math.exp(-b1jiter[t]*tempconc[2*y][u])-Math.exp(-b2j[t]*tempconc[2*y][u]));
						resdown+=Xij[t][y]*Math.pow((Math.exp(-b1jiter[t]*tempconc[2*y][u])-Math.exp(-b2j[t]*tempconc[2*y][u])), 2);
					}
				}
				ajnew[t]=resup/resdown;
				resup = 0;
				resdown = 0;
			}

			for(int z = 0;z<M;z++){

				//###################################
				//B2J
				//###################################

				flag2 = 0;

				b2jiter[z]=b2j[z];


				//###################################
				//10000 ITERADAS NORMAIS
				//###################################

				iter2=0;

				while(iter2<9999){

					//Actualizacao do h2j 
					double resup32=0;
					for(int a = 0; a<K;a++){
						for(int d = 0; d<n;d++){
							resup32+=Xij[z][a]*tempconc[2*a][d]*Math.exp(-b2jiter[z]*tempconc[2*a][d])*(tempconc[2*a+1][d]-ajnew[z]*(Math.exp(-b1jiter[z]*tempconc[2*a][d])-Math.exp(-b2jiter[z]*tempconc[2*a][d])));
						}
					}
					h2j[z]=resup32;

					//Actualizacao do h2jlinha
					double resup42=0;
					for(int g = 0; g<K;g++){
						for(int l = 0; l<n;l++){
							resup42+=Xij[z][g]*Math.pow(tempconc[2*g][l],2)*Math.exp(-b2jiter[z]*tempconc[2*g][l])*(ajnew[z]*(Math.exp(-b1jiter[z]*tempconc[2*g][l])-2*Math.exp(-b2jiter[z]*tempconc[2*g][l]))-tempconc[2*g+1][l]);
						}
					}
					h2jlinha[z]=resup42;

					b2jiter[z]=b2jiter[z]-(h2j[z]/h2jlinha[z]);


					//Verifica as condições de paragem e actualiza os valores para recomecar
					if(b2jiter[z]<b1j[z] || b2jiter[z]>5 ){

						b2jiter[z]=b2j[z];

						flag2 = 1;
						break;
					}

					if(h2j[z]==0){
						break;
					}

					iter2++;
				}
				System.out.println("PASSOU NORMAL "+z+" b2");
				//###################################
				//10000 ITERADAS COM OS LIMITES ULTRAPASSADOS
				//###################################

				if(flag2 == 1){
					iter2=0;
					double beta2 = -0.3;
					//					h2j[z]=0;
					//					h2jlinha[z]=0;
					while(iter2<9999){


						//					System.out.println("ITERAÇÃO ############################"+iter2);
						//Actualizacao do h2j 
						double resup32=0;
						for(int a = 0; a<K;a++){
							for(int d = 0; d<n;d++){
								resup32+=Xij[z][a]*tempconc[2*a][d]*Math.exp(-b2jiter[z]*tempconc[2*a][d])*(tempconc[2*a+1][d]-ajnew[z]*(Math.exp(-b1jiter[z]*tempconc[2*a][d])-Math.exp(-b2jiter[z]*tempconc[2*a][d])));
							}
						}
						h2j[z]=resup32;

						//Actualizacao do h2jlinha
						double resup42=0;
						for(int g = 0; g<K;g++){
							for(int l = 0; l<n;l++){
								resup42+=Xij[z][g]*Math.pow(tempconc[2*g][l],2)*Math.exp(-b2jiter[z]*tempconc[2*g][l])*(ajnew[z]*(Math.exp(-b1jiter[z]*tempconc[2*g][l])-2*Math.exp(-b2jiter[z]*tempconc[2*g][l]))-tempconc[2*g+1][l]);
							}
						}
						h2jlinha[z]=resup42;

						b2jiter[z]=b2jiter[z]-(h2j[z]/h2jlinha[z]);


						if(h2jlinha[z]>beta2){
							break;
						}

						if(b2jiter[z]<b1j[z] || b2jiter[z]>5){
							b2jiter[z]=b2j[z];
							iter2=0;
							beta2-=0.2;
						}
						if(beta2<-5){
							b2jiter[z]=b2j[z];
							break;
						}
						iter2++;
					}
					System.out.println("PASSOU FORA LIMIT "+z+" b2");
				}
				flag2=0;
			}


			//Calculo do sigmaj
			for(int b= 0; b<M;b++){
				double resup89=0;
				double resdown89=0;
				for (int nn=0;nn<K;nn++){
					for (int m=0; m<tempconc[2*n].length;m++){
						resup89+=Xij[b][nn]*Math.pow((tempconc[2*nn+1][m]-(ajnew[b]*Math.exp(-b1jiter[b]*tempconc[2*nn][m])-ajnew[b]*Math.exp(-b2jiter[b]*tempconc[2*nn][m]))),2);
					}
					resdown89+=Xij[b][nn];
				}
				sigmajnew[b]=resup89/(tempconc[0].length*resdown89);
			}


			System.out.println(wjnew[0]+" "+wjnew[1]+" "+wjnew[2]+" "+wjnew[3]+" "+"WJNEW");
			System.out.println(ajnew[0]+" "+ajnew[1]+" "+ajnew[2]+" "+ajnew[3]+" "+"AJNEW");
			System.out.println(b1jiter[0]+" "+b1jiter[1]+" "+b1jiter[2]+" "+b1jiter[3]+" "+"B1JNEW");
			System.out.println(b2jiter[0]+" "+b2jiter[1]+" "+b2jiter[2]+" "+b2jiter[3]+" "+"B2JJNEW");
			System.out.println(sigmajnew[0]+" "+sigmajnew[1]+" "+sigmajnew[2]+" "+sigmajnew[3]+" "+"SIGMAJNEW");

			//Actualização dos parametros
			for(int zz = 0;zz<M;zz++){
				actualizar[zz][0]=wjnew[zz];
				actualizar[zz][1]=sigmajnew[zz];
				actualizar[zz][2]=ajnew[zz];
				actualizar[zz][3]=-ajnew[zz];
				actualizar[zz][4]=b1jiter[zz];
				actualizar[zz][5]=b2jiter[zz];
				if(Math.pow(Math.abs(tt.theta()[zz][4]-b1jiter[zz]),2)<0.000001){
					convergencia++;
				}

				if( Math.pow(Math.abs(tt.theta()[zz][5]-b2jiter[zz]),2)<0.000001){
					convergencia++;
				}
				System.out.println("DIFFFF "+Math.pow(Math.abs(tt.theta()[zz][4]-b1jiter[zz]),2));
				System.out.println("DIFFFF "+Math.pow(Math.abs(tt.theta()[zz][5]-b2jiter[zz]),2));
			}

			tt.update(actualizar);
			
			if(convergencia == 2*M){
				break;
			}
			System.out.println("CONVERGENCIA "+convergencia);
			convergencia =0;
			iteracao++;
			
		}
		return tt;
	}
	
	//Metodo que retorna o numero de iteracoes
	public int iteracao(){
		return iteracao;
	}
}


