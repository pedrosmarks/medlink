-- Dados detalhados para pacientes do db.json
-- Paciente 1: João da Silva
INSERT INTO vacina (prontuario_id, descricao_vacina, data) VALUES (1, 'COVID-19', '2023-01-15');
INSERT INTO vacina (prontuario_id, descricao_vacina, data) VALUES (1, 'Febre amarela', '2025-08-06');
INSERT INTO medicamento_diario (prontuario_id, descricao_medicamento) VALUES (1, 'Losartana 50mg');
INSERT INTO medicamento_diario (prontuario_id, descricao_medicamento) VALUES (1, 'Tylenol 2 por dia');
INSERT INTO historico_cirurgico (prontuario_id, descricao_cirurgica, data_cirurgia) VALUES (1, 'Apendicectomia', '2015-08-20');
INSERT INTO diagnostico (prontuario_id, descricao_diagnostico, data_diagnostico) VALUES (1, 'Hipertensão', '2022-03-01');
INSERT INTO alergia (prontuario_id, descricao_alergia) VALUES (1, 'top');
INSERT INTO consulta (prontuario_id, data_hora, observacao) VALUES (1, '2024-06-10', 'Consulta de rotina');

-- Paciente 2: Maria Oliveira
INSERT INTO vacina (prontuario_id, descricao_vacina, data) VALUES (2, 'Influenza', '2023-03-10');
INSERT INTO medicamento_diario (prontuario_id, descricao_medicamento) VALUES (2, 'Metformina 850mg');
INSERT INTO diagnostico (prontuario_id, descricao_diagnostico, data_diagnostico) VALUES (2, 'Diabetes Tipo 2', '2020-09-15');
INSERT INTO alergia (prontuario_id, descricao_alergia) VALUES (2, 'Penicilina');
INSERT INTO consulta (prontuario_id, data_hora, observacao) VALUES (2, '2024-05-20', 'Avaliação de rotina');

-- Paciente 3: Carlos Pereira
INSERT INTO vacina (prontuario_id, descricao_vacina, data) VALUES (3, 'Hepatite B', '2022-11-05');
INSERT INTO medicamento_diario (prontuario_id, descricao_medicamento) VALUES (3, 'AAS 100mg');
INSERT INTO historico_cirurgico (prontuario_id, descricao_cirurgica, data_cirurgia) VALUES (3, 'Revascularização do miocárdio', '2023-12-01');
INSERT INTO diagnostico (prontuario_id, descricao_diagnostico, data_diagnostico) VALUES (3, 'Cardiopatia isquêmica', '2023-10-20');
INSERT INTO consulta (prontuario_id, data_hora, observacao) VALUES (3, '2024-04-15', 'Pós-operatório');
