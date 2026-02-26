#include <iostream> // Biblioteca para entrada e saída de dados
#include <iomanip>  // Biblioteca para formatar a saída de casas decimais
using namespace std;

int main() {
    // Passo 1: Declaração de Variáveis (Tipagem Estática)    
    int numero1 = 0, numero2 = 0, soma, subtracao, multiplicacao;
    float divisao;

    // Passo 2: Entrada de Dados    
    cout << "Digite o primeiro numero: "; cin >> numero1;
    cout << "Digite o segundo numero: "; cin >> numero2;

    // Passo 3: Processamento (Calculos Aritmeticos)
    soma = numero1 + numero2;
    subtracao = numero1 - numero2;
    multiplicacao = numero1 * numero2;
    divisao = numero1 / numero2;

    // Passo 4: Saida de Resultados Formatados    
    cout << fixed << setprecision(2);    
    cout << "Soma: " << soma << endl;
    cout << "Subtracao: " << subtracao << endl;
    cout << "Multiplicacao: " << multiplicacao << endl;
    cout << "Divisao: " << divisao << endl;  

    return 0; // Indica que o programa terminou com sucesso
}