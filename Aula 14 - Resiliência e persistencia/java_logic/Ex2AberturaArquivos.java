import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Ex2AberturaArquivos {

    public static void main(String[] args) {
        // Agora a lógica está dentro de um método, o Java sabe por onde começar!
        // O código fatal

        try (FileWriter escritor = new FileWriter("log_sistema.txt", true)) {
            escritor.write("Operação realizada em: " + java.time.LocalDateTime.now() + "\n");
            System.out.println("Log registrado no disco com sucesso.");

        } catch (IOException e) {
            System.out.println("Falha de hardware: Disco protegido ou cheio.");

        } finally {
            // O Finally executa sempre, garantindo a auditoria do sistema
            System.out.println("[AUDITORIA] Recursos de I/O verificados e seguros.");
        }
    }
}