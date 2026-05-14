from produto import Produto

# 1. Instanciação: Criando objetos nas "gavetas da memória" (Heap)
pote_chocolate = Produto("Pote Chocolate 2L", 15, 100) # Estoque real (15) < 20% de 100
picole_limao = Produto("Picolé de Limão", 50, 100)    # Estoque real (50) ok

# 2. Testando a Inteligência (Passo 2)
print(f"Chocolate precisa comprar? {pote_chocolate.verificar_necessidade_compra()}") # Saída: True
print(f"Limão precisa comprar? {picole_limao.verificar_necessidade_compra()}")       # Saída: False

# 3. Testando a Blindagem (Passo 3)
# Tentando vender mais do que existe no estoque real do chocolate
pote_chocolate.registrar_saida(20) 
# Saída: Operação Bloqueada! (Evita que o Dener tenha -5 potes no sistema)