package br.fai.lds.medlink.implementation.dao.postgres.configuration;

import br.fai.lds.medlink.port.service.tools.ResourceFileService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

@Configuration
public class PostgresConnectionManagerConfiguration {

    @Value("${spring.datasource.base.url}")
    private String databaseBaseUrl;

    @Value("${spring.datasource.name}")
    private String databaseName;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Autowired
    private ResourceFileService resourceFileService;


    @Bean
    public DataSource dataSource() throws SQLException {

        final DataSource build = DataSourceBuilder
                .create()
                .url(databaseBaseUrl)
                .username(databaseUsername)
                .password(databasePassword)
                .build();

        final Connection connection = build.getConnection();

        createDataBaseIfNotExists(connection);

        return build;
    }

    private void createDataBaseIfNotExists(Connection connection) throws SQLException {

        final Statement statement = connection.createStatement();

        String sql = "SELECT COUNT(*) AS dbs ";
        sql += " FROM pg_catalog.pg_database  ";
        sql += " Where lower(datname) = '" + databaseName + "' ;";

        ResultSet resultSet = statement.executeQuery(sql);

        boolean dbExists = resultSet.next();

        if(!dbExists || resultSet.getInt("dbs") == 0){
            String createDbSql = "CREATE DATABASE " +  databaseName+  " WITH ";
            createDbSql += " OWNER = postgres ENCODING = 'UTF8' ";
            createDbSql += " CONNECTION LIMIT = -1; ";

            PreparedStatement preparedStatement = connection.prepareStatement(createDbSql);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }

    }

    @Bean
    @DependsOn("dataSource")
    public Connection getConnection() throws SQLException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(databaseUrl);
        hikariConfig.setUsername(databaseUsername);
        hikariConfig.setPassword(databasePassword);

        return new HikariDataSource(hikariConfig).getConnection();
    }
    
    @Bean
    @DependsOn("getConnection")
    public boolean createTableAndInsertData() throws SQLException, IOException {
        Connection connection = getConnection();

        final String basePath = "medlink-db-scripts";

        final String createTableSql = resourceFileService.read(basePath + "/create-tables-postgres.sql");

        PreparedStatement createStatemente = connection.prepareStatement(createTableSql);
        createStatemente.executeUpdate();
        createStatemente.close();

        final String insertDataSql  = resourceFileService.read(basePath + "/insert-data-postgres.sql");;

        final PreparedStatement insertStatement = connection.prepareStatement(insertDataSql);
        insertStatement.executeUpdate();
        insertStatement.close();

        return true;

    }

}
