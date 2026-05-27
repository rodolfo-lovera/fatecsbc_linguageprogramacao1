"""
PROJETO DE EXTENSÃO: SISTEMA DE GESTÃO DA SORVETERIA DO DENER
FASE 4: Robustez, Persistência de Dados e Entrega Final
"""

import os
from decimal import Decimal, InvalidOperation

ARQUIVO_DADOS = "estoque.csv"


# --- 1. CLASSE DE NEGÓCIO ENCAPSULADA ---
class Insumo:
    def __init__(self, nome: str, estoque_atual: int, estoque_ideal: int, preco_custo: Decimal):
        self.__nome = nome
        self.__estoque_ideal = estoque_ideal
        self.preco_custo = preco_custo
        self.estoque_atual = estoque_atual

    @property
    def nome(self) -> str:
        return self.__nome

    @property
    def estoque_ideal(self) -> int:
        return self.__estoque_ideal

    @property
    def estoque_atual(self) -> int:
        return self.__estoque_atual

    @estoque_atual.setter
    def estoque_atual(self, quantidade: int):
        if quantidade < 0:
            print(f"\n❌ ERRO: Quantidade {quantidade} inválida para '{self.__nome}'. Estoque mantido.")
        else:
            self.__estoque_atual = quantidade            

    @property
    def preco_custo(self) -> Decimal:
        return self.__preco_custo

    @preco_custo.setter
    def preco_custo(self, preco: Decimal):
        if preco <= 0:
            print(f"\n❌ ERRO: Preço de custo de '{self.__nome}' deve ser maior que zero!")
            self.__preco_custo = Decimal("0.01")
        else:
            self.__preco_custo = preco

    def calcular_falta(self) -> int:
        if self.estoque_atual >= self.estoque_ideal:
            return 0
        return self.estoque_ideal - self.estoque_atual

    def calcular_custo_reposicao(self) -> Decimal:
        return self.calcular_falta() * self.preco_custo

    def precisa_reposicao_critica(self) -> bool:
        return self.estoque_atual <= (self.estoque_ideal * 0.5)

    # Converte o objeto em uma linha de texto padronizada para o arquivo CSV
    def para_linha_csv(self) -> str:
        return f"{self.__nome};{self.estoque_atual};{self.estoque_ideal};{self.preco_custo}\n"


# --- 2. SISTEMA DE ENTRADA E SAÍDA DE DADOS (PERSISTÊNCIA I/O) ---

def carregar_dados(lista_insumos: list):
    """
    Lê o arquivo CSV e reconstrói os objetos em memória.
    Aplica lógica de 'Self-Healing' se o arquivo não existir.
    """
    if not os.path.exists(ARQUIVO_DADOS):
        print(f"\nℹ️ Informativo: Arquivo '{ARQUIVO_DADOS}' não encontrado. Criando base de dados inicial...")
        # Cria a base inicial com os dados padrão do Dener
        lista_insumos.append(Insumo("Base de Baunilha (L)", 12, 40, Decimal("15.50")))
        lista_insumos.append(Insumo("Calda de Chocolate (Kg)", 3, 15, Decimal("28.90")))
        lista_insumos.append(Insumo("Morango Fresco (Cx)", 18, 20, Decimal("8.25")))
        salvar_dados(lista_insumos)
        return

    try:
        # Uso do Gerenciador de Contexto 'with' para garantir o fechamento seguro do arquivo
        with open(ARQUIVO_DADOS, "r", encoding="utf-8") as arquivo:
            for linha in arquivo:
                linha = linha.strip()
                if not linha:
                    continue
                # Divide os atributos utilizando o separador ponto e vírgula
                nome, atual, ideal, preco = linha.split(";")
                lista_insumos.append(Insumo(nome, int(atual), int(ideal), Decimal(preco)))
        print(f"✔️ {len(lista_insumos)} registros carregados do arquivo local com sucesso!")
    except Exception as e:
        print(f"❌ Erro crítico ao ler o arquivo de persistência: {e}")


def salvar_dados(lista_insumos: list):
    """Grava o estado atual de toda a lista de objetos no arquivo físico."""
    try:
        with open(ARQUIVO_DADOS, "w", encoding="utf-8") as arquivo:
            for insumo in lista_insumos:
                arquivo.write(insumo.para_linha_csv())
    except Exception as e:
        print(f"❌ Erro crítico ao gravar os dados no disco: {e}")


# --- 3. CONTROLE PRINCIPAL E BLINDAGEM DA INTERFACE (CLI) ---

estoque_insumos = []
carregar_dados(estoque_insumos)

while True:
    print("\n==================================================")
    print("      SISTEMA DE ESTOQUE COMPLETO - SORVETERIA    ")
    print("==================================================")
    print("1. Cadastrar Novo Insumo")
    print("2. Listar Todos os Insumos e Estoques")
    print("3. Emitir Alerta de Reposição Crítica")
    print("4. Sair do Sistema")
    print("--------------------------------------------------")
    
    opcao = input("Escolha uma opção (1-4): ").strip()
    
    # --- OPÇÃO 1: CADASTRAR NOVO INSUMO (TRATAMENTO DE ERROS) ---
    if opcao == "1":
        print("\n--- CADASTRO DE NOVO INSUMO ---")
        nome = input("Nome do Insumo: ").strip()
        if not nome:
            print("❌ Erro: O nome do item não pode estar vazio!")
            continue
            
        # Try/Except aninhados para garantir resiliência contra inputs caóticos
        try:
            atual = int(input("Quantidade em Estoque Atual: "))
            ideal = int(input("Quantidade de Estoque Ideal (Meta): "))
        except ValueError:
            print("❌ ERRO: As quantidades de estoque atual e ideal devem ser números inteiros válidos!")
            continue

        try:
            preco_input = input("Preço de Custo unitário (Ex: 10.50): ").strip()
            preco = Decimal(preco_input)
        except (ValueError, InvalidOperation):
            print("❌ ERRO: O preço de custo deve ser um valor numérico decimal válido (utilize ponto como separador)!")
            continue
        
        # Criação está segura contra falhas de conversão de tipos
        novo_insumo = Insumo(nome, atual, ideal, preco)
        estoque_insumos.append(novo_insumo)
        salvar_dados(estoque_insumos)  # Gravação imediata de segurança (Persistence)
        print(f"✔️ Insumo '{novo_insumo.nome}' salvo e persistido com sucesso!")
        
    # --- OPÇÃO 2: LISTAR INVENTÁRIO ---
    elif opcao == "2":
        print("\n==================================================================")
        print("                 INVENTÁRIO ATUAL DA SORVETERIA                  ")
        print("==================================================================")
        print(f"{'Item':<25} | {'Est. Atual':<10} | {'Est. Ideal':<10} | {'Preço Custo':<12}")
        print("-" * 66)
        
        valor_total_estoque = Decimal("0.00")
        for insumo in estoque_insumos:
            print(f"{insumo.nome:<25} | {insumo.estoque_atual:<10} | {insumo.estoque_ideal:<10} | R$ {insumo.preco_custo:>9}")
            valor_total_estoque += insumo.estoque_atual * insumo.preco_custo
            
        print("-" * 66)
        print(f"VALOR TOTAL INVESTIDO NO ESTOQUE ATUAL: R$ {valor_total_estoque}")
        print("==================================================================")
        
    # --- OPÇÃO 3: ALERTA CRÍTICO ---
    elif opcao == "3":
        print("\n==================================================================")
        print("          ⚠️ ALERTA DE REPOSIÇÃO CRÍTICA (ESTOQUE <= 50%)        ")
        print("==================================================================")
        print(f"{'Item':<25} | {'Falta Comprar':<13} | {'Custo de Reposição':<18}")
        print("-" * 66)
        
        investimento_necessario = Decimal("0.00")
        itens_em_alerta = 0
        
        for insumo in estoque_insumos:
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
        salvar_dados(estoque_insumos)  # Garante a consistência final dos arquivos
        print("\nDados salvos com segurança em disco. Sistema encerrado. Até logo, Dener!")
        break
        
    else:
        print("\n❌ Opção inválida! Escolha um número entre 1 e 4.")