"""
PROJETO DE EXTENSÃO: SISTEMA DE GESTÃO DA SORVETERIA DO DENER
FASE 3: Modelagem e Refatoração para a Orientação a Objetos (POO)
"""

from decimal import Decimal


# --- 1. O MOLDE REGULADOR: CLASSE DE NEGÓCIO ENCAPSULADA ---
class Insumo:
    def __init__(self, nome: str, estoque_atual: int, estoque_ideal: int, preco_custo: Decimal):
        self.__nome = nome
        self.__estoque_ideal = estoque_ideal
        
        # Uso dos setters internos para validar os dados no momento da criação
        self.preco_custo = preco_custo
        self.estoque_atual = estoque_atual

    # --- ENCAPSULAMENTO (PROPEDADES / GETTERS) ---
    @property
    def nome(self) -> str:
        return self.__nome

    @property
    def estoque_ideal(self) -> int:
        return self.__estoque_ideal

    @property
    def estoque_atual(self) -> int:
        return self.__estoque_atual

    # --- VALIDAÇÃO RESTRITIVA (SETTER DE ESTOQUE) ---
    @estoque_atual.setter
    def estoque_atual(self, quantidade: int):
        # Regra de Negócio: Impede que o estoque assuma valores físicos impossíveis
        if quantidade < 0:
            print(f"\n❌ ERRO DE OPERAÇÃO: Quantidade {quantidade} é inválida para o item '{self.__nome}'. O estoque não pode ser negativo!")
        else:
            self.__estoque_atual = quantidade

    @property
    def preco_custo(self) -> Decimal:
        return self.__preco_custo

    # --- VALIDAÇÃO RESTRITIVA (SETTER DE PREÇO) ---
    @preco_custo.setter
    def preco_custo(self, preco: Decimal):
        if preco <= 0:
            print(f"\n❌ ERRO DE CADASTRO: O preço de custo de '{self.__nome}' deve ser maior que zero!")
            self.__preco_custo = Decimal("0.01")  # Valor mínimo de contingência
        else:
            self.__preco_custo = preco

    # --- MÉTODOS DE COMPORTAMENTO (INTELIGÊNCIA DO OBJETO) ---
    def calcular_falta(self) -> int:
        if self.__estoque_atual >= self.__estoque_ideal:
            return 0
        return self.__estoque_ideal - self.__estoque_atual

    def calcular_custo_reposicao(self) -> Decimal:
        return self.calcular_falta() * self.__preco_custo

    def precisa_reposicao_critica(self) -> bool:
        # Regra de negócio: Alerta crítico se o estoque atual for <= 50% do ideal
        return self.__estoque_atual <= (self.__estoque_ideal * 0.5)


# --- 2. BANCO DE DADOS EM MEMÓRIA (Agora uma Lista de Objetos) ---
# Substituímos a lista de dicionários por instâncias tipadas da Classe Insumo
estoque_insumos = [
    Insumo("Base de Baunilha (L)", 12, 40, Decimal("15.50")),
    Insumo("Calda de Chocolate (Kg)", 3, 15, Decimal("28.90")),
    Insumo("Morango Fresco (Cx)", 18, 20, Decimal("8.25"))
]


# --- 3. LOOP PRINCIPAL DA CLI INTERATIVA ---
while True:
    print("\n==================================================")
    print("     SISTEMA DE ESTOQUE (ORIENTADO A OBJETOS)     ")
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
        
        # Criação do objeto passando os parâmetros para o construtor
        novo_insumo = Insumo(nome, atual, ideal, preco)
        estoque_insumos.append(novo_insumo)
        print(f"✔️ Objeto Insumo '{novo_insumo.nome}' instanciado e armazenado com sucesso!")
        
    # --- OPÇÃO 2: LISTAR INVENTÁRIO (INTERAGINDO COM OBJETOS) ---
    elif opcao == "2":
        print("\n==================================================================")
        print("                 INVENTÁRIO ATUAL DA SORVETERIA                  ")
        print("==================================================================")
        print(f"{'Item':<25} | {'Est. Atual':<10} | {'Est. Ideal':<10} | {'Preço Custo':<12}")
        print("-" * 66)
        
        valor_total_estoque = Decimal("0.00")
        
        # Varre a coleção acessando os métodos e propriedades de cada objeto
        for insumo in estoque_insumos:
            print(f"{insumo.nome:<25} | {insumo.estoque_atual:<10} | {insumo.estoque_ideal:<10} | R$ {insumo.preco_custo:>9}")
            valor_total_estoque += insumo.estoque_atual * insumo.preco_custo
            
        print("-" * 66)
        print(f"VALOR TOTAL INVESTIDO NO ESTOQUE ATUAL: R$ {valor_total_estoque}")
        print("==================================================================")
        
    # --- OPÇÃO 3: ALERTA CRÍTICO UTILIZANDO OS MÉTODOS DO OBJETO ---
    elif opcao == "3":
        print("\n==================================================================")
        print("          ⚠️ ALERTA DE REPOSIÇÃO CRÍTICA (ESTOQUE <= 50%)        ")
        print("==================================================================")
        print(f"{'Item':<25} | {'Falta Comprar':<13} | {'Custo de Reposição':<18}")
        print("-" * 66)
        
        investimento_necessario = Decimal("0.00")
        itens_em_alerta = 0
        
        for insumo in estoque_insumos:
            # Toda a lógica de verificação e cálculo agora é resolvida internamente pelo próprio objeto
            if insumo.precisa_reposicao_critica():
                itens_em_alerta += 1
                investimento_necessario += insumo.calcular_custo_reposicao()
                
                print(f"{insumo.nome:<25} | {insumo.calcular_falta():<13} | R$ {insumo.calcular_custo_reposicao():>15}")
                
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
        
    else:
        print("\n❌ Opção inválida! Escolha um número entre 1 e 4.")