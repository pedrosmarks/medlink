# MedLink - Migração para PostgreSQL

## Resumo das Alterações

O projeto foi migrado dos DAOs "fake" (dados simulados) para uma implementação real com PostgreSQL, seguindo o padrão da pasta `base`.

### Arquivos Modificados/Criados:

#### 1. Dependências (pom.xml)
- Adicionada dependência `spring-boot-starter-jdbc`
- Especificada versão do PostgreSQL driver (42.6.0)
- Adicionada dependência do HikariCP para pool de conexões

#### 2. Configuração de Banco (application.properties)
```properties
spring.datasource.name=db_medlink
spring.datasource.base.url=jdbc:postgresql://localhost:5432/
spring.datasource.url=jdbc:postgresql://localhost:5432/${spring.datasource.name}
spring.datasource.username=postgres
spring.datasource.password=123456
```

#### 3. Scripts SQL
- **create-tables-postgres.sql**: Baseado no seu arquivo PID_Banco_Dados.sql, criando todas as tabelas do MedLink
- **insert-data-postgres.sql**: Dados de teste baseados no seu povoamento.sql

#### 4. Implementações PostgreSQL DAO
- **PatientPostgresDaoImpl**: Implementa todas as operações CRUD para pacientes
- **MedicPostgresDaoImpl**: Implementa todas as operações CRUD para médicos  
- **MedicalRecordPostgresDaoImpl**: Implementa todas as operações CRUD para prontuários

#### 5. Configuração de Conexão
- **PostgresConnectionManagerConfiguration**: Gerencia a conexão com PostgreSQL, cria o banco se não existir, e executa os scripts SQL automaticamente
- **ResourceFileService**: Serviço para leitura dos arquivos SQL de recursos

#### 6. AppConfiguration
- Atualizada para usar os DAOs PostgreSQL ao invés dos "fake"
- DAOs fake mantidos comentados para fácil rollback se necessário

### Estrutura do Banco de Dados

O banco de dados implementa o modelo normalizado com as seguintes tabelas principais:
- **pessoa**: Dados básicos de pessoas (pacientes e médicos)
- **paciente/medico**: Dados específicos de cada tipo de usuário
- **prontuario**: Informações médicas dos pacientes
- **clinica**: Informações das clínicas
- **especialidade**: Especialidades médicas
- **consulta**: Consultas realizadas
- **mensagem**: Sistema de mensagens entre médicos e pacientes

### Como Executar

1. **Pré-requisitos:**
   - PostgreSQL instalado e rodando na porta 5432
   - Usuário: postgres, Senha: 123456 (ou altere no application.properties)

2. **Execução:**
   - Execute o Spring Boot normalmente
   - O sistema criará automaticamente o banco `db_medlink` se não existir
   - As tabelas serão criadas e populadas automaticamente na primeira execução

3. **Rollback para DAOs Fake (se necessário):**
   - No `AppConfiguration.java`, comente as linhas dos DAOs PostgreSQL
   - Descomente as linhas dos DAOs Fake
   - Remova a dependência `Connection` dos métodos @Bean

### Benefícios da Migração

- **Persistência Real**: Dados não são perdidos entre reinicializações
- **Consultas Complexas**: Possibilidade de fazer joins e consultas avançadas
- **Escalabilidade**: Banco preparado para crescimento dos dados
- **Integridade**: Constraints e relacionamentos garantem consistência
- **Backup/Restore**: Facilidade para backup e restauração dos dados

### Notas Técnicas

- Os DAOs PostgreSQL implementam todas as interfaces existentes
- Transações são utilizadas para operações que envolvem múltiplas tabelas
- Pool de conexões HikariCP para melhor performance
- Logs detalhados para debug e monitoramento
- Mapeamento adequado entre objetos Java e estrutura relacional

### Próximos Passos

Com essa base implementada, você pode:
1. Adicionar mais funcionalidades específicas de cada DAO
2. Implementar consultas mais complexas
3. Adicionar cache para melhor performance
4. Implementar auditoria e logs de acesso
5. Adicionar validações específicas de negócio

A estrutura está preparada para suportar todas as funcionalidades do MedLink de forma robusta e escalável.
