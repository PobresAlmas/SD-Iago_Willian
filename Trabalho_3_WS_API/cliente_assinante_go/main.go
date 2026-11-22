package main

import (
	"bufio"
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"os"
	"strconv"
	"strings"
)

// Altere para o IP da máquina do servidor quando testarem em computadores diferentes
const serverIP = "localhost"
const baseURL = "http://" + serverIP + ":8080/api"

type Usuario struct {
	ID    int64  `json:"id,omitempty"`
	Nome  string `json:"nome"`
	Email string `json:"email"`
	Tipo  string `json:"tipo"`
}

type CanalNotificacoes struct {
	ID           int64   `json:"id"`
	Nome         string  `json:"nome"`
	Descricao    string  `json:"descricao"`
	AdminID      int64   `json:"adminId"`
	InscritosIDs []int64 `json:"inscritosIds"`
}

type Notificacao struct {
	ID       int64  `json:"id"`
	CanalID  int64  `json:"canalId"`
	AdminID  int64  `json:"adminId"`
	Titulo   string `json:"titulo"`
	Mensagem string `json:"mensagem"`
	DataHora string `json:"dataHora"`
}

var reader = bufio.NewReader(os.Stdin)

func lerLinha(prompt string) string {
	fmt.Print(prompt)
	texto, _ := reader.ReadString('\n')
	return strings.TrimSpace(texto)
}

func lerInt64(prompt string) int64 {
	for {
		val, err := strconv.ParseInt(lerLinha(prompt), 10, 64)
		if err == nil {
			return val
		}
		fmt.Println("Valor inválido. Digite um número inteiro.")
	}
}

func cadastrarCliente() int64 {
	fmt.Println("\n--- Cadastrar Cliente Assinante ---")
	nome := lerLinha("Nome: ")
	email := lerLinha("E-mail: ")

	payload, _ := json.Marshal(Usuario{
		Nome:  nome,
		Email: email,
		Tipo:  "CLIENTE",
	})

	resp, err := http.Post(baseURL+"/usuarios", "application/json", bytes.NewBuffer(payload))
	if err != nil {
		fmt.Printf("[FALHA DE REDE] %v\n", err)
		return 0
	}
	defer resp.Body.Close()

	body, _ := io.ReadAll(resp.Body)
	if resp.StatusCode == http.StatusCreated {
		var criado Usuario
		json.Unmarshal(body, &criado)
		fmt.Printf("[SUCESSO] Cliente cadastrado! ID: %d | Nome: %s\n", criado.ID, criado.Nome)
		return criado.ID
	}

	fmt.Printf("[ERRO %d] %s\n", resp.StatusCode, string(body))
	return 0
}

func listarCanais() {
	fmt.Println("\n--- Canais Disponíveis ---")
	resp, err := http.Get(baseURL + "/canais")
	if err != nil {
		fmt.Printf("[FALHA DE REDE] %v\n", err)
		return
	}
	defer resp.Body.Close()

	var canais []CanalNotificacoes
	json.NewDecoder(resp.Body).Decode(&canais)

	if len(canais) == 0 {
		fmt.Println("Nenhum canal cadastrado ainda.")
		return
	}

	for _, c := range canais {
		fmt.Printf("ID: %d | Nome: %s | Descrição: %s | Inscritos: %d\n",
			c.ID, c.Nome, c.Descricao, len(c.InscritosIDs))
	}
}

func inscreverEmCanal(clienteID int64) {
	if clienteID == 0 {
		clienteID = lerInt64("Informe o seu ID de Cliente: ")
	}
	listarCanais()
	canalID := lerInt64("ID do Canal para se inscrever: ")

	url := fmt.Sprintf("%s/canais/%d/inscrever/%d", baseURL, canalID, clienteID)
	resp, err := http.Post(url, "application/json", nil)
	if err != nil {
		fmt.Printf("[FALHA DE REDE] %v\n", err)
		return
	}
	defer resp.Body.Close()

	body, _ := io.ReadAll(resp.Body)
	if resp.StatusCode == http.StatusOK {
		fmt.Printf("[SUCESSO] Inscrito no canal %d!\n", canalID)
	} else {
		fmt.Printf("[ERRO %d] %s\n", resp.StatusCode, string(body))
	}
}

func cancelarInscricao(clienteID int64) {
	if clienteID == 0 {
		clienteID = lerInt64("Informe o seu ID de Cliente: ")
	}
	canalID := lerInt64("ID do Canal para cancelar inscrição: ")

	url := fmt.Sprintf("%s/canais/%d/inscrever/%d", baseURL, canalID, clienteID)
	req, _ := http.NewRequest(http.MethodDelete, url, nil)
	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		fmt.Printf("[FALHA DE REDE] %v\n", err)
		return
	}
	defer resp.Body.Close()

	if resp.StatusCode == http.StatusOK {
		fmt.Printf("[SUCESSO] Inscrição removida do canal %d.\n", canalID)
	} else {
		fmt.Printf("[ERRO %d] Não foi possível cancelar a inscrição.\n", resp.StatusCode)
	}
}

func consultarNotificacoes(clienteID int64) {
	if clienteID == 0 {
		clienteID = lerInt64("Informe o seu ID de Cliente: ")
	}

	url := fmt.Sprintf("%s/notificacoes/cliente/%d", baseURL, clienteID)
	resp, err := http.Get(url)
	if err != nil {
		fmt.Printf("[FALHA DE REDE] %v\n", err)
		return
	}
	defer resp.Body.Close()

	var notificacoes []Notificacao
	json.NewDecoder(resp.Body).Decode(&notificacoes)

	fmt.Printf("\n--- Minhas Notificações (Cliente ID %d) ---\n", clienteID)
	if len(notificacoes) == 0 {
		fmt.Println("Nenhuma notificação encontrada para os seus canais inscritos.")
		return
	}

	for _, n := range notificacoes {
		fmt.Printf("[%s] Canal #%d | %s: %s\n", n.DataHora, n.CanalID, n.Titulo, n.Mensagem)
	}
}

func main() {
	var clienteIDAtual int64 = 0

	for {
		fmt.Println("\n=========================================")
		fmt.Printf(" CLIENTE ASSINANTE (GOLANG) | ID Logado: %d\n", clienteIDAtual)
		fmt.Println("=========================================")
		fmt.Println("1. Cadastrar Cliente")
		fmt.Println("2. Listar Canais Disponíveis")
		fmt.Println("3. Inscrever-se em um Canal")
		fmt.Println("4. Cancelar Inscrição em um Canal")
		fmt.Println("5. Consultar Minhas Notificações")
		fmt.Println("0. Sair")

		opcao := lerLinha("Escolha uma opção: ")

		switch opcao {
		case "1":
			clienteIDAtual = cadastrarCliente()
		case "2":
			listarCanais()
		case "3":
			inscreverEmCanal(clienteIDAtual)
		case "4":
			cancelarInscricao(clienteIDAtual)
		case "5":
			consultarNotificacoes(clienteIDAtual)
		case "0":
			os.Exit(0)
		default:
			fmt.Println("Opção inválida.")
		}
	}
}