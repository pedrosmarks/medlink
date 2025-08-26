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