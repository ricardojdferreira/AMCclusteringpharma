import java.util.ArrayList;
import java.util.Iterator;

public class amostra {

	//O comprimento e a soma servem para criar a media da amostra, de modo a
	//ter um momento para se verificar se uma pesquisa comeca pelo inicio da lista, ou pelo fim da lista
	private ArrayList<double[]> array;
	private double comprimento;
	private double soma;


	//Construtor da classe amostra
	public amostra(){
		array=new ArrayList<double[]>();
		comprimento=array.size();
		soma=0;
	}

	//Adicionar vector a amostra, ordenado por indice, mas não por tempos/concentrações, 
	//visto que nao e necessario para o calculo do prob
	//A adicao e feita pelo inicio ou pelo fim, relacionando o indice com a media da amostra
	//reduzindo assim o numero de iteracoes
	public void add(double[] i){
		if(array.isEmpty()){
			array.add(i);
			soma+=i[0];
			comprimento=array.size();
			return;
		}

		if(i[0]<=soma/comprimento){
			int j=0;
			while (j < array.size() && i[0]>array.get(j)[0]){
				j++;
			}
			array.add(j,i);
			soma+=i[0];
			comprimento=array.size();
		}

		if(i[0]>soma/comprimento){
			int k=array.size()-1;
			while (k > 0 && i[0]<array.get(k)[0]){
				k--;;
			}
			array.add(k+1,i);
			soma+=i[0];
			comprimento=array.size();
		}
	}

	//Retorna o comprimento da amostra
	public double length(){
		return comprimento;
	}

	//Retorna o elemento k da amostra
	public double[] element (double k){
		int x = (int) (k-1);
		return array.get(x);
	}

	//Retorna um vector com todos os tempos e concentracoes associados a um indice
	public double[] indice(double i){

		ArrayList<Double> pares  =new ArrayList<Double>();

		if(i<=soma/comprimento){
			int k = 0;
			int flag = 0;
			while(k<array.size()){
				if(array.get(k)[0] == i){
					pares.add(array.get(k)[1]);
					pares.add(array.get(k)[2]);
					flag++;
				}
				if(array.get(k)[0] != i && flag != 0)
					break;
				k++;
			}
		}

		if(i>soma/comprimento){
			int k = array.size()-1;
			int flag = 0;
			while(k>=0){
				if(array.get(k)[0] == i){
					pares.add(array.get(k)[1]);
					pares.add(array.get(k)[2]);
					flag++;
				}
				if(array.get(k)[0] != i && flag != 0)
					break;
				k--;
			}
		}

		double[] ret = new double[pares.size()];
		Iterator<Double> iterador = pares.iterator();
		for (int i1 = 0; i1 < ret.length; i1++){
			ret[i1] = iterador.next().doubleValue();
		}
		return ret;
	}

	//Retorna a juncao de duas amostras
	public amostra join(amostra a, amostra b){
		if(a.length()>=b.length()){
			int k = 1;
			while(k<=b.length()){
				a.add(b.element(k));
				k++;
			}
			return a;
		}
		int k = 1;
		while(k<=a.length()){
			b.add(a.element(k));
			k++;
		}
		return b;
	}

	//Retorna os valores de diferentes indices: numero total de individuos
	public double[] individuos(){
		ArrayList<Double> id  =new ArrayList<Double>();
		int h=1;
		while(h<=array.size()){
			if(id.isEmpty()){
				id.add(this.element(h)[0]);
			}
			if(id.contains(this.element(h)[0])){
				h=h+1;
			}
			else{
				id.add(element(h)[0]);
				h=h+1;
			}
		}
		double[] ret = new double[id.size()];
		Iterator<Double> iterador = id.iterator();
		for (int i1 = 0; i1 < ret.length; i1++){
			ret[i1] = iterador.next().doubleValue();
		}
		return ret;
	}
}
