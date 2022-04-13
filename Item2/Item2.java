package Item2;
import java.util.Scanner;
import java.util.Date; 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Item2 {
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException {	
		
		int option;
		
		Scanner lerUsuario = new Scanner(System.in);
		
		do{
			System.out.printf("\n###### MENU ######\n");
			System.out.printf("1- Verficar Registros\n");
			System.out.printf("2- Descriptografar Usuarios\n");
			System.out.printf("3- Sair\n");
			System.out.printf("Escolha uma opcao\n");
			option = lerUsuario.nextInt();
			lerUsuario.nextLine();
				//Switch para processar a opcao selecionada
				switch (option) {
				
					case 1 :
						ExibirRegistros();
						break;
						
					case 2:
						DescriptografarUsuarios();
						break;
						
					case 3:
						System.out.print("\nVOLTE SEMPRE!");
						break;
				}
					
			}while(option != 3);
		
		
			lerUsuario.close();
		   
		}
/**
 * M�todo para exibir os registros do arquivo
 * @throws IOException
 */
public static void ExibirRegistros() throws IOException {

	
	FileReader reader = new FileReader("autenticacao.txt");
	BufferedReader leitor = new BufferedReader(reader);
	String linha = leitor.readLine();
	
	//Enquanto a linha nao estiver nula, ou seja, enquanto ainda esta lendo, vai fazendo print do conteudo do arquivo
		while(linha != null) {
			System.out.printf("\nUsuario : %s\nMD5: %s\n", linha.split(",")[0],linha.split(",")[1] );
			linha = leitor.readLine();
		}
		
	leitor.close();
}

/***
 * M�todo para quebrar a senha dos 4 primeiros usuarios do arquivo
 * @throws IOException
 * @throws InterruptedException
 */
public static void DescriptografarUsuarios() throws IOException, InterruptedException {
	
	//Variaveis de acesso ao arquivo
	FileReader reader = new FileReader("autenticacao.txt");
	BufferedReader leitorCont = new BufferedReader(reader);
	BufferedReader leitor = new BufferedReader(reader);
	
	//Variaveis de mensagem de retorno ao usuario
	String Retorno = "";
	String[] Resumo = new String[4];
	
	String linha = leitor.readLine();
	
	//Variaveis Contadores
	long contLinhas = leitorCont.lines().count();
	int contPosicao  = 0;
	int contTempo  = 10;
	
	 System.out.printf("\n%ss para o inicio do processo...\n", 5);
     Thread.sleep(5000);   
	
        //Executa 4 vezes
		while(contPosicao != 4) {
			
			//Chama o metodo que faz o ataque passando o usuario e o md5
			Retorno =  AtaqueForcaBruta(linha.split(",")[0].trim(),linha.split(",")[1].trim());
			
			//Armazena o retorno par ao resultado final do processo
			Resumo[contPosicao] = Retorno;
			
			//Mostra ao usuario o resultado desse usuario
			System.out.printf(Retorno);
			
			//Le a proxima linha
			linha = leitor.readLine();
			
			//Aumenta 1 o cont de posicao
			contPosicao++;
			
			//Se ainda n rodou 4 vezes, da uma pausada para o usuario poder ver o resultado desse usuario
			if (contPosicao != 4) {
			     System.out.printf("\n%ss para a execucaoo do proximo usuario...\n", contTempo);
			     Thread.sleep(10000);   
			}
		}
	
	leitor.close();
	
	//Mostra um resumo de todos os usuarios
	System.out.printf("\n----RESULTADO FINAL----\n", contTempo);
	
	for(int i = 0; i <= 3; ++i){
		System.out.printf("%s", Resumo[i]);
	}
	
}
	
/**
 * Metodo para tentar quebrar senha MD5 por forca bruta
 * @param Usuario 
 * Usuario no qual se esta tentando quebrar a senha
 * @param MD5Alvo
 * MD5 que se deseja obter
 * @return
 * Em caso de sucesso retorna o usuario, senha e tempo de processamento
 */
public static String AtaqueForcaBruta(String Usuario, String MD5Alvo) {
	
	   //Variaveis para contar o tempo
	   long Comeco = System.nanoTime();
	   long Fim;
		
	   //Caracteres que serao considerados que existem na senha e que sao a base do ataque
	   String[] CaracteresSenha = { "", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9","a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
				                    "n", "o", "p", "q", "r", "s", "t", "u", "v","w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
				                    "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "~", "!", "@", "#", "$", "%", "^", "&", "*",
			                        "(", ")", "_", "-", "+", "=", "."};
	
	    //Variaveis para comparacao das senhas
		String Senha = "";
		String hash = "";
		
		//Variavel de retorno, inicializada com um valor padrao
		String Retorno = "Nao foi possivel quebrar essa senha por forca bruta !";

		
		//Instancia do MD5
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
		}
            //Logica para quebra: Cada for compoe um digito da senha e contem todos os caracteres do array, como tem 4 fors, durante a execucao conjunta dos 4 todas as poss�veis combina��es desses caracteres seram atingidos.
		
		    //For 1
			for(int Seq1 = 0; Seq1 <= CaracteresSenha.length - 1; ++Seq1){
				//For 2
				for(int Seq2 = 0; Seq2 <= CaracteresSenha.length - 1; ++Seq2){
					//For 3
					for(int Seq3 = 0; Seq3 <= CaracteresSenha.length - 1; ++Seq3){
						//For 4
						for(int Seq4 = 0; Seq4 <= CaracteresSenha.length - 1; ++Seq4){
							
							//Feedback ao usuario sobre qual combinacao estou tentando
							System.out.printf("Tentando: %s %s %s %s \n" , CaracteresSenha[Seq1], CaracteresSenha[Seq2], CaracteresSenha[Seq3], CaracteresSenha[Seq4]);
						
							//Pega a sequencia da vez e converte em MD5 para comparacao
							Senha = (CaracteresSenha[Seq1] + CaracteresSenha[Seq2] + CaracteresSenha[Seq3] + CaracteresSenha[Seq4]);
							
							m.update(Senha.getBytes(), 0, Senha.length());
						    hash = new BigInteger(1, m.digest()).toString(16);
							
						    //Se for o meu hash alvo, retorna o sucesso do processo e po consequencia do retorno a funcao e encerrada
							if (hash.equals(MD5Alvo)) {
								Fim = System.nanoTime();
								Retorno = "\nSenha de " + Usuario + " foi Encontrada ! \nSenha: " + Senha + "\nTempo Necessario: " + ((Fim / 1000000000) - (Comeco / 1000000000)) + " segundos\n ";
								return Retorno;
								
							}
						
						}
						
					}
					
				}
				
			}
			
		//Se chegou aqui n achou a senha, retorna a variavel que contem o valor padrao de nao encontrado
		return Retorno;
	}
}