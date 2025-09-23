 🏥 MedLink - Sistema de Gestão Médica

Sistema web completo para gestão de prontuários médicos, desenvolvido como projeto acadêmico. Permite o gerenciamento de pacientes, médicos, consultas e prontuários eletrônicos com foco na segurança e privacidade dos dados médicos.

## 📋 Sobre o Projeto

O MedLink é uma aplicação full-stack que digitaliza e centraliza o gerenciamento de informações médicas, facilitando o acesso controlado aos prontuários e melhorando a comunicação entre médicos e pacientes.

### ✨ Funcionalidades Principais

#### 👨‍⚕️ Para Médicos
- **Dashboard** com estatísticas e resumo de atividades
- **Gerenciamento de Pacientes** - visualizar lista e detalhes
- **Prontuários Eletrônicos** - acesso completo aos dados médicos
- **Histórico Clínico** - consultas, diagnósticos, medicamentos, cirurgias
- **Sistema de Mensagens** - comunicação direta com pacientes
- **Relatórios** - aniversariantes, consultas, tipos de plano
- **Perfil** - gerenciar informações pessoais e profissionais

#### 👤 Para Pacientes
- **Dashboard Pessoal** - visão geral da saúde
- **Prontuário Digital** - acesso ao próprio histórico médico
- **Médicos Autorizados** - gerenciar permissões de acesso
- **Solicitações de Acesso** - aprovar/negar pedidos de médicos
- **Mensagens** - comunicação com médicos autorizados
- **Perfil** - atualizar dados pessoais

#### 🔐 Segurança e Controle
- Sistema de autenticação seguro
- Controle de acesso baseado em perfis (médico/paciente)
- Solicitações de acesso ao prontuário
- Log de acessos aos dados médicos
- Validação de dados médicos críticos

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 21** - Linguagem de programação
- **Spring Boot 3.4.5** - Framework principal
- **Spring Web** - API REST
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados
- **Lombok** - Redução de código boilerplate
- **SpringDoc OpenAPI** - Documentação da API
- **Maven** - Gerenciamento de dependências

### Frontend
- **Angular 20** - Framework frontend
- **TypeScript** - Linguagem de programação
- **Bootstrap 5** - Framework CSS
- **FontAwesome** - Ícones
- **Chart.js** - Gráficos e estatísticas
- **RxJS** - Programação reativa

### Banco de Dados
- **PostgreSQL** - Sistema de gerenciamento de banco de dados
- **Arquitetura relacional** com integridade referencial
- **Controle de acesso** granular aos dados

## 🚀 Como Executar o Projeto

### Pré-requisitos
- Java 21 ou superior
- Node.js 18+ e npm
- PostgreSQL 12+
- Git

### 1. Clone o Repositório
```bash
git clone https://github.com/seu-usuario/medlink.git
cd medlink
```

### 2. Configuração do Banco de Dados
```sql
-- Criar banco de dados
CREATE DATABASE medlink;

-- Executar o script SQL
\i Banco/PID_Banco_Dados.sql
```

### 3. Configuração do Backend
```bash
cd Backend

# Configurar application.properties (se necessário)
# spring.datasource.url=jdbc:postgresql://localhost:5432/medlink
# spring.datasource.username=seu_usuario
# spring.datasource.password=sua_senha

# Executar aplicação
./mvnw spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

### 4. Configuração do Frontend
```bash
cd Frontend

# Instalar dependências
npm install

# Executar aplicação
npm start
```

A aplicação web estará disponível em: `http://localhost:4200`

## 📁 Estrutura do Projeto

```
medlink/
├── Backend/                    # API REST em Spring Boot
│   ├── src/main/java/
│   │   └── br/fai/lds/medlink/
│   │       ├── controller/     # Controladores REST
│   │       ├── domain/         # Entidades e DTOs
│   │       ├── implementation/ # Implementações dos serviços
│   │       ├── port/          # Interfaces (ports)
│   │       ├── configuration/ # Configurações
│   │       └── exception/     # Tratamento de exceções
│   └── pom.xml               # Dependências Maven
├── Frontend/                  # Aplicação Angular
│   ├── src/app/
│   │   ├── pages/            # Páginas da aplicação
│   │   ├── services/         # Serviços Angular
│   │   ├── guards/           # Guards de autenticação
│   │   └── interceptors/     # Interceptadores HTTP
│   └── package.json          # Dependências npm
├── Banco/                    # Scripts do banco de dados
│   └── PID_Banco_Dados.sql  # Schema completo
└── Documentação/            # Documentos do projeto
    ├── Caso de Uso/         # Diagramas UML
    ├── Esboços das Telas/   # Mockups das interfaces
    └── Visão Comportamental/ # Especificações funcionais
```

## 🏗️ Arquitetura

### Backend - Arquitetura Hexagonal (Ports & Adapters)
- **Controllers**: Adaptadores de entrada (REST API)
- **Services**: Lógica de negócio
- **Ports**: Interfaces que definem contratos
- **DAOs**: Adaptadores de saída (acesso a dados)
- **DTOs**: Objetos de transferência de dados

### Frontend - Arquitetura por Módulos
- **Pages**: Componentes de página organizados por funcionalidade
- **Services**: Comunicação com API e lógica de negócio
- **Guards**: Proteção de rotas
- **Interceptors**: Tratamento global de requisições

## 🔌 API Endpoints

### Autenticação
- `POST /api/auth/login` - Login do usuário
- `POST /api/auth/logout` - Logout do usuário

### Pacientes
- `GET /api/patients` - Listar pacientes
- `GET /api/patients/{id}` - Buscar paciente por ID
- `PUT /api/patients/{id}` - Atualizar paciente
- `GET /api/patients/{id}/allergies` - Alergias do paciente
- `GET /api/patients/{id}/medications` - Medicamentos do paciente

### Médicos
- `GET /api/medics` - Listar médicos
- `GET /api/medics/{id}` - Buscar médico por ID
- `PUT /api/medics/{id}` - Atualizar médico

### Prontuários
- `GET /api/medical-records/{id}` - Buscar prontuário
- `POST /api/medical-records` - Criar prontuário
- `PUT /api/medical-records/{id}` - Atualizar prontuário

### Mensagens
- `GET /api/messages` - Listar mensagens
- `POST /api/messages` - Enviar mensagem

## 🎨 Interfaces do Sistema

O sistema possui interfaces distintas e intuitivas:

### Telas do Médico
- Login e cadastro
- Dashboard com estatísticas
- Lista de pacientes
- Prontuário detalhado (consultas, medicamentos, cirurgias, etc.)
- Sistema de mensagens
- Relatórios gerenciais
- Perfil profissional

### Telas do Paciente
- Dashboard pessoal
- Visualização do próprio prontuário
- Gerenciamento de médicos autorizados
- Solicitações de acesso
- Mensagens com médicos
- Perfil pessoal

## 🔒 Segurança

- **Autenticação**: Sistema de login seguro
- **Autorização**: Controle de acesso baseado em perfis
- **CORS**: Configurado para desenvolvimento e produção
- **Validação**: Validação de dados de entrada
- **Logs**: Registro de acessos aos prontuários

## 📊 Banco de Dados

### Principais Entidades
- **Pessoa**: Dados básicos (nome, CPF, endereço)
- **Paciente**: Informações específicas do paciente
- **Médico**: Dados profissionais (CRM, especialidades)
- **Prontuário**: Histórico médico completo
- **Consulta**: Registros de atendimentos
- **Mensagem**: Comunicação médico-paciente

### Relacionamentos
- Controle de acesso médico-prontuário
- Histórico de consultas e procedimentos
- Sistema de mensagens bidirecional
- Log de acessos para auditoria

## 👥 Equipe de Desenvolvimento

Este projeto foi desenvolvido como trabalho acadêmico


## 📚 Documentação Adicional

- **Casos de Uso**: Diagramas UML na pasta `Documentação/`
- **Mockups**: Esboços das telas na pasta `Documentação/`
- **Especificações**: Documentos técnicos detalhados
- **API Docs**: Disponível em `http://localhost:8080/swagger-ui.html`

## 🤝 Contribuição

Este é um projeto acadêmico, mas sugestões e melhorias são bem-vindas:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto é desenvolvido para fins acadêmicos.

## 📞 Contato

Para dúvidas ou sugestões sobre o projeto, entre em contato através do GitHub.
