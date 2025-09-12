-- Verificar usuários cadastrados
SELECT 'MEDICOS' as tipo;
SELECT m.id, p.nome, m.crm, m.especialidade 
FROM medico m 
JOIN pessoa p ON m.pessoa_id = p.id;

SELECT 'PACIENTES' as tipo;
SELECT pac.id, p.nome, pac.email 
FROM paciente pac 
JOIN pessoa p ON pac.pessoa_id = p.id 
WHERE pac.ativo = true;

-- Verificar solicitações de acesso
SELECT 'SOLICITACOES_ACESSO' as tipo;
SELECT sap.medico_id, sap.paciente_id, sap.status, sap.data_solicitacao, sap.data_resposta,
       m.nome as medico_nome, p.nome as paciente_nome
FROM solicitacao_acesso_prontuario sap
JOIN medico med ON sap.medico_id = med.id
JOIN pessoa m ON med.pessoa_id = m.id
JOIN paciente pac ON sap.paciente_id = pac.id
JOIN pessoa p ON pac.pessoa_id = p.id
ORDER BY sap.data_solicitacao DESC;

-- Verificar acessos diretos
SELECT 'ACESSOS_DIRETOS' as tipo;
SELECT map.medico_id, map.prontuario_id, pr.paciente_id,
       m.nome as medico_nome, p.nome as paciente_nome
FROM medico_acesso_prontuario map
JOIN prontuario pr ON map.prontuario_id = pr.id
JOIN medico med ON map.medico_id = med.id
JOIN pessoa m ON med.pessoa_id = m.id
JOIN paciente pac ON pr.paciente_id = pac.id
JOIN pessoa p ON pac.pessoa_id = p.id;