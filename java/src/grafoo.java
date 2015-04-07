public class grafoo {

	//Atributos da classe de grafos de compartimentos
	private double[][][][] MA;
	
	//Os grafos foram implementados como um tensor, em que as linhas são o no de partida, 
	//as colunas são o nó de chegada e a 3ª dimensão possuem os parametros dessa ligacao

	//Construtor de um grafo com n "compartimentos"
	public grafoo(int n){
		MA = new double [n][n][][];
	}

	//Adiciona uma aresta
	public void add_edge(int n1, int n2, double[][] mix){
		MA[n1-1][n2-1]=mix;
	}

	//Retira uma aresta
	public void remove_edge(int n1, int n2){
		MA[n1-1][n2-1]=null;
	}

	//Actualiza uma aresta
	public void update_edge(int n1, int n2, double[][] mix){
		MA[n1-1][n2-1]=mix;
	}
}
