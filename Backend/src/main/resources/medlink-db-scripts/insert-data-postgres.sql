-- Script simplificado para inserir dados essenciais no PostgreSQL
-- Execute este script após criar as tabelas

-- =====================================================
-- INSERINDO ESTADOS E CIDADES
-- =====================================================

INSERT INTO estado (id, sigla, nome) VALUES
(1, 'SP', 'São Paulo'),
(2, 'MG', 'Minas Gerais'),
(3, 'RJ', 'Rio de Janeiro');

INSERT INTO cidade (id, estado_id, nome) VALUES
(1, 1, 'São Paulo'),
(2, 2, 'Cambuí'),
(3, 3, 'Rio de Janeiro');

-- =====================================================
-- INSERINDO ENDEREÇOS
-- =====================================================

INSERT INTO endereco (id, logradouro, numero, complemento, bairro, cidade_id, cep) VALUES
(1, 'Av. Paulista', '1000', 'Sala 101', 'Bela Vista', 1, '01310100'),
(2, 'Rua Augusta', '500', '', 'Consolação', 1, '01305000'),
(3, 'Rua A', '123', '', 'Centro', 2, '12345678'),
(4, 'Rua B', '456', 'Apto 101', 'Vila Nova', 1, '01234567'),
(5, 'Rua C', '789', '', 'Centro', 3, '20000000');

-- =====================================================
-- INSERINDO PESSOAS (para médicos e pacientes)
-- =====================================================

INSERT INTO pessoa (id, nome, cpf, sexo, data_nascimento, endereco_id) VALUES
(1, 'Dr. Pedro Almeida', '11111111111', 'M', '1980-03-15', 1),
(2, 'Dr. José Silva', '22222222222', 'M', '1975-07-20', 2),
(3, 'João da Silva', '33333333333', 'M', '1994-01-01', 3),
(4, 'Maria Oliveira', '44444444444', 'F', '1979-05-15', 4),
(5, 'Carlos Pereira', '55555555555', 'M', '1972-05-15', 5);

-- =====================================================
-- INSERINDO TELEFONES
-- =====================================================

INSERT INTO telefone_pessoa (pessoa_id, numero, tipo_telefone) VALUES
(1, '11999998888', 'Celular'),
(2, '11888887777', 'Celular'),
(3, '35999998888', 'Celular'),
(4, '11999997777', 'Celular'),
(5, '21999996666', 'Celular');

-- =====================================================
-- INSERINDO ESPECIALIDADES
-- =====================================================

INSERT INTO especialidade (id, nome, descricao) VALUES
(1, 'Cardiologia', 'Especialidade médica que se dedica ao diagnóstico e tratamento das doenças do coração'),
(2, 'Neurologia', 'Especialidade médica que trata dos distúrbios estruturais do sistema nervoso'),
(3, 'Clínica Geral', 'Especialidade médica que trata de pacientes adultos em nível ambulatorial');

-- =====================================================
-- INSERINDO CLÍNICAS
-- =====================================================

INSERT INTO clinica (id, cnpj, razao_social, nome_fantasia, ativo, endereco_id) VALUES
(1, '12345678000199', 'Clínica MedLink São Paulo LTDA', 'Clínica MedLink São Paulo', true, 1),
(2, '98765432000188', 'Clínica MedLink Cambuí LTDA', 'Clínica MedLink Cambuí', true, 3);

INSERT INTO telefone_clinica (clinica_id, numero, tipo_telefone) VALUES
(1, '1133334444', 'Fixo'),
(2, '3533335555', 'Fixo');

-- =====================================================
-- INSERINDO MÉDICOS
-- =====================================================

INSERT INTO medico (id, pessoa_id, email, senha, crm, ativo) VALUES
(1, 1, 'pedro.almeida@medlink.com', '123456', '123456-SP', true),
(2, 2, 'jose.silva@medlink.com', '123', '654321-SP', true);

-- =====================================================
-- INSERINDO PACIENTES
-- =====================================================

INSERT INTO paciente (id, pessoa_id, email, senha, convenio_medico, cartao_sus, ativo) VALUES
(1, 3, 'joao@exemplo.com.br', '123', 'Particular', '123456', true),
(2, 4, 'maria@exemplo.com.br', '123', 'Convênio', '654321', true),
(3, 5, 'carlos@exemplo.com.br', '123', 'Convênio', '789123', true);

-- =====================================================
-- INSERINDO PRONTUÁRIOS
-- =====================================================

INSERT INTO prontuario (id, paciente_id, tipo_sanguineo, doador_orgao, observacoes, historico_familiar) VALUES
(1, 1, 'A+', true, 'Paciente com hipertensão arterial sistêmica', 'Hipertensão na família'),
(2, 2, 'B-', true, 'Paciente com diabetes mellitus tipo 2', 'Histórico familiar de diabetes'),
(3, 3, 'O+', true, 'Paciente com cardiopatia isquêmica crônica', 'Histórico familiar de problemas cardíacos');

-- =====================================================
-- INSERINDO RELACIONAMENTOS MÉDICO-CLÍNICA-ESPECIALIDADE
-- =====================================================

INSERT INTO medico_clinica_especialidade (id, medico_id, clinica_id, especialidade_id) VALUES
(1, 1, 1, 1),
(2, 2, 1, 2),
(3, 1, 2, 1),
(4, 2, 2, 3);

-- =====================================================
-- INSERINDO CONSULTAS
-- =====================================================

INSERT INTO consulta (prontuario_id, medico_clinica_especialidade_id, data_hora, observacao) VALUES
(1, 1, '2024-06-10 10:00:00', 'Paciente apresentou bom estado geral'),
(2, 2, '2024-05-20 14:30:00', 'Controle glicêmico adequado'),
(3, 1, '2024-04-15 09:15:00', 'Recuperação dentro do esperado');

-- =====================================================
-- INSERINDO ACESSOS A PRONTUÁRIOS
-- =====================================================

INSERT INTO medico_acesso_prontuario (medico_id, prontuario_id, data_liberacao) VALUES
(1, 1, CURRENT_TIMESTAMP),
(2, 1, CURRENT_TIMESTAMP),
(2, 2, CURRENT_TIMESTAMP),
(1, 3, CURRENT_TIMESTAMP);

-- =====================================================
-- INSERINDO USUÁRIOS PARA AUTENTICAÇÃO
-- =====================================================

INSERT INTO usuario (pessoa_id, email, senha, perfil) VALUES
(1, 'pedro.almeida@medlink.com', '123456', 'MEDICO'),
(2, 'jose.silva@medlink.com', '123', 'MEDICO'),
(3, 'joao@exemplo.com.br', '123', 'PACIENTE'),
(4, 'maria@exemplo.com.br', '123', 'PACIENTE'),
(5, 'carlos@exemplo.com.br', '123', 'PACIENTE');

-- Dados detalhados para pacientes do db.json
-- Paciente 1: João da Silva
INSERT INTO vacina (paciente_id, name, date) VALUES (1, 'COVID-19', '2023-01-15');
INSERT INTO vacina (paciente_id, name, date) VALUES (1, 'Febre amarela', '2025-08-06');
INSERT INTO medicamento (prontuario_id, name, dosage) VALUES (1, 'Losartana', '50mg');
INSERT INTO medicamento (prontuario_id, name, dosage) VALUES (1, 'Tylenol', '2 por dia');
INSERT INTO historico_cirurgico (prontuario_id, descricao_cirurgica, data_cirurgia) VALUES (1, 'Apendicectomia', '2015-08-20');
INSERT INTO diagnostico (prontuario_id, description, date) VALUES (1, 'Hipertensão', '2022-03-01');
INSERT INTO alergia (prontuario_id, name) VALUES (1, 'top');
INSERT INTO consulta (prontuario_id, medico_clinica_especialidade_id, data_hora, observacao) VALUES (1, 1, '2024-06-10', 'Consulta de rotina');

-- Paciente 2: Maria Oliveira
INSERT INTO vacina (paciente_id, name, date) VALUES (2, 'Influenza', '2023-03-10');
INSERT INTO medicamento (prontuario_id, name, dosage) VALUES (2, 'Metformina', '850mg');
INSERT INTO diagnostico (prontuario_id, description, date) VALUES (2, 'Diabetes Tipo 2', '2020-09-15');
INSERT INTO alergia (prontuario_id, name) VALUES (2, 'Penicilina');
INSERT INTO consulta (prontuario_id, medico_clinica_especialidade_id, data_hora, observacao) VALUES (2, 2, '2024-05-20', 'Avaliação de rotina');

-- Paciente 3: Carlos Pereira
INSERT INTO vacina (paciente_id, name, date) VALUES (3, 'Hepatite B', '2022-11-05');
INSERT INTO medicamento (prontuario_id, name, dosage) VALUES (3, 'AAS', '100mg');
INSERT INTO historico_cirurgico (prontuario_id, descricao_cirurgica, data_cirurgia) VALUES (3, 'Revascularização do miocárdio', '2023-12-01');
INSERT INTO diagnostico (prontuario_id, description, date) VALUES (3, 'Cardiopatia isquêmica', '2023-10-20');
INSERT INTO consulta (prontuario_id, medico_clinica_especialidade_id, data_hora, observacao) VALUES (3, 1, '2024-04-15', 'Pós-operatório');

-- =====================================================
-- INSERINDO SOLICITAÇÕES DE ACESSO AO PRONTUÁRIO
-- =====================================================

-- Solicitações para demonstrar o fluxo de aprovação
INSERT INTO solicitacao_acesso_prontuario (medico_id, paciente_id, status) VALUES
(1, 2, 'ACEITA'),    -- Dr. Pedro tem acesso à Maria (aprovado)
(1, 3, 'ACEITA'),    -- Dr. Pedro tem acesso ao Carlos (aprovado)
(2, 1, 'PENDENTE'),  -- Dr. José solicitou acesso ao João (pendente)
(1, 1, 'RECUSADA');  -- Dr. Pedro solicitou acesso ao João (rejeitado)

-- =====================================================
-- MENSAGENS DE EXEMPLO ENTRE MÉDICO E PACIENTE (ID 1)
-- =====================================================
INSERT INTO mensagem (sender_id, sender_type, sender_name, recipient_id, recipient_type, recipient_name, text, date, read) VALUES
(1, 'MEDIC', 'Dr. Pedro Almeida', 1, 'PATIENT', 'João da Silva', 'Olá, tudo bem? Aqui é o Dr. Pedro.', NOW(), false),
(1, 'PATIENT', 'João da Silva', 1, 'MEDIC', 'Dr. Pedro Almeida', 'Olá doutor, estou bem sim. Obrigado!', NOW(), false),
(1, 'MEDIC', 'Dr. Pedro Almeida', 1, 'PATIENT', 'João da Silva', 'Ótimo! Se precisar de algo, me avise.', NOW(), false);

COMMIT;
