import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AuditoriaEstoque {

    public static void main(String[] args) {
        System.out.println("--- Terminal de Auditoria: Sorveteria do Dener ---");
        System.out.println("[SISTEMA] Tentando carregar banco de dados de estoque...");

        // 1. ZONA DE RISCO: O Java tenta apenas LER o arquivo.
        try (BufferedReader leitor = new BufferedReader(new FileReader("estoque_dener.txt"))) {

            System.out.println("\n--- ITENS EM ESTOQUE ---");
            String linha;
            while ((linha = leitor.readLine()) != null) {
                System.out.println(linha);
            }

        } catch (FileNotFoundException e) {
            // 2. O ESCUDO (FALLBACK): O arquivo não existe? O Java cai aqui em vez de
            // quebrar!
            System.out.println("\n[ALERTA] Base de dados não encontrada no disco.");
            System.out.println("[SISTEMA] Inicializando uma nova base de dados vazia...");

            // 3. A AUTORREPARAÇÃO: Usamos o FileWriter para CRIAR o arquivo fisicamente.
            try (FileWriter escritor = new FileWriter("estoque_dener.txt")) {
                escritor.write("--- REGISTRO DE ESTOQUE DA SORVETERIA ---\n");
                System.out.println("[SISTEMA] Arquivo 'estoque_dener.txt' criado com sucesso!");

            } catch (IOException ex) {
                System.out.println("\n[ERRO CRÍTICO] Sem permissão para escrever no disco.");
            }

        } catch (IOException e) {
            System.out.println("\n[ERRO GERAL] Falha na comunicação com o arquivo.");
        }

        System.out.println("\n[LOG] Fim da rotina de auditoria.");
    }
}