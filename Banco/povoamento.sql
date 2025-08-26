INSERT INTO estado (sigla, nome) VALUES
('SP', 'São Paulo'),
('RJ', 'Rio de Janeiro'),
('MG', 'Minas Gerais'),
('BA', 'Bahia'),
('RS', 'Rio Grande do Sul');

INSERT INTO cidade (estado_id, nome) VALUES
(1, 'São Paulo'),
(1, 'Campinas'),
(2, 'Rio de Janeiro'),
(3, 'Belo Horizonte'),
(4, 'Salvador');

INSERT INTO pessoa (nome, cpf, sexo, data_nascimento) VALUES
('Ana Silva', '12345678901', 'F', '1990-05-10'),
('João Pereira', '12345678902', 'M', '1985-03-20'),
('Maria Oliveira', '12345678903', 'F', '2000-08-15'),
('Carlos Souza', '12345678904', 'M', '1978-11-25'),
('Beatriz Lima', '12345678905', 'F', '1995-01-05');

INSERT INTO endereco_pessoa (pessoa_id, logradouro, numero, complemento, bairro, cidade_id, cep) VALUES
(1, 'Rua A', '100', 'Apto 1', 'Centro', 1, '01001000'),
(2, 'Av B', '200', NULL, 'Bairro B', 2, '13000000'),
(3, 'Rua C', '300', 'Casa', 'Bairro C', 3, '20000000'),
(4, 'Av D', '400', NULL, 'Bairro D', 4, '30000000'),
(5, 'Rua E', '500', 'Bloco 5', 'Bairro E', 5, '40000000');

INSERT INTO telefone_pessoa (pessoa_id, numero, tipo_telefone) VALUES
(1, '11999990001', 'Celular'),
(2, '21988880002', 'Celular'),
(3, '31977770003', 'Residencial'),
(4, '71966660004', 'Comercial'),
(5, '51955550005', 'Celular');

INSERT INTO clinica (cnpj, razao_social, nome_fantasia) VALUES
('11111111000101', 'Clínica Vida Saúde LTDA', 'Vida Saúde'),
('22222222000102', 'Clínica Bem Estar LTDA', 'Bem Estar'),
('33333333000103', 'Hospital São Lucas SA', 'São Lucas'),
('44444444000104', 'Centro Médico Esperança LTDA', 'Esperança'),
('55555555000105', 'Clínica Popular LTDA', 'Clínica Popular');

INSERT INTO endereco_clinica (clinica_id, logradouro, numero, complemento, bairro, cidade_id, cep) VALUES
(1, 'Rua Saúde', '10', NULL, 'Centro', 1, '01002000'),
(2, 'Av Paz', '20', 'Sala 5', 'Jardins', 2, '13001000'),
(3, 'Rua Esperança', '30', NULL, 'Copacabana', 3, '20001000'),
(4, 'Av Cura', '40', NULL, 'Savassi', 4, '30001000'),
(5, 'Rua Popular', '50', 'Loja 2', 'Barra', 5, '40001000');

INSERT INTO telefone_clinica (clinica_id, numero, tipo_telefone) VALUES
(1, '1133330001', 'Fixo'),
(2, '2133330002', 'Fixo'),
(3, '3133330003', 'Fixo'),
(4, '7133330004', 'Fixo'),
(5, '5133330005', 'Fixo');

INSERT INTO paciente (pessoa_id, email, convenio_medico, cartao_sus) VALUES
(1, 'ana@teste.com', 'Unimed', '123456789012345'),
(2, 'joao@teste.com', 'Amil', '223456789012345'),
(3, 'maria@teste.com', NULL, '323456789012345'),
(4, 'carlos@teste.com', 'Sulamérica', '423456789012345'),
(5, 'bia@teste.com', 'Bradesco Saúde', '523456789012345');

INSERT INTO prontuario (paciente_id, tipo_sanguineo, doador_orgao, diagnostico, historico_familiar) VALUES
(1, 'A+', TRUE, 'Hipertensão', 'Pai hipertenso'),
(2, 'O-', FALSE, 'Diabetes tipo 2', 'Avó diabética'),
(3, 'B+', TRUE, 'Asma leve', 'Mãe asmática'),
(4, 'AB-', TRUE, 'Colesterol alto', 'Histórico cardíaco'),
(5, 'O+', FALSE, 'Enxaqueca', 'Sem histórico relevante');

INSERT INTO alergia (prontuario_id, descricao_alergia) VALUES
(1, 'Alergia a dipirona'),
(2, 'Alergia a penicilina'),
(3, 'Alergia a poeira'),
(4, 'Alergia a frutos do mar'),
(5, 'Alergia a pólen');

INSERT INTO vacina (prontuario_id, descricao_vacina) VALUES
(1, 'Covid-19'),
(2, 'Influenza'),
(3, 'Hepatite B'),
(4, 'Febre Amarela'),
(5, 'Tétano');

INSERT INTO procedimento (prontuario_id, descricao_procedimento) VALUES
(1, 'Exame de sangue anual'),
(2, 'Ressonância magnética'),
(3, 'Raio-x torácico'),
(4, 'Eletrocardiograma'),
(5, 'Ultrassonografia');