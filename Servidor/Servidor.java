package Servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Servidor implements Runnable {
    private Socket cliente;
    private boolean conexao = true;
    private PrintStream saida;
    private Scanner teclado;
    private SistemaBancario sistema;
    

    public Servidor(Socket c, SistemaBancario sist) {
        this.cliente = c;
        this.sistema = sist;
    }

    public void run() {
        try {
            teclado = new Scanner(System.in);
            saida = new PrintStream(cliente.getOutputStream());
            Scanner entrada = new Scanner(cliente.getInputStream());

            

            boolean resposta;

            String mensagemResposta;

            int opcao = -1;

            int operacao = 0;

            Double valor;

            String mensagem;

            String numConta;
            String senha;

            while (conexao) {
                

                // Recebe mensagem do firewall e envia para o cliente
                
                    opcao = entrada.nextInt();

                    //limpa o buffer
                    entrada.nextLine();
                    

                    //fazer login
                    if (opcao == 1) {
                        
                        numConta = entrada.nextLine();
                        senha = entrada.nextLine();

                        //acessar o banco de dados
                        resposta = sistema.autenticarUser(numConta, senha);
                        System.out.println("Login bem sucedido");
                        saida.println(resposta);


                        while (operacao != 6) {
  
                            operacao = entrada.nextInt();

                            System.out.println(operacao);

                            
                            

                            //saque
                            if (operacao == 1) {
                                System.out.println("Operação de saque em andamento..");
                            }
    
                            //depósito
                            if (operacao == 2) {

                                entrada.nextLine();

                                mensagem = entrada.nextLine();
                                valor = Double.parseDouble(mensagem);
                                
                                if (valor > 0.0) {
                                    System.out.println("Operação de deposito em andamento..");
                                
                                    sistema.deposito(numConta, valor);
    
                                    mensagemResposta = " Saldo atual:"+sistema.saldo(numConta);
                                }
                                else{
                                    mensagemResposta = " Valor inválido";
                                }

                                saida.println(mensagemResposta);

    
                            }

                            //transferencia
                            if (operacao == 3) {
                                
                            }

                            //saldo
                            if (operacao == 4) {
                                System.out.println("Operação de saldo em andamento..");

                                mensagemResposta = " Saldo atual:"+sistema.saldo(numConta);

                                saida.println(mensagemResposta);
                            }

                            //Investimentos
                            if (operacao == 5) {
                                
                            }


                            
                        }

                       

                    }
                    
                


                // // Envia mensagem para o firewall
                // System.out.println("Digite uma mensagem para enviar ao firewall: ");
                // mensagem = "chavepublica";
                // if (mensagem.equals("fim")) {
                //     conexao = false;
                //     break;
                // }
                // saida.println(mensagem);
            }

            // Fechar conexões
            entrada.close();
            saida.close();
            cliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
