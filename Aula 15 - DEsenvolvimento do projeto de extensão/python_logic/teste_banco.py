import sqlite3

conexao = sqlite3.connect("minhasorveteria.db")
cursor = conexao.cursor()

cursor.execute(""" 
               CREATE TABLE IF NOT EXISTS insumos (
                   id INTEGER PRIMARY KEY AUTOINCREMENT,
                   nome TEXT,
                   quantidade REAL
               )
               """)

cursor.execute(""" 
               INSERT INTO insumos (nome,quantidade)
               VALUES (?,?) 
               """, ("Morango",15.5)
) 

# uso de placeholder "?"" para proteger a conexão com BD

conexao.commit()

cursor.execute("""
               SELECT id,nome,quantidade FROM insumos
               """)
dados = cursor.fetchall()

print(dados)

conexao.close()
print("Tabela Criada e dados salvo com sucesso!")
