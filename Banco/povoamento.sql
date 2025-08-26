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