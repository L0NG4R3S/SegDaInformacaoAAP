package Item3;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Item3 {

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		String usuarioDigitado = "";
		String senhaDigitada = "";
		String usuarioAutenticacao = "";
		String senhaAutenticacao = "";

		int option;

		Scanner lerUsuario = new Scanner(System.in);

		//Do-while para a repeticao do menu
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

			FileWriter arq = new FileWriter("autenticacao3.txt", true);
			PrintWriter gravarArq = new PrintWriter(arq);
			FileReader reader = new FileReader("autenticacao3.txt");
			BufferedReader leitor = new BufferedReader(reader);

			//Switch para execucao da opcao
			switch (option) {
				//opcao cadastrar usuário
				case 1:
					System.out.printf("\n###### CADASTRE-SE ######\n");

					System.out.printf("\nInforme seu usuario (4 caracteres): \n");
					usuarioDigitado = lerUsuario.nextLine();

					//Restricao de 4 caracteres de usuario
					if (usuarioDigitado.length() != 4) {
						do {
							System.out.printf("\nO usuario deve conter 4 caracteres, tente novamente: \n");
							usuarioDigitado = lerUsuario.nextLine();
						} while (usuarioDigitado.length() != 4);
					}

					System.out.printf("Informe sua senha (4 caracteres): \n");
					senhaDigitada = lerUsuario.nextLine();

					//Restricao de 4 caracteres de senha
					if (senhaDigitada.length() != 4) {
						do {
							System.out.printf("\nA senha deve conter 4 caracteres, tente novamente: \n");
							senhaDigitada = lerUsuario.nextLine();
						} while (senhaDigitada.length() != 4);
					}

					//MD5
					m.update(senhaDigitada.getBytes(), 0, senhaDigitada.length());
					String hash = new BigInteger(1, m.digest()).toString(16);

					//System.out.printf("MD5: %s\n", hash);

					// Grava usuario e hash no arquivo txt e inicia o contador do numero de tentativas incorretas em 0
					gravarArq.printf("%s, %s, %d\n", usuarioDigitado, hash, 0);

					arq.close();
					leitor.close();

					System.out.print("\nUsuário Registrado com sucesso!\n");

					break;

				// opçao efetuar login
				case 2:

					System.out.printf("\n###### EFETUE LOGIN ######\n");

					System.out.printf("\nInforme seu usuario: \n");
					usuarioAutenticacao = lerUsuario.nextLine();

					System.out.printf("Informe sua senha: \n");
					senhaAutenticacao = lerUsuario.nextLine();

					//MD5: gera hash para a senha informada
					m.update(senhaAutenticacao.getBytes(),0,senhaAutenticacao.length());
					String hash2 = new BigInteger(1,m.digest()).toString(16);

					String linha = "";
					String[] userFound = null;
					boolean credenciaisIncorretas = false;
					int tentativas = 0;
					boolean userBlocked = false;

					//Do-while para ler as linhas do arquivo de texto e verificar se o usuario e senha estao corretos
					do {
						linha = leitor.readLine();
						if(linha.contains(usuarioAutenticacao+",") == true) {
							//Separamos o arquivo por , onde nos retorna um array com a posicao 0 o usuario e 1 o MD5
							userFound = linha.split(", ");
							tentativas = Integer.parseInt(userFound[2]);

							//Bloqueia usuario se errar a senha 5 vezes
							if(tentativas >= 5) {
								userBlocked = true;
								System.out.print("\n\nNUMERO DE TENTATIVAS EXCEDIDO, USUÁRIO BLOQUEADO");
							}else {
								//Verifica se o usario e o hash gerado pela senha digitada estão na linha do arquivo txt
								if(usuarioAutenticacao.equals(userFound[0]) && hash2.equals(userFound[1])) {
									System.out.print("\n\nCREDENCIAIS CORRETAS\n");
									credenciaisIncorretas = false;
								}else {
									credenciaisIncorretas = true;
									if(tentativas >= 4) {
										System.out.print("\n\nNUMERO DE TENTATIVAS EXCEDIDO, USUÁRIO BLOQUEADO\n");
									//Usuario não encontrado ou numero de tentativas não excedido
									}else {
										System.out.print("\n\nCREDENCIAIS INCORRETAS\n");
									}
								}
							}

							break;
						}
					}while(linha != null);

					arq.close();
					gravarArq.close();


					if (linha != null && userBlocked == false) {
		
						String l = leitor.readLine();
						ArrayList<String> salvar = new ArrayList<String>();

						while (l != null) {
							if (l.contains(usuarioAutenticacao + ",") == false) {
								salvar.add(l + "\n");
							}
							l = leitor.readLine();
						}
						
						//Atualiza o numero de tentativas, se as credenciais estiverem incorretas
						if (credenciaisIncorretas == true) {
							int tentativasAtualizadas = tentativas + 1;
							String novaLinha = userFound[0] + ", " + userFound[1] + ", " + tentativasAtualizadas + "\n";
							salvar.add(novaLinha);

						} else {
							String novaLinha = userFound[0] + ", " + userFound[1] + ", " + 0 + "\n";
							salvar.add(novaLinha);
						}
						
						//Escreve o arquivo com a quantidade de erros de autenticacao do usuario atualizada
						FileWriter arq2 = new FileWriter("autenticacao3.txt");
						PrintWriter gravarArq2 = new PrintWriter(arq2);

						for (int i = 0; i < salvar.size(); i++) {
							gravarArq2.print(salvar.get(i));
						}

						arq2.close();
						gravarArq2.close();

					}
					
					break;

				// Sai do laço de repeticao caso o usuario escolha a opcao 3
				case 3:
					System.out.print("\nVOLTE SEMPRE!");
					leitor.close();
					break;
			}

		}while(option != 3);

		lerUsuario.close();

	}
}