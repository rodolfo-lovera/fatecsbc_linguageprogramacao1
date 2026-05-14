import java.util.Scanner;

public class ex1SistemaEstoqueP1 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Produto poteChocolate = new Produto("Pote Chocolate 2L", 15, 100);
        Produto picoleLimao = new Produto("Picolé de Limão", 50, 100);

        // Testando a inteligência automatizada
        System.out.println("Chocolate precisa repor? " + poteChocolate.verificarNecessidadeCompra()); // True
        System.out.println("Limão precisa repor? " + picoleLimao.verificarNecessidadeCompra()); // False

        System.out.println(" -- Terminal de vendas do Dener --");
        System.out.println("Digite a quantidade de potes vendidos: ");

        int qtdvendida = Integer.parseInt(scanner.nextLine());

        poteChocolate.registrarSaida(qtdvendida);

        scanner.close();

    }
}
