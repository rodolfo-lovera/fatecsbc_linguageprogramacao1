"""
PROJETO DE EXTENSÃO: SISTEMA DE GESTÃO DA SORVETERIA DO DENER
FASE 1: Engenharia de Dados e Regras de Negócio (Abordagem Agnóstica/Primitiva)
"""

from decimal import Decimal

# --- 1. MODELAGEM DOS DADOS (Estruturas Primitivas e Alta Precisão) ---

# Insumo 1: Base de Baunilha
nome_item_1 = "Base de Baunilha (L)"
estoque_atual_1 = 12
estoque_ideal_1 = 40
preco_custo_1 = Decimal("15.50")

# Insumo 2: Calda de Chocolate
nome_item_2 = "Calda de Chocolate (Kg)"
estoque_atual_2 = 3
estoque_ideal_2 = 15
preco_custo_2 = Decimal("28.90")

# Insumo 3: Morango Fresco
nome_item_3 = "Morango Fresco (Cx)"
estoque_atual_3 = 18
estoque_ideal_3 = 20
preco_custo_3 = Decimal("8.25")


# --- 2. PROCESSAMENTO E APLICAÇÃO DA LÓGICA DE NEGÓCIO ---

# Cálculos para o Insumo 1
falta_item_1 = estoque_ideal_1 - estoque_atual_1
custo_reposicao_1 = falta_item_1 * preco_custo_1

# Cálculos para o Insumo 2
falta_item_2 = estoque_ideal_2 - estoque_atual_2
custo_reposicao_2 = falta_item_2 * preco_custo_2

# Cálculos para o Insumo 3
falta_item_3 = estoque_ideal_3 - estoque_atual_3
custo_reposicao_3 = falta_item_3 * preco_custo_3

# Cálculo do Investimento Total
investimento_total = custo_reposicao_1 + custo_reposicao_2 + custo_reposicao_3


# --- 3. OUTPUT DE AUDITORIA (Exibição Formatada) ---

print("==================================================")
print("        RELATÓRIO DE COMPRAS - SORVETERIA         ")
print("==================================================")

# Formatação das linhas utilizando f-strings e delimitadores de casas decimais
print(f"Item: {nome_item_1:<23} | Falta: {falta_item_1:>2} | Custo Reposição: R$ {custo_reposicao_1:>7}")
print(f"Item: {nome_item_2:<23} | Falta: {falta_item_2:>2} | Custo Reposição: R$ {custo_reposicao_2:>7}")
print(f"Item: {nome_item_3:<23} | Falta: {falta_item_3:>2} | Custo Reposição: R$ {custo_reposicao_3:>7}")

print("--------------------------------------------------")
print(f"INVESTIMENTO TOTAL NECESSÁRIO: R$ {investimento_total}")
print("==================================================")