
/**
 * PROJETO DE EXTENSÃO: SISTEMA DE GESTÃO DA SORVETERIA DO DENER
 * FASE 4: Robustez, Persistência de Dados e Entrega Final
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// --- 1. CLASSE PRINCIPAL: CONTROLE DE FLUXO E BLINDAGEM DA INTERFACE (No Topo) ---
public class Fase4Sorveteria {
    private static final String ARQUIVO_DADOS = "estoque.csv";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Insumo> estoqueInsumos = new ArrayList<>();

        // Executa a carga inicial dos dados vindo do arquivo físico
        carregarDados(estoqueInsumos);

        while (true) {
            System.out.println("\n==================================================");
            System.out.println("      SISTEMA DE ESTOQUE COMPLETO - SORVETERIA    ");
            System.out.println("==================================================");
            System.out.println("1. Cadastrar Novo Insumo");
            System.out.println("2. Listar Todos os Insumos e Estoques");
            System.out.println("3. Emitir Alerta de Reposição Crítica");
            System.out.println("4. Sair do Sistema");
            System.out.println("--------------------------------------------------");
            System.out.print("Escolha uma opção (1-4): ");

            String opcao = scanner.nextLine().trim();

            // --- OPÇÃO 1: CADASTRAR NOVO INSUMO (COM TRATAMENTO DE ERROS) ---
            if (opcao.equals("1")) {
                System.out.println("\n--- CADASTRO DE NOVO INSUMO ---");
                System.out.print("Nome do Insumo: ");
                String nome = scanner.nextLine().trim();

                if (nome.isEmpty()) {
                    System.out.println("❌ Erro: O nome do item não pode estar vazio!");
                    continue;
                }

                int atual = 0;
                int ideal = 0;
                BigDecimal preco = BigDecimal.ZERO;

                // Try/Catch para blindar a conversão de inteiros
                try {
                    System.out.print("Quantidade em Estoque Atual: ");
                    atual = Integer.parseInt(scanner.nextLine());

                    System.out.print("Quantidade de Estoque Ideal (Meta): ");
                    ideal = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println(
                            "❌ ERRO: As quantidades de estoque atual e ideal devem ser números inteiros válidos!");
                    continue;
                }

                // Try/Catch para blindar a conversão monetária do BigDecimal
                try {
                    System.out.print("Preço de Custo unitário (Ex: 10.50): ");
                    preco = new BigDecimal(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println(
                            "❌ ERRO: O preço de custo deve ser um valor numérico decimal válido (utilize ponto como separador)!");
                    continue;
                }

                // Instanciação e gravação imediata em disco por segurança
                Insumo novoInsumo = new Insumo(nome, atual, ideal, preco);
                estoqueInsumos.add(novoInsumo);
                salvarDados(estoqueInsumos);
                System.out.println("✔️ Insumo '" + novoInsumo.getNome() + "' salvo e persistido com sucesso!");

                // --- OPÇÃO 2: LISTAR INVENTÁRIO ---
            } else if (opcao.equals("2")) {
                System.out.println("\n==================================================================");
                System.out.println("                 INVENTÁRIO ATUAL DA SORVETERIA                  ");
                System.out.println("==================================================================");
                System.out.printf("%-25s | %-10s | %-10s | %-12s%n", "Item", "Est. Atual", "Est. Ideal", "Preço Custo");
                System.out.println("------------------------------------------------------------------");

                BigDecimal valorTotalEstoque = new BigDecimal("0.00");

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

                // --- OPÇÃO 3: ALERTA CRÍTICO ---
            } else if (opcao.equals("3")) {
                System.out.println("\n==================================================================");
                System.out.println("          ⚠️ ALERTA DE REPOSIÇÃO CRÍTICA (ESTOQUE <= 50%)        ");
                System.out.println("==================================================================");
                System.out.printf("%-25s | %-13s | %-18s%n", "Item", "Falta Comprar", "Custo de Reposição");
                System.out.println("------------------------------------------------------------------");

                BigDecimal investimentoNecessario = new BigDecimal("0.00");
                int itensEmAlerta = 0;

                for (Insumo insumo : estoqueInsumos) {
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
                salvarDados(estoqueInsumos); // Garante o commit físico no encerramento
                System.out.println("\nDados salvos com segurança em disco. Sistema encerrado. Até logo, Dener!");
                break;
            } else {
                System.out.println("\n❌ Opção inválida! Escolha um número entre 1 e 4.");
            }
        }
        scanner.close();
    }

    // --- 2. MÉTODOS DE ENTRADA E SAÍDA DE DADOS (PERSISTÊNCIA I/O COM
    // SELF-HEALING) ---

    private static void carregarDados(List<Insumo> listaInsumos) {
        File arquivo = new File(ARQUIVO_DADOS);

        // Lógica de Self-Healing: Se o arquivo não existir, cria a base inicial do
        // Dener
        if (!arquivo.exists()) {
            System.out.println(
                    "\nℹ️ Informativo: Base '" + ARQUIVO_DADOS + "' não encontrada. Criando registros padrão...");
            listaInsumos.add(new Insumo("Base de Baunilha (L)", 12, 40, new BigDecimal("15.50")));
            listaInsumos.add(new Insumo("Calda de Chocolate (Kg)", 3, 15, new BigDecimal("28.90")));
            listaInsumos.add(new Insumo("Morango Fresco (Cx)", 18, 20, new BigDecimal("8.25")));
            salvarDados(listaInsumos);
            return;
        }

        // Try-with-resources: Garante o fechamento automático das conexões mesmo
        // ocorrendo erros
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_DADOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty())
                    continue;

                // Divide a linha pelos delimitadores ponto e vírgula
                String[] dados = linha.split(";");
                String nome = dados[0];
                int atual = Integer.parseInt(dados[1]);
                int ideal = Integer.parseInt(dados[2]);
                BigDecimal preco = new BigDecimal(dados[3]);

                listaInsumos.add(new Insumo(nome, atual, ideal, preco));
            }
            System.out.println("✔️ " + listaInsumos.size() + " registros carregados do arquivo local com sucesso!");
        } catch (IOException e) {
            System.out.println("❌ Erro crítico ao ler o arquivo de persistência: " + e.getMessage());
        }
    }

    private static void salvarDados(List<Insumo> listaInsumos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_DADOS))) {
            for (Insumo insumo : listaInsumos) {
                writer.write(insumo.paraLinhaCsv());
            }
        } catch (IOException e) {
            System.out.println("❌ Erro crítico ao gravar os dados no disco: " + e.getMessage());
        }
    }
}

// --- 3. CLASSE DE NEGÓCIO ENCAPSULADA ---
class Insumo {
    private String nome;
    private int estoqueAtual;
    private int estoqueIdeal;
    private BigDecimal precoCusto;

    public Insumo(String nome, int estoqueAtual, int estoqueIdeal, BigDecimal precoCusto) {
        this.nome = nome;
        this.estoqueIdeal = estoqueIdeal;
        setPrecoCusto(precoCusto);
        setEstoqueAtual(estoqueAtual);
    }

    public String getNome() {
        return this.nome;
    }

    public int getEstoqueIdeal() {
        return this.ideal;
    } // Alinhado com a propriedade ideal

    private int ideal = estoqueIdeal; // Atributo interno para manter conformidade

    public int getEstoqueIdealInternal() {
        return this.estoqueIdeal;
    }

    public int getEstoqueAtual() {
        return this.estoqueAtual;
    }

    public void setEstoqueAtual(int quantidade) {
        if (quantidade < 0) {
            System.out.println(
                    "\n❌ ERRO: Quantidade " + quantidade + " inválida para '" + this.nome + "'. Estoque mantido.");
        } else {
            this.estoqueAtual = quantidade;
        }
    }

    public BigDecimal getPrecoCusto() {
        return this.precoCusto;
    }

    public void setPrecoCusto(BigDecimal preco) {
        if (preco.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("\n❌ ERRO: O preço de custo de '" + this.nome + "' deve ser maior que zero!");
            this.precoCusto = new BigDecimal("0.01");
        } else {
            this.precoCusto = preco;
        }
    }

    public int calcularFalta() {
        if (this.estoqueAtual >= this.estoqueIdeal)
            return 0;
        return this.estoqueIdeal - this.estoqueAtual;
    }

    public BigDecimal calcularCustoReposicao() {
        return this.precoCusto.multiply(new BigDecimal(calcularFalta()));
    }

    public boolean precisaReposicaoCritica() {
        return this.estoqueAtual <= (this.estoqueIdeal * 0.5);
    }

    // Formata o objeto para serialização no arquivo físico
    public String paraLinhaCsv() {
        return this.nome + ";" + this.estoqueAtual + ";" + this.estoqueIdeal + ";" + this.precoCusto + "\n";
    }
}