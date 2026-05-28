from flask import Flask, render_template, request, redirect, url_for, flash
import sqlite3
# Importamos a lógica de banco de dados construída na aula anterior
from database_manager import inicializar_banco, salvar_insumo_no_bd

app = Flask(__name__)
app.secret_key = "sorvete_dener_chave_secreta" # Necessário para exibir mensagens de feedback

def listar_insumos_do_bd():
    """ Busca todos os registros do banco para exibir na tabela web """
    conexao = sqlite3.connect("estoque_sorveteria.db")
    cursor = conexao.cursor()
    cursor.execute("SELECT id, nome, quantidade, unidade FROM insumos")
    dados = cursor.fetchall()
    conexao.close()
    return dados

@app.route("/", methods=["GET", "POST"])
def index():
    if request.method == "POST":
        # Captura os dados enviados pelo formulário HTML
        nome = request.form.get("nome")
        quantidade = request.form.get("quantidade")
        unidade = request.form.get("unidade")
        
        # Validação de dados (Resiliência do software)
        if not nome or not quantidade or not unidade:
            flash("Preencha todos os campos!", "erro")
        else:
            try:
                qtd_validada = float(quantidade)
                if qtd_validada < 0:
                    flash("A quantidade não pode ser negativa!", "erro")
                else:
                    # Grava no banco de dados SQLite
                    sucesso = salvar_insumo_no_bd(nome, qtd_validada, unidade)
                    if sucesso:
                        flash(f"Insumo '{nome}' salvo com sucesso!", "sucesso")
                    else:
                        flash("Erro crítico ao salvar no banco.", "erro")
            except ValueError:
                flash("A quantidade precisa ser um número!", "erro")
                
        return redirect("/")

    # Se for requisição GET, renderiza a página trazendo a lista atualizada do estoque
    estoque_atual = listar_insumos_do_bd()
    return render_template("estoque.html", insumos=estoque_atual)

if __name__ == "__main__":
    inicializar_banco() # Garante que a tabela exista antes do servidor subir
    print("[URL] Acesse o sistema em: http://127.0.0.1:5000")
    app.run(debug=True)