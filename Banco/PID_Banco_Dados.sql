-- =============================
-- TABELAS BÁSICAS
-- =============================

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

-- Tabela Pessoa e seus dados básicos
CREATE TABLE pessoa (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE CHECK (char_length(cpf) = 11),
    sexo CHAR(1) NOT NULL CHECK (sexo IN ('M', 'F')),
    data_nascimento DATE NOT NULL
);

-- Endereço (apenas 1 endereço por pessoa, se mudar, atualiza)
CREATE TABLE endereco_pessoa (
    id SERIAL PRIMARY KEY,
    pessoa_id INT NOT NULL UNIQUE,
    logradouro VARCHAR(100) NOT NULL,
    numero VARCHAR(5) NOT NULL,
    complemento VARCHAR(50),
    bairro VARCHAR(100) NOT NULL,
    cidade_id INT NOT NULL,
    cep CHAR(8) NOT NULL CHECK (char_length(cep) = 8),
    CONSTRAINT fk_endereco_pessoa_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id),
    CONSTRAINT fk_endereco_pessoa_cidade FOREIGN KEY (cidade_id) REFERENCES cidade(id)
);

-- Telefones da pessoa (podem ter vários, tipo opcional)
CREATE TABLE telefone_pessoa (
    id SERIAL PRIMARY KEY,
    pessoa_id INT NOT NULL,
    numero VARCHAR(15) NOT NULL,
    tipo_telefone VARCHAR(20), -- ex: residencial, celular, comercial
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
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE endereco_clinica (
    id SERIAL PRIMARY KEY,
    clinica_id INT NOT NULL UNIQUE,
    logradouro VARCHAR(100) NOT NULL,
    numero VARCHAR(5) NOT NULL,
    complemento VARCHAR(50),
    bairro VARCHAR(100) NOT NULL,
    cidade_id INT NOT NULL,
    cep CHAR(8) NOT NULL CHECK (char_length(cep) = 8),
    CONSTRAINT fk_endereco_clinica_clinica FOREIGN KEY (clinica_id) REFERENCES clinica(id),
    CONSTRAINT fk_endereco_clinica_cidade FOREIGN KEY (cidade_id) REFERENCES cidade(id)
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
    diagnostico VARCHAR(500),
    historico_familiar VARCHAR(100),
    CONSTRAINT fk_prontuario_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id)
);

-- Detalhes do prontuário (tabelas de alergias, vacinas, etc)

CREATE TABLE alergia (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    descricao_alergia VARCHAR(500) NOT NULL,
    CONSTRAINT fk_alergia_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

CREATE TABLE vacina (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    descricao_vacina VARCHAR(500) NOT NULL,
    CONSTRAINT fk_vacina_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

CREATE TABLE procedimento (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    descricao_procedimento VARCHAR(500) NOT NULL,
    CONSTRAINT fk_procedimento_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

CREATE TABLE historico_cirurgico (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    descricao_cirurgica VARCHAR(500) NOT NULL,
    CONSTRAINT fk_historico_cirurgico_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

CREATE TABLE medicamento_diario (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    descricao_medicamento VARCHAR(500) NOT NULL,
    CONSTRAINT fk_medicamento_diario_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

-- Consultas e resultados

CREATE TABLE consulta (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    observacao VARCHAR(500),
    CONSTRAINT fk_consulta_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

CREATE TABLE resultado_consulta (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    exame_solicitado BOOLEAN NOT NULL,
    nome_exame VARCHAR(100),
    receita VARCHAR(1000),
    CONSTRAINT fk_resultado_consulta_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
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
    ativo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_medico_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id)
);

CREATE TABLE crm (
    id SERIAL PRIMARY KEY,
    medico_id INT NOT NULL UNIQUE,
    numero VARCHAR(20) NOT NULL,
    CONSTRAINT fk_crm_medico FOREIGN KEY (medico_id) REFERENCES medico(id)
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
-- LOGS DE ALTERAÇÕES (exemplo para médico, paciente, prontuário)
-- =============================

CREATE TABLE log_alteracao_medico (
    id SERIAL PRIMARY KEY,
    medico_id INT NOT NULL,
    campo_alterado VARCHAR(100) NOT NULL,
    valor_antigo TEXT,
    valor_novo TEXT,
    data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_log_medico FOREIGN KEY (medico_id) REFERENCES medico(id)
);

CREATE TABLE log_alteracao_paciente (
    id SERIAL PRIMARY KEY,
    paciente_id INT NOT NULL,
    campo_alterado VARCHAR(100) NOT NULL,
    valor_antigo TEXT,
    valor_novo TEXT,
    data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_log_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id)
);

CREATE TABLE log_alteracao_prontuario (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    campo_alterado VARCHAR(100) NOT NULL,
    valor_antigo TEXT,
    valor_novo TEXT,
    data_alteracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_log_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

-- =============================
-- TRIGGERS
-- =============================

-- 1) Trigger para gravar acesso ao prontuário no log_acesso_prontuario
CREATE OR REPLACE FUNCTION log_acesso()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO log_acesso_prontuario (medico_id, prontuario_id, data_acesso)
    VALUES (NEW.medico_id, NEW.prontuario_id, CURRENT_TIMESTAMP);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_log_acesso
AFTER INSERT ON medico_acesso_prontuario
FOR EACH ROW EXECUTE FUNCTION log_acesso();

-- 2) Trigger para atualizar status para REVOGADA ao remover acesso
CREATE OR REPLACE FUNCTION revogar_solicitacao()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE solicitacao_acesso_prontuario
    SET status = 'REVOGADA',
        revogado = TRUE,
        data_resposta = CURRENT_TIMESTAMP
    WHERE medico_id = OLD.medico_id
      AND paciente_id = (SELECT paciente_id FROM prontuario WHERE id = OLD.prontuario_id);
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_revogar_solicitacao
AFTER DELETE ON medico_acesso_prontuario
FOR EACH ROW EXECUTE FUNCTION revogar_solicitacao();

-- =============================
-- VIEW PARA MÉDICOS PACIENTES AUTORIZADOS
-- =============================

CREATE OR REPLACE VIEW medico_paciente_autorizados AS
SELECT 
    m.id AS medico_id,
    p.id AS paciente_id,
    mp.data_liberacao,
    pe.nome AS nome_paciente,
    pm.nome AS nome_medico
FROM medico_acesso_prontuario mp
JOIN prontuario pr ON pr.id = mp.prontuario_id
JOIN paciente p ON pr.paciente_id = p.id
JOIN pessoa pe ON p.pessoa_id = pe.id
JOIN medico m ON mp.medico_id = m.id
JOIN pessoa pm ON m.pessoa_id = pm.id;

-- =============================
-- TABELA DE USUÁRIOS PARA LOGIN (email e senha hash)
-- =============================

CREATE TYPE tipo_usuario AS ENUM ('PACIENTE', 'MEDICO', 'ADMIN');

CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    tipo tipo_usuario NOT NULL,
    pessoa_id INT NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_usuario_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id)
);

-- =============================
-- EXEMPLO DE INSERÇÃO DE ESTADOS E CIDADES
-- =============================

INSERT INTO estado (sigla, nome) VALUES
('AC', 'Acre'), ('AL', 'Alagoas'), ('AP', 'Amapá'), ('AM', 'Amazonas'), ('BA', 'Bahia'),
('CE', 'Ceará'), ('DF', 'Distrito Federal'), ('ES', 'Espírito Santo'), ('GO', 'Goiás'),
('MA', 'Maranhão'), ('MT', 'Mato Grosso'), ('MS', 'Mato Grosso do Sul'), ('MG', 'Minas Gerais'),
('PA', 'Pará'), ('PB', 'Paraíba'), ('PR', 'Paraná'), ('PE', 'Pernambuco'), ('PI', 'Piauí'),
('RJ', 'Rio de Janeiro'), ('RN', 'Rio Grande do Norte'), ('RS', 'Rio Grande do Sul'),
('RO', 'Rondônia'), ('RR', 'Roraima'), ('SC', 'Santa Catarina'), ('SP', 'São Paulo'),
('SE', 'Sergipe'), ('TO', 'Tocantins');