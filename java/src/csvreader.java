import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;



public class csvreader{

	// Construtor do reader
	private amostra data;
	private String filepath1;
	private String filepath2;

	// Construtor de uma nova leitura
	public csvreader(){
		data=new amostra();
		filepath1=null;
		filepath2=null;
	}

	// Rotina que le o ficheiro .CSV e introduz os valores numa nova amostra
	public amostra reader(String s) throws Exception{
		
		this.data = new amostra();
		this.filepath1 = s;
			
		BufferedReader CSVFile = new BufferedReader (new FileReader (filepath1));
		
		//Descarta a primeira linha
		String line = CSVFile.readLine();
		int flag = 0;
		while(line != null){
			if(flag == 0){
				//Descarta a segunda linha
				line = CSVFile.readLine();
				flag++;
			}
			else{
				//O ficheiro tem um espco no final, ignora a linha vazia
				if(!line.isEmpty()){
					String[] line2=line.split(";");
					double[] vector =new double[3];
					vector[0]=Double.parseDouble(line2[0]);
					vector[1]=Double.parseDouble(line2[1]);
					vector[2]=Double.parseDouble(line2[2]);
					data.add(vector);
					System.out.println(vector[0]);
					System.out.println(vector[1]);
					System.out.println(vector[2]);
					System.out.println("-----");
				}
				line = CSVFile.readLine();
			}
		}
		CSVFile.close();
		return data;
	}


	// Faz leitura do ficheiro de aproximacoes iniciais
	public double[][] readertheta(String s) throws Exception{
		
		this.filepath2 = s;
		
		BufferedReader CSVFile = new BufferedReader (new FileReader (filepath2));
		//Faz leitura da primeira linha
		String line = CSVFile.readLine();
		String[] line2 = line.split(",");
		double[] vector =new double[4];
		vector[0]=Double.parseDouble(line2[0]);
		vector[1]=-vector[0];
		vector[2]=Double.parseDouble(line2[1]);
		vector[3]=Double.parseDouble(line2[2]);
		System.out.println(vector[0]);
		System.out.println(vector[1]);
		System.out.println(vector[2]);
		System.out.println(vector[3]);
		System.out.println("-----");
		ArrayList<double[]> aux = new ArrayList<double[]>();
		aux.add(vector);

		//Faz leitura do resto do ficheiro
		String line3 = CSVFile.readLine();
		while(line3 != null){
			if(!line.isEmpty()){
				String[] line4=line3.split(",");
				double[] vector2 =new double[4];
				vector2[0]=Double.parseDouble(line4[0]);
				vector2[1]=-vector2[0];
				vector2[2]=Double.parseDouble(line4[1]);
				vector2[3]=Double.parseDouble(line4[2]);
				aux.add(vector2);
				System.out.println(vector2[0]);
				System.out.println(vector2[1]);
				System.out.println(vector2[2]);
				System.out.println(vector2[3]);
				System.out.println("-----");
			}
			line3 = CSVFile.readLine();

		}
		CSVFile.close();

		//Foi usado um ArrayList porque nao se sabe quantas gaussianas ha na mistura,
		//e o metodo construtor da mistura tem de receber um tensor completo
		double[][] ret = new double[aux.size()][aux.get(0).length+2];
		for (int i1 = 0; i1 < aux.size(); i1++){
			for (int i2 = 0; i2 <aux.get(0).length ; i2++){
				ret[i1][i2+2] = aux.get(i1)[i2];
			}
			double x = aux.size();
			ret[i1][0]=1/x;
			ret[i1][1]=1;
		}
		return ret;
	}

}
