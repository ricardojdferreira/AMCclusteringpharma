import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JTextField;

import java.awt.GridBagConstraints;
import java.awt.CardLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;
import javax.swing.BoxLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextPane;
import javax.swing.JLabel;

import java.awt.Label;

import javax.swing.DropMode;
import javax.swing.JSpinner;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;


public class gui extends JFrame {

	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JFileChooser fileChooser1;
	private JFileChooser fileChooser2;
	private JLabel lblEstimativaInicial;
	private JLabel lblAmostra;
	private JButton btnOpen2;
	private JTextField nG;
	private JTextArea Path2;
	private final static String newline = "\n";
	private JTextArea Path1;
	private JTextField Arestas;
	private JLabel lblResultados;
	private JTextArea ResultadosText;
	private int botao1;
	private int botao2;
	private JButton Reiniciar;
	private grafoo g;
	private mix mi;
	private mix mf;
	private amostra a;
	private em alg;
	private int[][] arestas2;
	private JScrollPane scrollPane;
	private String gravar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui window = new gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 785, 592);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		botao1=0;
		botao2=0;


		//#########################
		//Botão que abre o ficheiro de amostra
		JButton btnOpen = new JButton("Abrir");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser1 = new JFileChooser("C:/Users/Ricardo Ferreira/Desktop");
				fileChooser1.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileFilter ft = new FileNameExtensionFilter("*.csv","csv");
				fileChooser1.addChoosableFileFilter(ft);
				fileChooser1.setAcceptAllFileFilterUsed(false);
				int rVal = fileChooser1.showOpenDialog(null);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					Path1.append(fileChooser1.getSelectedFile().toString()+";"+newline);
					botao1++;
				}
			}
		});
		btnOpen.setBounds(253, 81, 118, 22);
		frame.getContentPane().add(btnOpen);
		//#########################

		JLabel lblNDeCopmpartimentos = new JLabel("N\u00BA de compartimentos");
		lblNDeCopmpartimentos.setBounds(25, 13, 129, 22);
		frame.getContentPane().add(lblNDeCopmpartimentos);

		lblEstimativaInicial = new JLabel("Estimativa inicial");
		lblEstimativaInicial.setBounds(396, 81, 129, 22);
		frame.getContentPane().add(lblEstimativaInicial);

		lblAmostra = new JLabel("Amostra");
		lblAmostra.setBounds(25, 81, 129, 22);
		frame.getContentPane().add(lblAmostra);

		//#########################
		//Botao que abre a estimativa incial
		btnOpen2 = new JButton("Abrir");
		btnOpen2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileChooser2 = new JFileChooser("C:/Users/Ricardo Ferreira/Desktop");
				fileChooser2.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileFilter ft = new FileNameExtensionFilter("*.csv","csv");
				fileChooser2.addChoosableFileFilter(ft);
				fileChooser2.setAcceptAllFileFilterUsed(false);
				int rVal = fileChooser2.showOpenDialog(null);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					Path2.append(fileChooser2.getSelectedFile().toString()+";"+newline);
					botao2++;
				}
			}
		});
		btnOpen2.setBounds(624, 81, 118, 22);
		frame.getContentPane().add(btnOpen2);
		//#########################

		//#########################
		//Numero de grafos, caminho de ficheiro de amostra e de ficheiro inicial
		nG = new JTextField();
		nG.setBounds(25, 48, 129, 20);
		frame.getContentPane().add(nG);
		nG.setColumns(10);

		Path2 = new JTextArea();
		Path2.setBounds(396, 116, 346, 107);
		frame.getContentPane().add(Path2);

		Path1 = new JTextArea();
		Path1.setBounds(25, 116, 346, 107);
		frame.getContentPane().add(Path1);
		//#########################


		JLabel lblArestas = new JLabel("Arestas");
		lblArestas.setBounds(164, 13, 129, 22);
		frame.getContentPane().add(lblArestas);

		//#########################
		//Campo de texto com as arestas, separadas por ";" e com o inicio e fim separados por ","
		Arestas = new JTextField();
		Arestas.setBounds(164, 48, 207, 20);
		frame.getContentPane().add(Arestas);
		Arestas.setColumns(10);
		//#########################

		JLabel AlgortimoEM = new JLabel("Algoritmo EM");
		AlgortimoEM.setBounds(25, 236, 129, 22);
		frame.getContentPane().add(AlgortimoEM);

		//#########################
		//Realiza as confirmacoes necessárias para execucao do algoritmo
		//Caso haja mas atribuicoes nos valores da GUI retribui mensagem de erro
		//Depois de terminado o algoritmo apresenta os resultados
		JButton btnEm = new JButton("EM");
		btnEm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//Converte as arestas de string para uma matriz, de modo a se criar um grafo
				if(Arestas.getText().isEmpty()){
					ResultadosText.append("ERRO: Verificar dados!"+newline);
				}
				else{
					String[] arestas1 = Arestas.getText().split(";");
					int condicao=arestas1.length;
					arestas2 = new int[arestas1.length][2];
					for(int ll=0; ll<arestas1.length;ll++){
						String[] auxiliar = arestas1[ll].split(",");
						arestas2[ll][0]=Integer.parseInt(auxiliar[0]);
						arestas2[ll][1]=Integer.parseInt(auxiliar[1]);
					}

					if(botao2 == condicao && botao1==condicao){
						ResultadosText.setText(null);
						String[] caminho1 = new String[Path1.getText().split(";").length];
						String[] caminho2 =new String[Path2.getText().split(";").length];
						caminho1=Path1.getText().split(";"+newline);
						caminho2=Path2.getText().split(";"+newline);
						//Criacao de um grafo
						g = new grafoo(Integer.parseInt(nG.getText()));
						a =new amostra();
						csvreader file=new csvreader();
						//Adiciona as arestas

						for(int i=0;i<arestas2.length;i++){

							ResultadosText.append("Grafo com "+nG.getText()+" compartimentos"+newline);

							mi = new mix();
							mf = new mix();
							//Para cada aresta le os ficheiros
							try {
								a=file.reader(caminho1[i]);
								mi = new mix(file.readertheta(caminho2[i]));
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							alg = new em();
							mf=alg.alg(a,mi);
							g.add_edge(arestas2[i][0],arestas2[i][1],mf.theta());
							ResultadosText.append("Ligação de "+Integer.toString(arestas2[i][0])+" para "+Integer.toString(arestas2[i][1])+newline);
							ResultadosText.append("Número de iterações "+Double.toString(alg.iteracao())+newline);
							String[] legenda = {"wj ","sigmaj ","a1j ","a2j ","b1j ","b2j "};
							for(int q=0;q<mf.theta()[0].length;q++){
								ResultadosText.append(legenda[q]);
								for(int t=0;t<mf.theta().length;t++){
									ResultadosText.append(Double.toString(mf.theta()[t][q])+" ");
								}
								ResultadosText.append(newline);
							}
							ResultadosText.append(newline);

						}
					}
					else{
						ResultadosText.append("ERRO: Verificar dados!"+newline);
					}
				}
			}
		});
		btnEm.setBounds(253, 236, 118, 22);
		frame.getContentPane().add(btnEm);
		//#########################

		lblResultados = new JLabel("Resultados");
		lblResultados.setBounds(25, 271, 129, 22);
		frame.getContentPane().add(lblResultados);

		ResultadosText = new JTextArea();
		ResultadosText.setEditable(false);
		ResultadosText.setBounds(25, 306, 717, 237);
		frame.getContentPane().add(ResultadosText);

		//#########################
		//Limpa todas as variáveis de texto nos frames
		Reiniciar = new JButton("Reiniciar");
		Reiniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Path1.setText(null);
				Path2.setText(null);
				nG.setText(null);
				ResultadosText.setText(null);
				Arestas.setText(null);
				botao1=0;
				botao2=0;
				g=null;
				a=null;
				mi=null;
				mf=null;
				arestas2=null;
				gravar=null;

			}
		});
		Reiniciar.setBounds(624, 236, 118, 22);
		frame.getContentPane().add(Reiniciar);

		scrollPane = new JScrollPane(ResultadosText);
		scrollPane.setBounds(25, 304, 717, 188);
		frame.getContentPane().add(scrollPane);

		JButton btnGravarResultados = new JButton("Gravar resultados");
		btnGravarResultados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.getCurrentDirectory();
				chooser.setSelectedFile(new File("resultados.txt"));
				chooser.showSaveDialog(null);

				String save = chooser.getSelectedFile().toString();
				gravar = ResultadosText.getText();
				try {
					FileWriter ff= new FileWriter(save);
					PrintWriter pp = new PrintWriter(ff);
					pp.print(gravar);
					pp.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		btnGravarResultados.setBounds(550, 503, 192, 22);
		frame.getContentPane().add(btnGravarResultados);
		//#########################

	}
}
