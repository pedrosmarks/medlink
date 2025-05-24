
CREATE TABLE pessoa (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    sexo CHAR(1) NOT NULL CHECK (sexo IN ('M', 'F')),
    data_nascimento DATE NOT NULL
);

CREATE TABLE telefone_pessoa (
    id SERIAL PRIMARY KEY,
    pessoa_id INT NOT NULL,
    numero VARCHAR(15) NOT NULL,
    CONSTRAINT fk_telefone_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id)
);

CREATE TABLE estado (
	id SERIAL PRIMARY KEY,
    sigla CHAR(2) UNIQUE NOT NULL,
    nome VARCHAR(50) NOT NULL
);

CREATE TABLE endereco_pessoa (
    id SERIAL PRIMARY KEY,
    pessoa_id INT NOT NULL,
    logradouro VARCHAR(100) NOT NULL,
    numero VARCHAR(5) NOT NULL,
    complemento VARCHAR(50),
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado CHAR(2) NOT NULL,
    cep VARCHAR(11) NOT NULL,
	
    CONSTRAINT fk_endereco_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id),
	CONSTRAINT fk_estado_pessoa FOREIGN KEY (estado) REFERENCES estado(sigla)
	
);

CREATE TABLE clinica (
    id SERIAL PRIMARY KEY,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    razao_social VARCHAR(100) NOT NULL,
    nome_fantasia VARCHAR(100) NOT NULL
);

CREATE TABLE telefone_clinica (
    id SERIAL PRIMARY KEY,
    clinica_id INT NOT NULL,
    numero VARCHAR(15) NOT NULL,
    CONSTRAINT fk_telefone_clinica FOREIGN KEY (clinica_id) REFERENCES clinica(id)
);

CREATE TABLE endereco_clinica (
    id SERIAL PRIMARY KEY,
    clinica_id INT NOT NULL,
    logradouro VARCHAR(100) NOT NULL,
    numero VARCHAR(5) NOT NULL,
    complemento VARCHAR(50),
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado CHAR(2) NOT NULL,
    cep VARCHAR(11) NOT NULL,
    CONSTRAINT fk_endereco_clinica FOREIGN KEY (clinica_id) REFERENCES clinica(id),
	CONSTRAINT fk_estado_clinica FOREIGN KEY (estado) REFERENCES estado(sigla)
);

CREATE TABLE paciente (
    id SERIAL PRIMARY KEY,
    pessoa_id INT NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    convenio_medico VARCHAR(30),
    cartao_sus VARCHAR(15),
    CONSTRAINT fk_paciente_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa(id)
);

CREATE TABLE prontuario (
    id SERIAL PRIMARY KEY,
    paciente_id INT NOT NULL,
    tipo_sanguineo VARCHAR(3) NOT NULL,
    doador_orgao BOOLEAN NOT NULL,
    diagnostico VARCHAR(500),
    historico_familiar VARCHAR(100),
    CONSTRAINT fk_prontuario_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id)
);

CREATE TABLE alergia (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    CONSTRAINT fk_alergia_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

CREATE TABLE vacina (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    CONSTRAINT fk_vacina_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

CREATE TABLE procedimento (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    CONSTRAINT fk_procedimento_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

CREATE TABLE historico_cirurgico (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    CONSTRAINT fk_historico_cirurgico_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

CREATE TABLE medicamento_diario (
    id SERIAL PRIMARY KEY,
    prontuario_id INT NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    CONSTRAINT fk_medicamento_diario_prontuario FOREIGN KEY (prontuario_id) REFERENCES prontuario(id)
);

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


CREATE TABLE especialidade (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    descricao VARCHAR(500) NOT NULL
);

CREATE TABLE medico (
    id SERIAL PRIMARY KEY,
    pessoa_id INT NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
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
