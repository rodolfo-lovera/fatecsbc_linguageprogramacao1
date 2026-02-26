""" O fluxo do programa seguirá estes passos lógicos:
Entrada: Solicitar ao usuário o primeiro número.
Entrada: Solicitar ao usuário o segundo número.
Processamento: Realizar os cálculos de Adição, Subtração, Multiplicação e Divisão
               utilizando os operadores aritméticos.
Saída: Exibir os resultados formatados para o usuário.
"""

# Passo 1: Entrada de Dados
# Utilizamos float() para permitir cálculos com casas decimais
numero1 = int(input("Digite o primeiro número: "))
numero2 = int(input("Digite o segundo número: "))

# Passo 2: Processamento (Cálculos Aritméticos)

soma = numero1 + numero2
subtracao = numero1 - numero2
multiplicacao = numero1 * numero2
divisao = numero1 / numero2 

# Passo 3: Saída de Resultados Formatados
print(f"Soma (A + B): {soma}")
print(f"Subtração (A - B): {subtracao}")
print(f"Multiplicação (A * B): {multiplicacao}")
print(f"Divisão (A / B): {divisao}")