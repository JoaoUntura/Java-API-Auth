# Projeto de Autenticação Segura com Spring e Kong

Este projeto foi desenvolvido com foco em **segurança** e **observabilidade**, oferecendo um fluxo de autenticação robusto, resiliente contra ataques e totalmente auditável.

---

## 🔐 Fluxo de Autenticação

1. **Entrada via Kong Gateway**  
   - Rate Limiter configurado para mitigar ataques de força bruta e requisições excessivas.  
   - Kong é a **única porta de entrada externa**, garantindo isolamento da rede interna.

2. **Backend em Spring Security**  
   - Login com **detecção de device fingerprint**, restringindo múltiplos logins suspeitos.  
   - Login válido gera um **cookie temporário** para ativação do **2FA**.

3. **Two-Factor Authentication (2FA)**  
   - Envio de código de verificação via **e-mail**.  
   - Código armazenado no **Redis**, com TTL de 5 minutos.  
   - Limite de **3 tentativas** de validação.

4. **Geração de Tokens**  
   - Sucesso no 2FA gera **JWTs com roles** + **Refresh Token**.  
   - Refresh Token registrado no banco de dados.  
   - Endpoint de renovação:  
     - Valida o refresh token no banco.  
     - Revoga o token anterior.  
     - Gera novos JWT + Refresh Token.  

---

## ⚙️ Arquitetura e Infraestrutura

- **Docker Compose**: todos os serviços (Spring, Redis, Banco, ELK, Kong) executam em containers.  
- **Kong Gateway**: único ponto exposto externamente, com balanceamento, segurança e rate limiting.  
- **Redis**: utilizado para armazenamento temporário dos códigos 2FA.  
- **Banco de Dados (Postgres)**: persistência de usuários, tokens e logs de autenticação.  
- **ELK Stack (Elasticsearch, Logstash, Kibana)**: centraliza logs do Kong e do Spring Boot para observabilidade e auditoria.  

👉 Repositório da infraestrutura: [auth_infra](https://github.com/JoaoUntura/auth_infra)

---

## 📊 Observabilidade e Logs

- **Kong** + **Spring Boot** → Logs enviados ao **Logstash**.  
- **Elasticsearch** → Armazena e indexa os logs.  
- **Kibana** → Interface gráfica para análise e auditoria de segurança.  

---

##  Como Rodar o Projeto

1. Clone o repositório de infraestrutura:  
   ```bash
   git clone https://github.com/JoaoUntura/auth_infra.git
   cd auth_infra
   docker compose up -d


## 🌐 Rotas da Aplicação

| Rota          | Método | Descrição                                                                 |
|---------------|--------|---------------------------------------------------------------------------|
| `/login`      | POST   | Realiza login inicial com **device fingerprint**                          |
| `/login/2fa/` | POST   | Valida código **2FA** enviado por e-mail (necessita cookie `2fa`)         |
| `/user`       | POST   | Registra um novo usuário                                                  |
| `/user`       | GET    | Retorna dados do usuário autenticado (necessário JWT válido)              |
| `:5601`       | GET    | Acesso à interface do **Kibana** (logs e auditoria)                       |

Este projeto utiliza GitHub Actions para automação de build e deploy da imagem Docker.
Sempre que um push é realizado na branch main, a pipeline:

Faz build da imagem do projeto.

Faz login no Docker Hub.

Publica a imagem no Docker Hub.
