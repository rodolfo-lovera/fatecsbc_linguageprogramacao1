from flask import Flask, render_template, request, redirect, url_for,flash

from teste_dataBase import inicialiar_banco, salvar_insumos_no_bd, listar_insumos_do_db

app = Flask(__name__)
app.secret_key = 'chave_segura'

@app.route("/", methods=["GET","POST"])
def index():
    if request.method == "POST":
        
        nome = request.form.get("nome")
        quantidade = request.form.get("quantidade")
        unidade = request.form.get("unidade")
        
        if not nome or not quantidade or not unidade:
            flash("Preencha todos os campos.","erro")
        else:
            salvar_insumos_no_bd(nome, float(quantidade),unidade)
            flash("Salvo com sucesso!", "sucesso")
        
        return redirect(url_for('index'))
    
    estoque_atual = listar_insumos_do_db()
    
    return render_template("estoque.html", insumos=estoque_atual)

if __name__ == "__main__":
    inicialiar_banco()
    app.run(debug=True)