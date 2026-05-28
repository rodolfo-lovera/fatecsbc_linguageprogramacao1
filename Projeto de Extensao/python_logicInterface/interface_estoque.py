from database_manager import inicializar_banco, salvar_insumo_no_bd
import tkinter as tk
from tkinter import messagebox

class Application:
    def __init__(self, root):
        self.root = root
        self.root.title("Sorveteria do Dener - Controle de Estoque")
        self.root.geometry("450x400")
        self.root.configure(bg="#f4f4f9") # Cor de fundo leve

        # --- TÍTULO DA TELA ---
        self.lbl_titulo = tk.Label(root, text="Cadastro de Insumos", font=("Arial", 16, "bold"), bg="#f4f4f9", fg="#333")
        self.lbl_titulo.pack(pady=15)

        # --- CAMPOS DE ENTRADA (WIDGETS) ---
        # Nome do Insumo
        self.lbl_nome = tk.Label(root, text="Nome do Insumo (ex: Morango):", font=("Arial", 10), bg="#f4f4f9")
        self.lbl_nome.pack(anchor="w", padx=40)
        self.txt_nome = tk.Entry(root, font=("Arial", 11), width=40)
        self.txt_nome.pack(pady=5)

        # Quantidade
        self.lbl_quantidade = tk.Label(root, text="Quantidade Inicial:", font=("Arial", 10), bg="#f4f4f9")
        self.lbl_quantidade.pack(anchor="w", padx=40)
        self.txt_quantidade = tk.Entry(root, font=("Arial", 11), width=40)
        self.txt_quantidade.pack(pady=5)

        # Unidade de Medida
        self.lbl_unidade = tk.Label(root, text="Unidade de Medida (kg, g, Litros, Un):", font=("Arial", 10), bg="#f4f4f9")
        self.lbl_unidade.pack(anchor="w", padx=40)
        self.txt_unidade = tk.Entry(root, font=("Arial", 11), width=40)
        self.txt_unidade.pack(pady=5)

        # --- BOTÃO DE AÇÃO ---
        self.btn_cadastrar = tk.Button(root, text="Salvar Insumo", font=("Arial", 11, "bold"), 
                                       bg="#4CAF50", fg="white", width=20, command=self.capturar_dados)
        self.btn_cadastrar.pack(pady=25)
        
    
    def capturar_dados(self):
        nome = self.txt_nome.get()
        qtd = self.txt_quantidade.get()
        unidade = self.txt_unidade.get()

        if not nome or not qtd or not unidade:
            messagebox.showwarning("Atenção", "Por favor, preencha todos os campos!")
            return

        try:
            # Tenta converter a quantidade para número antes de enviar ao banco
            qtd_validada = float(qtd)
        except ValueError:
            messagebox.showerror("Erro de Digitação", "A quantidade deve ser um número válido (ex: 10 ou 2.5)!")
            return

        # CHAMADA DO BANCO DE DADOS
        sucesso = salvar_insumo_no_bd(nome, qtd_validada, unidade)

        if sucesso:
            messagebox.showinfo("Sucesso!", f"O insumo '{nome}' foi gravado com segurança no SQLite!")
            # Limpa os campos
            self.txt_nome.delete(0, tk.END)
            self.txt_quantidade.delete(0, tk.END)
            self.txt_unidade.delete(0, tk.END)
        else:
            messagebox.showerror("Erro Crítico", "Não foi possível salvar os dados no banco.")

# Inicialização da interface
if __name__ == "__main__":
    inicializar_banco()
    window = tk.Tk()
    app = Application(window)
    window.mainloop() # Mantém a janela aberta rodando em loop