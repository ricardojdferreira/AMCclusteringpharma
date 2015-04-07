
public class mix {

	//Propriedades da mistura de gaussianas: numero de gaussianas e parametros theta
	private double[][] theta; //As linhas referem-se a cada theta e as colunas aos diferentes parametros


	public mix(){
		theta=null;
	}

	//Construtor da mistura de gaussianas
	public mix(double[][] parametros){
		theta=parametros;
	}

	//Função a estimar (gaussiana), meramente auxiliar no calculo do prob
	private double Gauss( int num, double t){
		double res = (theta[num][2]*Math.exp(-theta[num][4]*t))+(theta[num][3]*Math.exp(-theta[num][5]*t));
		return res;
	}

	//Implementa a funcao 2.3.4 descrita no enunciado
	public double prob(double[] t, double[] y,int num){
		double resinter = 0;		
		double res = 0;
		double c1,c2;
		c1=t.length;
		c2=2;
		c1=c1/c2;
		for(int l=0;l<t.length;l++){
			res += Math.pow((y[l]-this.Gauss(num,t[l])),2);
		}
		resinter=(1/Math.pow(2*Math.PI*theta[num][1],c1))*Math.exp((-1/(2*theta[num][1]))*res);
		return resinter;
	}

	//Retorna a lista de parametros actual
	public double [][] theta(){
		return theta;
	}

	//Método que actualiza os parametros theta
	public void update(double[][] parametros){
		theta=parametros;
	}
}
