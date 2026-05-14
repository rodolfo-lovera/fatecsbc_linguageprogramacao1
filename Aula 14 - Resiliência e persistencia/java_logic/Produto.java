public class Produto { 
    // Atributos Privados: O "Campo de Força" do Objeto
    private String nome;
    private int estoqueReal;
    private int estoqueIdeal;

    // Construtor: O momento em que o objeto "nasce" na memória
    public Produto(String nome, int estoqueReal, int estoqueIdeal) {
        this.nome = nome;
        this.estoqueReal = estoqueReal;
        this.estoqueIdeal = estoqueIdeal;
    }

    //Passo 2: Implementando a Inteligência (Nível Intermediário)
    public boolean verificarNecessidadeCompra() {
        // A inteligência de negócio agora reside na entidade
        double limiteMinimo = this.estoqueIdeal * 0.20;
        return this.estoqueReal < limiteMinimo;
    }

    // Passo 3: Blindagem de Dados (Nível Avançado)
    public void registrarSaida(int quantidade) {
        // Validação: Protege a integridade dos dados do Dener
        if (quantidade <= this.estoqueReal) {
            this.estoqueReal -= quantidade;
            System.out.println("Venda realizada! Novo saldo de " + this.nome + ": " + this.estoqueReal);
        } else {
            // Alerta de segurança contra erros de inventário[cite: 2]
            System.out.println("ERRO: Operação Bloqueada para " + this.nome + "!");
            System.out.println("Risco de Estoque Negativo. Saldo disponível: " + this.estoqueReal);
        }
    }    
}

