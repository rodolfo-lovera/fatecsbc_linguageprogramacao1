import java.util.Scanner; // Importa a classe para leitura de dados [cite: 11]
import java.util.Locale;  // Importa para garantir o uso do ponto decimal em vez de vírgula

public class calculadora {
    public static void main(String[] args) {
        // Configura o ponto como separador decimal (Padrão Americano/Programação)
        Locale.setDefault(Locale.US);
        
        // Passo 1: Instanciar o objeto Scanner para ler a entrada do teclado [cite: 11]
        Scanner sc = new Scanner(System.in);

        // Passo 2: Entrada de Dados e Declaração de Variáveis [cite: 6]
        System.out.println("--- Calculadora de Negócios (Java) ---");
        
        System.out.print("Digite o primeiro número: ");
        double numero1 = sc.nextDouble(); // Java requer definição explícita do tipo [cite: 6]

        System.out.print("Digite o segundo número: ");
        double numero2 = sc.nextDouble();

        // Passo 3: Processamento (Cálculos Aritméticos) [cite: 7]
        double soma = numero1 + numero2;
        double subtracao = numero1 - numero2;
        double multiplicacao = numero1 * numero2;
        double divisao = numero1 / numero2;

        // Passo 4: Saída de Resultados Formatados        
        System.out.printf("Soma: %.2f%n", soma);
        System.out.printf("Subtração: %.2f%n", subtracao);
        System.out.printf("Multiplicação:  %.2f%n", multiplicacao);
        System.out.printf("Divisão:  %.2f%n", divisao);
        
        // Passo 5: Fechar o Scanner (Boa prática de gerenciamento de memória)
        sc.close();
    }
}