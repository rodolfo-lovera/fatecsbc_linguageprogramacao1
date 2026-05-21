
/**
 * PROJETO DE EXTENSÃO: SISTEMA DE GESTÃO DA SORVETERIA DO DENER
 * FASE 3: Modelagem e Refatoração para a Orientação a Objetos (POO)
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// --- 1. CLASSE PRINCIPAL: CONTROLE DE FLUXO (CLI) ---
public class Fase3Sorveteria {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // BANCO DE DADOS EM MEMÓRIA: Agora uma lista tipada estritamente para guardar
        // Objetos Insumo
        List<Insumo> estoqueInsumos = new ArrayList<>();

        // Inicializando a lista com os objetos da Sorveteria do Dener
        estoqueInsumos.add(new Insumo("Base de Baunilha (L)", 12, 40, new BigDecimal("15.50")));
        estoqueInsumos.add(new Insumo("Calda de Chocolate (Kg)", 3, 15, new BigDecimal("28.90")));
        estoqueInsumos.add(new Insumo("Morango Fresco (Cx)", 18, 20, new BigDecimal("8.25")));

        // Loop principal da aplicação
        while (true) {
            System.out.println("\n==================================================");
            System.out.println("     SISTEMA DE ESTOQUE (ORIENTADO A OBJETOS)     ");
            System.out.println("==================================================");
            System.out.println("1. Cadastrar Novo Insumo");
            System.out.println("2. Listar Todos os Insumos e Estoques");
            System.out.println("3. Emitir Alerta de Reposição Crítica");
            System.out.println("4. Sair do Sistema");
            System.out.println("--------------------------------------------------");
            System.out.print("Escolha uma opção (1-4): ");

            String opcao = scanner.nextLine().trim();

            // --- OPÇÃO 1: CADASTRAR NOVO INSUMO ---
            if (opcao.equals("1")) {
                System.out.println("\n--- CADASTRO DE NOVO INSUMO ---");
                System.out.print("Nome do Insumo: ");
                String nome = scanner.nextLine().trim();

                System.out.print("Quantidade em Estoque Atual: ");
                int atual = Integer.parseInt(scanner.nextLine());

                System.out.print("Quantidade de Estoque Ideal (Meta): ");
                int ideal = Integer.parseInt(scanner.nextLine());

                System.out.print("Preço de Custo unitário (Ex: 10.50): ");
                BigDecimal preco = new BigDecimal(scanner.nextLine());

                // Instanciação limpa e segura do objeto
                Insumo novoInsumo = new Insumo(nome, atual, ideal, preco);
                estoqueInsumos.add(novoInsumo);
                System.out.println(
                        "✔️ Objeto Insumo '" + novoInsumo.getNome() + "' instanciado e armazenado com sucesso!");

                // --- OPÇÃO 2: LISTAR INVENTÁRIO (INTERAGINDO COM OBJETOS) ---
            } else if (opcao.equals("2")) {
                System.out.println("\n==================================================================");
                System.out.println("                 INVENTÁRIO ATUAL DA SORVETERIA                  ");
                System.out.println("==================================================================");
                System.out.printf("%-25s | %-10s | %-10s | %-12s%n", "Item", "Est. Atual", "Est. Ideal", "Preço Custo");
                System.out.println("------------------------------------------------------------------");

                BigDecimal valorTotalEstoque = new BigDecimal("0.00");

                // Varre a coleção de objetos (não há mais necessidade de fazer castings
                // manuais)
                for (Insumo insumo : estoqueInsumos) {
                    System.out.printf("%-25s | %-10d | %-10d | R$ %9.2f%n",
                            insumo.getNome(), insumo.getEstoqueAtual(), insumo.getEstoqueIdeal(),
                            insumo.getPrecoCusto());

                    BigDecimal valorItem = insumo.getPrecoCusto().multiply(new BigDecimal(insumo.getEstoqueAtual()));
                    valorTotalEstoque = valorTotalEstoque.add(valorItem);
                }

                System.out.println("------------------------------------------------------------------");
                System.out.printf("VALOR TOTAL INVESTIDO NO ESTOQUE ATUAL: R$ %9.2f%n", valorTotalEstoque);
                System.out.println("==================================================================");

                // --- OPÇÃO 3: ALERTA CRÍTICO UTILIZANDO A INTELIGÊNCIA DO OBJETO ---
            } else if (opcao.equals("3")) {
                System.out.println("\n==================================================================");
                System.out.println("          ⚠️ ALERTA DE REPOSIÇÃO CRÍTICA (ESTOQUE <= 50%)        ");
                System.out.println("==================================================================");
                System.out.printf("%-25s | %-13s | %-18s%n", "Item", "Falta Comprar", "Custo de Reposição");
                System.out.println("------------------------------------------------------------------");

                BigDecimal investimentoNecessario = new BigDecimal("0.00");
                int itensEmAlerta = 0;

                for (Insumo insumo : estoqueInsumos) {
                    // Toda a lógica complexa foi delegada para os métodos do próprio objeto
                    if (insumo.precisaReposicaoCritica()) {
                        itensEmAlerta++;
                        investimentoNecessario = investimentoNecessario.add(insumo.calcularCustoReposicao());

                        System.out.printf("%-25s | %-13d | R$ %15.2f%n",
                                insumo.getNome(), insumo.calcularFalta(), insumo.calcularCustoReposicao());
                    }
                }

                if (itensEmAlerta == 0) {
                    System.out.println("🎉 Todos os insumos estão operando acima da margem crítica!");
                } else {
                    System.out.println("------------------------------------------------------------------");
                    System.out.printf("INVESTIMENTO TOTAL PARA REGULARIZAÇÃO: R$ %15.2f%n", investimentoNecessario);
                }
                System.out.println("==================================================================");

                // --- OPÇÃO 4: SAIR DO SISTEMA ---
            } else if (opcao.equals("4")) {
                System.out.println("\nEncerrando o sistema de gestão. Até logo, Dener!");
                break;
            } else {
                System.out.println("\n❌ Opção inválida! Escolha um número entre 1 e 4.");
            }
        }

        scanner.close();
    }
}

// --- 2. O MOLDE REGULADOR: CLASSE DE NEGÓCIO ENCAPSULADA ---
class Insumo {
    private String nome;
    private int estoqueAtual;
    private int estoqueIdeal;
    private BigDecimal precoCusto;

    // Método Construtor
    public Insumo(String nome, int estoqueAtual, int estoqueIdeal, BigDecimal precoCusto) {
        this.nome = nome;
        this.estoqueIdeal = estoqueIdeal;

        // Uso dos setters internos para aplicar as regras de validação logo na
        // instanciação
        setPrecoCusto(precoCusto);
        setEstoqueAtual(estoqueAtual);
    }

    // --- ENCAPSULAMENTO (GETTERS & SETTERS COM VALIDAÇÃO) ---
    public String getNome() {
        return this.nome;
    }

    public int getEstoqueIdeal() {
        return this.estoqueIdeal;
    }

    public int getEstoqueAtual() {
        return this.estoqueAtual;
    }

    // Validação restritiva: Impede estoque físico negativo
    public void setEstoqueAtual(int quantidade) {
        if (quantidade < 0) {
            System.out.println("\n❌ ERRO DE OPERAÇÃO: Quantidade " + quantidade + " é inválida para o item '"
                    + this.nome + "'. O estoque não pode ser negativo!");
        } else {
            this.estoqueAtual = quantidade;
        }
    }

    public BigDecimal getPrecoCusto() {
        return this.precoCusto;
    }

    // Validação restritiva: Impede preço de custo nulo ou negativo
    public void setPrecoCusto(BigDecimal preco) {
        if (preco.compareTo(BigDecimal.ZERO) <= 0) {
            System.out
                    .println("\n❌ ERRO DE CADASTRO: O preço de custo de '" + this.nome + "' deve ser maior que zero!");
            this.precoCusto = new BigDecimal("0.01"); // Valor mínimo de contingência
        } else {
            this.precoCusto = preco;
        }
    }

    // --- MÉTODOS DE COMPORTAMENTO (INTELIGÊNCIA DO OBJETO) ---
    public int calcularFalta() {
        if (this.estoqueAtual >= this.estoqueIdeal) {
            return 0;
        }
        return this.estoqueIdeal - this.estoqueAtual;
    }

    public BigDecimal calcularCustoReposicao() {
        return this.precoCusto.multiply(new BigDecimal(calcularFalta()));
    }

    public boolean precisaReposicaoCritica() {
        // Regra de negócio: Alerta crítico se o estoque atual for <= 50% do ideal
        return this.estoqueAtual <= (this.estoqueIdeal * 0.5);
    }
}
