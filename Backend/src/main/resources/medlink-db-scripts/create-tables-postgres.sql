-- Drop das tabelas se existirem
DROP TABLE IF EXISTS mensagem CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS log_acesso_prontuario CASCADE;
DROP TABLE IF EXISTS medico_acesso_prontuario CASCADE;
DROP TABLE IF EXISTS solicitacao_acesso_prontuario CASCADE;
DROP TABLE IF EXISTS consulta CASCADE;
DROP TABLE IF EXISTS historico_cirurgico CASCADE;
DROP TABLE IF EXISTS prontuario CASCADE;
DROP TABLE IF EXISTS paciente CASCADE;
DROP TABLE IF EXISTS medico_clinica_especialidade CASCADE;
DROP TABLE IF EXISTS medico CASCADE;
DROP TABLE IF EXISTS especialidade CASCADE;
DROP TABLE IF EXISTS telefone_clinica CASCADE;
DROP TABLE IF EXISTS clinica CASCADE;
DROP TABLE IF EXISTS telefone_pessoa CASCADE;
DROP TABLE IF EXISTS pessoa CASCADE;
DROP TABLE IF EXISTS endereco CASCADE;
DROP TABLE IF EXISTS cidade CASCADE;
DROP TABLE IF EXISTS estado CASCADE;


-- Drop dos tipos se existirem
DROP TYPE IF EXISTS status_solicitacao CASCADE;


DROP TABLE IF EXISTS vacina CASCADE;
DROP TABLE IF EXISTS alergia CASCADE;
DROP TABLE IF EXISTS diagnostico CASCADE;
DROP TABLE IF EXISTS medicamento CASCADE;

-- Tabela de Estados (com lista de todos os estados do Brasil)
CREATE TABLE estado (
    id SERIAL PRIMARY KEY,
    sigla CHAR(2) UNIQUE NOT NULL,
    nome VARCHAR(50) NOT NULL
);

-- Tabela de Cidades vinculadas ao Estado
CREATE TABLE cidade (
    id SERIAL PRIMARY KEY,
    estado_id INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    CONSTRAINT fk_cidade_estado FOREIGN KEY (estado_id) REFERENCES estado(id)
);

CREATE TABLE endereco (
    id SERIAL PRIMARY KEY,
    logradouro VARCHAR(100) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    complemento VARCHAR(50),
    bairro VARCHAR(100) NOT NULL,
    cidade_id INT NOT NULL,
    cep CHAR(8) NOT NULL CHECK (char_length(cep) = 8),
    CONSTRAINT fk_endereco_cidade FOREIGN KEY (cidade_id) REFERENCES cidade(id)
);

-- Tabela Pessoa e seus dados básicos
CREATE TABLE pessoa (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE CHECK (char_length(cpf) = 11),
    sexo CHAR(1) NOT NULL CHECK (sexo IN ('M', 'F')),
    data_nascimento DATE NOT NULL,
    endereco_id INT,
    CONSTRAINT fk_pessoa_endereco FOREIGN KEY (endereco_id) REFERENCES endereco(id)
);

-- Telefones da pessoa (podem ter vários, tipo opcional)
CREATE TABLE telefone_pessoa (
    id SERIAL PRIMARY KEY,
    pessoa_id INT NOT NULL,
    numero VARCHAR(15) NOT NULL,
    tipo_telefone VARCHAR(20),
    CONSTRAINT fk_telefone_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id)
);

-- =============================
-- CLINICAS
-- =============================

CREATE TABLE clinica (
    id SERIAL PRIMARY KEY,
    cnpj CHAR(14) NOT NULL UNIQUE CHECK (char_length(cnpj) = 14),
    razao_social VARCHAR(100) NOT NULL,
    nome_fantasia VARCHAR(100) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    endereco_id INT,
    CONSTRAINT fk_clinica_endereco FOREIGN KEY (endereco_id) REFERENCES endereco(id)
);

CREATE TABLE telefone_clinica (
    id SERIAL PRIMARY KEY,
    clinica_id INT NOT NULL,
    numero VARCHAR(15) NOT NULL,
    tipo_telefone VARCHAR(20),
    CONSTRAINT fk_telefone_clinica FOREIGN KEY (clinica_id) REFERENCES clinica(id)
);

-- =============================
-- PACIENTES E PRONTUÁRIOS
-- =============================

CREATE TABLE paciente (
    id SERIAL PRIMARY KEY,
    pessoa_id INT NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    convenio_medico VARCHAR(30),
    cartao_sus VARCHAR(15),
    ativo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_paciente_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id)
);

CREATE TABLE prontuario (
    id SERIAL PRIMARY KEY,
    paciente_id INT NOT NULL UNIQUE,
    tipo_sanguineo VARCHAR(3) NOT NULL,
    doador_orgao BOOLEAN NOT NULL,
    observacoes TEXT,
    historico_familiar VARCHAR(500),
    CONSTRAINT fk_prontuario_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id)
);

CREATE TABLE historico_cirurgico (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    descricao_cirurgica VARCHAR(500) NOT NULL,
    data_cirurgia DATE,
    CONSTRAINT fk_historico_cirurgico_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

-- =============================
-- TABELAS DE VACINAS, ALERGIAS, DIAGNÓSTICOS, MEDICAMENTOS
-- =============================

CREATE TABLE vacina (
    id SERIAL PRIMARY KEY,
    paciente_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    CONSTRAINT fk_vacina_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id)
);

CREATE TABLE alergia (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    substance VARCHAR(100),
    reaction VARCHAR(100),
    severity VARCHAR(50),
    CONSTRAINT fk_alergia_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

CREATE TABLE diagnostico (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    description VARCHAR(500) NOT NULL,
    date DATE NOT NULL,
    CONSTRAINT fk_diagnostico_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

CREATE TABLE medicamento (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    dosage VARCHAR(50),
    frequency VARCHAR(50),
    CONSTRAINT fk_medicamento_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

-- =============================
-- MÉDICOS E ESPECIALIDADES
-- =============================

CREATE TABLE especialidade (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    descricao VARCHAR(500) NOT NULL
);

CREATE TABLE medico (
    id SERIAL PRIMARY KEY,
    pessoa_id INT NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    crm VARCHAR(20) NOT NULL UNIQUE,
    CONSTRAINT fk_medico_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id)
);

CREATE TABLE medico_clinica_especialidade (
    id SERIAL PRIMARY KEY,
    medico_id INT NOT NULL,
    clinica_id INT NOT NULL,
    especialidade_id INT NOT NULL,
    CONSTRAINT fk_mce_medico FOREIGN KEY (medico_id) REFERENCES medico(id),
    CONSTRAINT fk_mce_clinica FOREIGN KEY (clinica_id) REFERENCES clinica(id),
    CONSTRAINT fk_mce_especialidade FOREIGN KEY (especialidade_id) REFERENCES especialidade(id),
    CONSTRAINT unq_mce UNIQUE (medico_id, clinica_id, especialidade_id)
);

CREATE TABLE consulta (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    observacao VARCHAR(500),
    CONSTRAINT fk_consulta_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

-- =============================
-- ACESSOS, AUTORIZAÇÕES E LOGS
-- =============================

CREATE TYPE status_solicitacao AS ENUM ('PENDENTE', 'ACEITA', 'RECUSADA', 'REVOGADA');

CREATE TABLE solicitacao_acesso_prontuario (
    id SERIAL PRIMARY KEY,
    medico_id INT NOT NULL,
    paciente_id INT NOT NULL,
    data_solicitacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status status_solicitacao NOT NULL DEFAULT 'PENDENTE',
    data_resposta TIMESTAMP,
    revogado BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_solicitacao_medico FOREIGN KEY (medico_id) REFERENCES medico(id),
    CONSTRAINT fk_solicitacao_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id),
    CONSTRAINT unq_medico_paciente UNIQUE (medico_id, paciente_id)
);

CREATE TABLE medico_acesso_prontuario (
    id SERIAL PRIMARY KEY,
    medico_id INT NOT NULL,
    prontuario_id INT NOT NULL,
    data_liberacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_map_medico FOREIGN KEY (medico_id) REFERENCES medico(id),
    CONSTRAINT fk_map_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id),
    CONSTRAINT unq_medico_prontuario UNIQUE (medico_id, prontuario_id)
);

CREATE TABLE log_acesso_prontuario (
    id SERIAL PRIMARY KEY,
    medico_id INT NOT NULL,
    prontuario_id INT NOT NULL,
    data_acesso TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_lap_medico FOREIGN KEY (medico_id) REFERENCES medico(id),
    CONSTRAINT fk_lap_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

-- =============================
-- USUÁRIO E AUTENTICAÇÃO
-- =============================

CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    pessoa_id INT NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(20) NOT NULL CHECK (perfil IN ('MEDICO', 'PACIENTE')),
    CONSTRAINT fk_usuario_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id)
);

-- =============================
-- MENSAGENS ENTRE PACIENTE E MÉDICO
-- =============================

CREATE TABLE mensagem (
    id SERIAL PRIMARY KEY,
    sender_id INTEGER NOT NULL,
    sender_type VARCHAR(20) NOT NULL,
    sender_name VARCHAR(100),
    recipient_id INTEGER NOT NULL,
    recipient_type VARCHAR(20) NOT NULL,
    recipient_name VARCHAR(100),
    text TEXT NOT NULL,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read BOOLEAN NOT NULL DEFAULT FALSE
);
