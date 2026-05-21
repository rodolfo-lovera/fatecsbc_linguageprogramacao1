
/**
 * PROJETO DE EXTENSÃO: SISTEMA DE GESTÃO DA SORVETERIA DO DENER
 * FASE 1: Engenharia de Dados e Regras de Negócio (Abordagem Agnóstica/Primitiva)
 */
import java.math.BigDecimal;

public class Fase1Sorveteria {
    public static void main(String[] args) {

        // --- 1. MODELAGEM DOS DADOS (Estruturas Primitivas e Alta Precisão) ---

        // Insumo 1: Base de Baunilha
        String nomeItem1 = "Base de Baunilha (L)";
        int estoqueAtual1 = 12;
        int estoqueIdeal1 = 40;
        BigDecimal precoCusto1 = new BigDecimal("15.50");

        // Insumo 2: Calda de Chocolate
        String nomeItem2 = "Calda de Chocolate (Kg)";
        int estoqueAtual2 = 3;
        int estoqueIdeal2 = 15;
        BigDecimal precoCusto2 = new BigDecimal("28.90");

        // Insumo 3: Morango Fresco
        String nomeItem3 = "Morango Fresco (Cx)";
        int estoqueAtual3 = 18;
        int estoqueIdeal3 = 20;
        BigDecimal precoCusto3 = new BigDecimal("8.25");

        // --- 2. PROCESSAMENTO E APLICAÇÃO DA LÓGICA DE NEGÓCIO ---

        // Cálculos para o Insumo 1
        int faltaItem1 = estoqueIdeal1 - estoqueAtual1;
        BigDecimal custoReposicao1 = precoCusto1.multiply(new BigDecimal(faltaItem1));

        // Cálculos para o Insumo 2
        int faltaItem2 = estoqueIdeal2 - estoqueAtual2;
        BigDecimal custoReposicao2 = precoCusto2.multiply(new BigDecimal(faltaItem2));

        // Cálculos para o Insumo 3
        int faltaItem3 = estoqueIdeal3 - estoqueAtual3;
        BigDecimal custoReposicao3 = precoCusto3.multiply(new BigDecimal(faltaItem3));

        // Cálculo do Investimento Total
        BigDecimal investimentoTotal = custoReposicao1
                .add(custoReposicao2)
                .add(custoReposicao3);

        // --- 3. OUTPUT DE AUDITORIA (Exibição Formatada) ---

        System.out.println("==================================================");
        System.out.println("        RELATÓRIO DE COMPRAS - SORVETERIA         ");
        System.out.println("==================================================");

        // Formatação de strings usando printf para alinhar as colunas na CLI
        System.out.printf("Item: %-23s | Falta: %2d | Custo Reposição: R$ %7.2f%n", nomeItem1, faltaItem1,
                custoReposicao1);
        System.out.printf("Item: %-23s | Falta: %2d | Custo Reposição: R$ %7.2f%n", nomeItem2, faltaItem2,
                custoReposicao2);
        System.out.printf("Item: %-23s | Falta: %2d | Custo Reposição: R$ %7.2f%n", nomeItem3, faltaItem3,
                custoReposicao3);

        System.out.println("--------------------------------------------------");
        System.out.printf("INVESTIMENTO TOTAL NECESSÁRIO: R$ %7.2f%n", investimentoTotal);
        System.out.println("==================================================");
    }
}
