import sqlite3

def inicializar_banco():
    """ Cria a conexão com o arquivo de banco de dados e cria a tabela se ela não existir """
    conexao = sqlite3.connect("estoque_sorveteria.db")
    cursor = conexao.cursor()
    
    # Criando a tabela de insumos com tipos de dados adequados
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS insumos (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nome TEXT NOT NULL,
            quantidade REAL NOT NULL,
            unidade TEXT NOT NULL
        )
    """)
    
    conexao.commit()
    conexao.close()
    print("[BD] Banco de dados e tabela 'insumos' prontos para uso.")

def salvar_insumo_no_bd(nome, quantidade, unidade):
    """ Insere um novo registro de insumo no banco de dados """
    try:
        conexao = sqlite3.connect("estoque_sorveteria.db")
        cursor = conexao.cursor()
        
        # Usando Placeholders (?) por segurança contra SQL Injection
        cursor.execute("""
            INSERT INTO insumos (nome, quantidade, unidade) 
            VALUES (?, ?, ?)
        """, (nome, float(quantidade), unidade))
        
        conexao.commit()
        conexao.close()
        return True
    except Exception as e:
        print(f"[BD] Erro ao salvar dados: {e}")
        return False