"""
PROJETO DE EXTENSÃO: SISTEMA DE GESTÃO DA SORVETERIA DO DENER
FASE 2: Automatização de Fluxos e Coleções em Memória
"""

from decimal import Decimal

# --- 1. BANCO DE DADOS EM MEMÓRIA (Inicializado com os dados da Fase 1) ---
# Cada produto é representado por um dicionário dentro de uma lista dinâmica.
estoque_insumos = [
    {
        "nome": "Base de Baunilha (L)",
        "estoque_atual": 12,
        "estoque_ideal": 40,
        "preco_custo": Decimal("15.50")
    },
    {
        "nome": "Calda de Chocolate (Kg)",
        "estoque_atual": 3,
        "estoque_ideal": 15,
        "preco_custo": Decimal("28.90")
    },
    {
        "nome": "Morango Fresco (Cx)",
        "estoque_atual": 18,
        "estoque_ideal": 20,
        "preco_custo": Decimal("8.25")
    }
]

# --- 2. LOOP PRINCIPAL DA CLI ---
while True:
    print("\n==================================================")
    print("        SISTEMA DE ESTOQUE - SORVETERIA         ")
    print("==================================================")
    print("1. Cadastrar Novo Insumo")
    print("2. Listar Todos os Insumos e Estoques")
    print("3. Emitir Alerta de Reposição Crítica")
    print("4. Sair do Sistema")
    print("--------------------------------------------------")
    
    opcao = input("Escolha uma opção (1-4): ").strip()
    
    # --- OPÇÃO 1: CADASTRAR NOVO INSUMO ---
    if opcao == "1":
        print("\n--- CADASTRO DE NOVO INSUMO ---")
        nome = input("Nome do Insumo: ").strip()
        atual = int(input("Quantidade em Estoque Atual: "))
        ideal = int(input("Quantidade de Estoque Ideal (Meta): "))
        preco = Decimal(input("Preço de Custo unitário (Ex: 10.50): "))
        
        # Criando o novo registro (dicionário)
        novo_insumo = {
            "nome": nome,
            "estoque_atual": atual,
            "estoque_ideal": ideal,
            "preco_custo": preco
        }
        
        # Inserindo dinamicamente na lista em memória
        estoque_insumos.append(novo_insumo)
        print(f"✔️ '{nome}' cadastrado com sucesso!")
        
    # --- OPÇÃO 2: LISTAR TODOS OS INSUMOS ---
    elif opcao == "2":
        print("\n==================================================================")
        print("                 INVENTÁRIO ATUAL DA SORVETERIA                  ")
        print("==================================================================")
        print(f"{'Item':<25} | {'Est. Atual':<10} | {'Est. Ideal':<10} | {'Preço Custo':<12}")
        print("-" * 66)
        
        valor_total_estoque = Decimal("0.00")
        
        # Iterando pela lista de dicionários
        for insumo in estoque_insumos:
            print(f"{insumo['nome']:<25} | {insumo['estoque_atual']:<10} | {insumo['estoque_ideal']:<10} | R$ {insumo['preco_custo']:>9}")
            
            # Cálculo do valor financeiro atualmente parado no estoque real
            valor_total_estoque += insumo['estoque_atual'] * insumo['preco_custo']
            
        print("-" * 66)
        print(f"VALOR TOTAL INVESTIDO NO ESTOQUE ATUAL: R$ {valor_total_estoque}")
        print("==================================================================")
        
    # --- OPÇÃO 3: ALERTA DE REPOSIÇÃO CRÍTICA (PBL - Regra de Negócio) ---
    elif opcao == "3":
        print("\n==================================================================")
        print("          ⚠️ ALERTA DE REPOSIÇÃO CRÍTICA (ESTOQUE <= 50%)        ")
        print("==================================================================")
        print(f"{'Item':<25} | {'Falta Comprar':<13} | {'Custo de Reposição':<18}")
        print("-" * 66)
        
        investimento_necessario = Decimal("0.00")
        itens_em_alerta = 0
        
        for insumo in estoque_insumos:
            # Regra de negócio: Alerta se o estoque real for metade ou menos que o ideal
            limite_critico = insumo['estoque_ideal'] * 0.5
            
            if insumo['estoque_atual'] <= limite_critico:
                itens_em_alerta += 1
                unidades_em_falta = insumo['estoque_ideal'] - insumo['estoque_atual']
                custo_item_reposicao = unidades_em_falta * insumo['preco_custo']
                
                investimento_necessario += custo_item_reposicao
                
                print(f"{insumo['nome']:<25} | {unidades_em_falta:<13} | R$ {custo_item_reposicao:>15}")
                
        if itens_em_alerta == 0:
            print("🎉 Todos os insumos estão operando acima da margem crítica!")
        else:
            print("-" * 66)
            print(f"INVESTIMENTO TOTAL PARA REGULARIZAÇÃO: R$ {investimento_necessario}")
            
        print("==================================================================")
        
    # --- OPÇÃO 4: SAIR DO SISTEMA ---
    elif opcao == "4":
        print("\nEncerrando o sistema de gestão. Até logo, Dener!")
        break
        
    # --- TRATAMENTO DE OPÇÃO INVÁLIDA ---
    else:
        print("\n❌ Opção inválida! Escolha um número entre 1 e 4.")