
/**
 * PROJETO DE EXTENSÃO: SISTEMA DE GESTÃO DA SORVETERIA DO DENER
 * FASE 2: Automatização de Fluxos e Coleções em Memória
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Fase2Sorveteria {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // --- 1. BANCO DE DADOS EM MEMÓRIA (Lista de Mapas Dinâmicos) ---
        List<Map<String, Object>> estoqueInsumos = new ArrayList<>();

        // Inicializando o Insumo 1
        Map<String, Object> item1 = new HashMap<>();
        item1.put("nome", "Base de Baunilha (L)");
        item1.put("estoque_atual", 12);
        item1.put("estoque_ideal", 40);
        item1.put("preco_custo", new BigDecimal("15.50"));
        estoqueInsumos.add(item1);

        // Inicializando o Insumo 2
        Map<String, Object> item2 = new HashMap<>();
        item2.put("nome", "Calda de Chocolate (Kg)");
        item2.put("estoque_atual", 3);
        item2.put("estoque_ideal", 15);
        item2.put("preco_custo", new BigDecimal("28.90"));
        estoqueInsumos.add(item2);

        // Inicializando o Insumo 3
        Map<String, Object> item3 = new HashMap<>();
        item3.put("nome", "Morango Fresco (Cx)");
        item3.put("estoque_atual", 18);
        item3.put("estoque_ideal", 20);
        item3.put("preco_custo", new BigDecimal("8.25"));
        estoqueInsumos.add(item3);

        // --- 2. LOOP PRINCIPAL DA CLI ---
        while (true) {
            System.out.println("\n==================================================");
            System.out.println("        SISTEMA DE ESTOQUE - SORVETERIA         ");
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

                // Criando o mapa estruturado para o novo insumo
                Map<String, Object> novoInsumo = new HashMap<>();
                novoInsumo.put("nome", nome);
                novoInsumo.put("estoque_atual", atual);
                novoInsumo.put("estoque_ideal", ideal);
                novoInsumo.put("preco_custo", preco);

                estoqueInsumos.add(novoInsumo);
                System.out.println("✔️ '" + nome + "' cadastrado com sucesso!");

                // --- OPÇÃO 2: LISTAR TODOS OS INSUMOS ---
            } else if (opcao.equals("2")) {
                System.out.println("\n==================================================================");
                System.out.println("                 INVENTÁRIO ATUAL DA SORVETERIA                  ");
                System.out.println("==================================================================");
                System.out.printf("%-25s | %-10s | %-10s | %-12s%n", "Item", "Est. Atual", "Est. Ideal", "Preço Custo");
                System.out.println("------------------------------------------------------------------");

                BigDecimal valorTotalEstoque = new BigDecimal("0.00");

                for (Map<String, Object> insumo : estoqueInsumos) {
                    String nome = (String) insumo.get("nome");
                    int atual = (Integer) insumo.get("estoque_atual");
                    int ideal = (Integer) insumo.get("estoque_ideal");
                    BigDecimal preco = (BigDecimal) insumo.get("preco_custo");

                    System.out.printf("%-25s | %-10d | %-10d | R$ %9.2f%n", nome, atual, ideal, preco);

                    // valorTotalEstoque += atual * preco
                    BigDecimal valorItem = preco.multiply(new BigDecimal(atual));
                    valorTotalEstoque = valorTotalEstoque.add(valorItem);
                }

                System.out.println("------------------------------------------------------------------");
                System.out.printf("VALOR TOTAL INVESTIDO NO ESTOQUE ATUAL: R$ %9.2f%n", valorTotalEstoque);
                System.out.println("==================================================================");

                // --- OPÇÃO 3: ALERTA DE REPOSIÇÃO CRÍTICA (PBL) ---
            } else if (opcao.equals("3")) {
                System.out.println("\n==================================================================");
                System.out.println("          ⚠️ ALERTA DE REPOSIÇÃO CRÍTICA (ESTOQUE <= 50%)        ");
                System.out.println("==================================================================");
                System.out.printf("%-25s | %-13s | %-18s%n", "Item", "Falta Comprar", "Custo de Reposição");
                System.out.println("------------------------------------------------------------------");

                BigDecimal investimentoNecessario = new BigDecimal("0.00");
                int itensEmAlerta = 0;

                for (Map<String, Object> insumo : estoqueInsumos) {
                    String nome = (String) insumo.get("nome");
                    int atual = (Integer) insumo.get("estoque_atual");
                    int ideal = (Integer) insumo.get("estoque_ideal");
                    BigDecimal preco = (BigDecimal) insumo.get("preco_custo");

                    // Regra de negócio: Alerta se o estoque real for metade ou menos que o ideal
                    double limiteCritico = ideal * 0.5;

                    if (atual <= limiteCritico) {
                        itensEmAlerta++;
                        int unidadesEmFalta = ideal - atual;
                        BigDecimal custoItemReposicao = preco.multiply(new BigDecimal(unidadesEmFalta));

                        investimentoNecessario = investimentoNecessario.add(custoItemReposicao);

                        System.out.printf("%-25s | %-13d | R$ %15.2f%n", nome, unidadesEmFalta, custoItemReposicao);
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

                // --- TRATAMENTO DE OPÇÃO INVÁLIDA ---
            } else {
                System.out.println("\n❌ Opção inválida! Escolha um número entre 1 e 4.");
            }
        }

        scanner.close();
    }
}