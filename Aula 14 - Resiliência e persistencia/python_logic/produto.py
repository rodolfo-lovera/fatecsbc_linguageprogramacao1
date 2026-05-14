class Produto:
    # Passo 1: Definindo o Contrato (Nível Introdutório)
    def __init__(self, nome, estoque_real, estoque_ideal):
        # Atributos Privados (Encapsulamento com __ em Python)
        # Isso cria o "Campo de Força" que protege os dados do Dener
        self.__nome = nome
        self.__estoque_real = estoque_real
        self.__estoque_ideal = estoque_ideal
    
    # Passo 2: Implementando a Inteligência (Nível Intermediário)
    def verificar_necessidade_compra(self):
        # O objeto agora responde se o Dener precisa gastar tempo com pedidos
        limite_minimo = self.__estoque_ideal * 0.20
        return self.__estoque_real < limite_minimo
    
    # Passo 3: Blindagem de Dados (Nível Avançado)
    def registrar_saida(self, quantidade):
        # Validação de Invariante de Estado: impede saldo negativo
        if quantidade <= self.__estoque_real:
            self.__estoque_real -= quantidade
            print(f"Venda realizada! Novo saldo de {self.__nome}: {self.__estoque_real}")
        else:
            # Alerta que impede a falha de inventário físico
            print(f"ERRO: Operação Bloqueada para {self.__nome}!")
            print(f"Risco de Estoque Negativo. Saldo atual: {self.__estoque_real}")

