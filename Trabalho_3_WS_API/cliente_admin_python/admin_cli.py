import requests
import sys

SERVER_IP = "localhost"
BASE_URL = f"http://{SERVER_IP}:8080/api"


def cadastrar_admin():
    print("\n--- Cadastrar Administrador ---")
    nome = input("Nome: ")
    email = input("E-mail: ")
    payload = {"nome": nome, "email": email, "tipo": "ADMIN"}

    resp = requests.post(f"{BASE_URL}/usuarios", json=payload)
    if resp.status_code == 201:
        dados = resp.json()
        print(f"[SUCESSO] Admin criado! ID: {dados['id']} | Nome: {dados['nome']}")
        return dados["id"]
    else:
        print(f"[ERRO {resp.status_code}] {resp.text}")
        return None


def criar_canal(admin_id):
    print("\n--- Criar Canal de Notificações ---")
    if not admin_id:
        admin_id = int(input("Informe o seu ID de Admin: "))
    nome = input("Nome do Canal: ")
    descricao = input("Descrição: ")

    payload = {"nome": nome, "descricao": descricao, "adminId": admin_id}
    resp = requests.post(f"{BASE_URL}/canais", json=payload)

    if resp.status_code == 201:
        dados = resp.json()
        print(f"[SUCESSO] Canal '{dados['nome']}' criado com ID: {dados['id']}")
    else:
        print(f"[ERRO {resp.status_code}] {resp.text}")


def listar_canais():
    print("\n--- Canais Disponíveis ---")
    resp = requests.get(f"{BASE_URL}/canais")
    if resp.status_code == 200:
        canais = resp.json()
        if not canais:
            print("Nenhum canal cadastrado ainda.")
        for c in canais:
            print(f"ID: {c['id']} | Nome: {c['nome']} | Inscritos: {len(c.get('inscritosIds', []))}")
    else:
        print(f"[ERRO {resp.status_code}] {resp.text}")


def publicar_notificacao(admin_id):
    print("\n--- Publicar Notificação ---")
    if not admin_id:
        admin_id = int(input("Informe o seu ID de Admin: "))
    listar_canais()
    canal_id = int(input("ID do Canal de destino: "))
    titulo = input("Título da Notificação: ")
    mensagem = input("Mensagem: ")

    payload = {
        "adminId": admin_id,
        "canalId": canal_id,
        "titulo": titulo,
        "mensagem": mensagem,
    }
    resp = requests.post(f"{BASE_URL}/notificacoes", json=payload)

    if resp.status_code == 201:
        dados = resp.json()
        print(f"[SUCESSO] Notificação #{dados['id']} publicada em {dados['dataHora']}!")
    else:
        print(f"[ERRO {resp.status_code}] {resp.text}")


def menu():
    admin_id_atual = None
    while True:
        print("\n=========================================")
        print(f" CLIENTE ADMIN (PYTHON) | ID Logado: {admin_id_atual or 'Nenhum'}")
        print("=========================================")
        print("1. Cadastrar Administrador")
        print("2. Criar Canal de Notificações")
        print("3. Listar Canais")
        print("4. Publicar Notificação em um Canal")
        print("0. Sair")
        opcao = input("Escolha uma opção: ")

        try:
            if opcao == "1":
                admin_id_atual = cadastrar_admin()
            elif opcao == "2":
                criar_canal(admin_id_atual)
            elif opcao == "3":
                listar_canais()
            elif opcao == "4":
                publicar_notificacao(admin_id_atual)
            elif opcao == "0":
                sys.exit(0)
            else:
                print("Opção inválida.")
        except requests.exceptions.ConnectionError:
            print(f"[FALHA DE REDE] Não foi possível conectar ao servidor em {BASE_URL}")


if __name__ == "__main__":
    menu()