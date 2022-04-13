package Item1;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Item1 {
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {	
		String usuarioDigitado = "";
		String senhaDigitada = "";
		String usuarioAutenticacao = "";
		String senhaAutenticacao = "";
		
		int option;
		
		Scanner lerUsuario = new Scanner(System.in);
		
	
		//Do while para a repeticao do menu
		do{
			System.out.printf("\n###### MENU ######\n");
			System.out.printf("1- Cadastre-se\n");
			System.out.printf("2- Login\n");
			System.out.printf("3- Sair\n");
			System.out.printf("Escolha uma opcao\n");
			option = lerUsuario.nextInt();
			lerUsuario.nextLine();
			
			MessageDigest m = null;
			try {
				m = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
				
			FileWriter arq = new FileWriter("autenticacao.txt", true);
			PrintWriter gravarArq = new PrintWriter(arq);
			FileReader reader = new FileReader("autenticacao.txt");
			BufferedReader leitor = new BufferedReader(reader);
				
			//Switch para execucao da opcao
				switch (option) {
					case 1 :
						System.out.printf("\n###### CADASTRE-SE ######\n");
						
						System.out.printf("\nInforme seu usuario (4 caracteres): \n");
						usuarioDigitado = lerUsuario.nextLine();
						
						//Restricao de 4 caracteres de usuario
						if(usuarioDigitado.length() != 4) {
							do {
								System.out.printf("\nO usuario deve conter 4 caracteres, tente novamente: \n");
								usuarioDigitado = lerUsuario.nextLine();
							}while(usuarioDigitado.length() != 4);
						}
							  
						System.out.printf("Informe sua senha (4 caracteres): \n");
						senhaDigitada = lerUsuario.nextLine();
						
						//Restricao de 4 caracteres de senha
						if(senhaDigitada.length() != 4) {
							do {
								System.out.printf("\nA senha deve conter 4 caracteres, tente novamente: \n");
								senhaDigitada = lerUsuario.nextLine();
							}while(senhaDigitada.length() != 4);
						}
						
						//MD5
						 m.update(senhaDigitada.getBytes(),0,senhaDigitada.length());
						 String hash = new BigInteger(1,m.digest()).toString(16);
						 
						 //System.out.printf("MD5: %s\n", hash);
						 
						 gravarArq.printf("%s, %s\n", usuarioDigitado, hash);
						 arq.close();
						 
						 System.out.print("\nUsuário Registrado com sucesso!\n");
						    
						break;
						
					case 2:
					
						System.out.printf("\n###### EFETUE LOGIN ######\n");
						
						System.out.printf("\nInforme seu usuario: \n");
						usuarioAutenticacao = lerUsuario.nextLine();
								  
						System.out.printf("Informe sua senha: \n");
						senhaAutenticacao = lerUsuario.nextLine();
						
						//Convertemos a senha digitada para MD5 para realizar a validacao
					    m.update(senhaAutenticacao.getBytes(),0,senhaAutenticacao.length());
					    String hash2 = new BigInteger(1,m.digest()).toString(16);
	
						String linha = "";
						String[] userFound = null;
						//Faz a busca do usuario enquanto esta lendo o arquivo
						do {
							linha = leitor.readLine();
							//Se a linha tem esse usuario
							if(linha.contains(usuarioAutenticacao+",")) {
								//Separamos o arquivo por , onde nos retorna um array com a posicao 0 o usuario e 1 o MD5
								userFound = linha.split(", ");
								
								//Se for esse usuario e com esse hash de senha
								if(usuarioAutenticacao.equals(userFound[0]) && hash2.equals(userFound[1])) {
									System.out.print("\n\nCREDENCIAIS CORRETAS");
									break;
								}else {
									System.out.print("\n\nCREDENCIAIS INCORRETAS");
									break;
								}
			
								
							}
						}while(linha != null);
						
						arq.close();
						leitor.close();
						
						if(linha == null) {
							System.out.print("\n\nUSUARIO NÃO ENCONTRADO");
						}
						break;
					case 3:
						System.out.print("\nVOLTE SEMPRE!");
						leitor.close();
						break;
				}
					
			}while(option != 3);
		
		
			lerUsuario.close();
		   
		}
}