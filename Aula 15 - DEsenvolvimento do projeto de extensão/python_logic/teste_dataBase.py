import sqlite3

def inicialiar_banco():
    "garantir que a tabela exista ai iniciar a aplicação"
    conexao = sqlite3.connect("estoque.db")
    cursor =conexao.cursor()
    cursor.execute("""
                   CREATE TABLE IF NOT EXISTS insumos(
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       nome TEXT NOT NULL,
                       quantidade REAL NOT NULL,
                       UNIDADE TEXT NOT NULL
                   )
                   """)
    conexao.commit()
    conexao.close()
    print("Conectado")


def salvar_insumos_no_bd(nome, quantidade, unidade):
    " Recebe os dados dinâmicos da tela e joga nos plaholders "
    try:
        conexao = sqlite3.connect('estoque.db')
        cursor = conexao.cursor()
        
        cursor.execute("""
                       INSERT INTO insumos (nome,quantidade, unit)
                       VALUES (?,?,?)
                       """, (nome,quantidade, unidade)
                       )
        conexao.commit()
        conexao.close()
        return True
    
    except Exception as e:
        print(f"Erro ao salvar: {e}")
        return False


def listar_insumos_do_db():
    "realiza a leitura dos dados salvos"
    conexao = sqlite3.connect("estoque.db")
    cursor = conexao.cursor()
    
    cursor.execute("SELECT id,nome,quantidade,unidade FROM insumos")
    dados = cursor.fetchall()
    
    conexao.close()
    return dados