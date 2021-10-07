package com.example.tvseries.dao;

import com.example.tvseries.entity.Series;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SeriesDaoTest {

    private static SeriesDao seriesDao;
    private static Connection connection;
    private static Statement statement;

    @BeforeAll
    static void setup() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = System.getenv("DB_NAME");
        String user = System.getenv("USER");
        String password = System.getenv("PASSWORD");
        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        seriesDao = new SeriesDaoJdbc(dataSource);
        connection = dataSource.getConnection();
        statement = connection.createStatement();
    }

    @BeforeEach
    void init() throws SQLException {
        statement.execute("DROP TABLE IF EXISTS series;");
        statement.execute("CREATE TABLE series (title varchar PRIMARY KEY, number_of_seasons integer, genre varchar);");
    }

    @AfterAll
    static void shutDown() throws SQLException {
        statement.close();
        connection.close();
    }

    @Test
    void add_ShouldAddSeries() throws SQLException {
        Series series = new Series("Big bang theory", 12, "Comedy");
        seriesDao.add(series);
        ResultSet resultSet = statement.executeQuery("SELECT * FROM series;");
        Series returnedSeries = convertResultSetToSeriesList(resultSet).get(0);
        assertEquals("Big bang theory", returnedSeries.getTitle());
    }

    @Test
    void update_ShouldUpdateSeries() throws SQLException {
        addTestDataToDataBase();
        Series series = new Series("Dr. Who", 8, "Thriller");
        seriesDao.update(series);
        ResultSet resultSet = statement.executeQuery("SELECT * FROM series WHERE title = 'Dr. Who';");
        Series returnedSeries = convertResultSetToSeriesList(resultSet).get(0);
        assertEquals(8, returnedSeries.getNumberOfSeasons());
        assertEquals("Thriller", returnedSeries.getGenre());
    }

    @Test
    void delete_ShouldDeleteSeries() throws SQLException {
        addTestDataToDataBase();
        seriesDao.delete("Friends");
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS row_count FROM series;");
        resultSet.next();
        assertEquals(2, resultSet.getInt("row_count"));
    }

    @Test
    void filterByGenre_ShouldReturnSeriesWithSpecifiedGenreOnly() throws SQLException {
        addTestDataToDataBase();
        Series returnedSeries = seriesDao.filterByGenre("Crime").get(0);
        assertEquals("Columbo", returnedSeries.getTitle());
    }

    @Test
    void createTable_ShouldCreateTable() throws SQLException {
        statement.execute("DROP TABLE IF EXISTS series;");
        seriesDao.createTable();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS row_count FROM pg_tables WHERE tablename = 'series';");
        resultSet.next();
        assertEquals(1, resultSet.getInt("row_count"));
    }

    @Test
    void dropTable_ShouldDropTable() throws SQLException {
        seriesDao.dropTable();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS row_count FROM pg_tables WHERE tablename = 'series';");
        resultSet.next();
        assertEquals(0, resultSet.getInt("row_count"));
    }

    private void addTestDataToDataBase() throws SQLException {
        statement.execute("INSERT INTO series (title, number_of_seasons, genre) VALUES ('Dr. Who', 6, 'Sci-fi');");
        statement.execute("INSERT INTO series (title, number_of_seasons, genre) VALUES ('Friends', 10, 'Comedy');");
        statement.execute("INSERT INTO series (title, number_of_seasons, genre) VALUES ('Columbo', 10, 'Crime');");
    }

    private List<Series> convertResultSetToSeriesList(ResultSet resultSet) throws SQLException {
        List<Series> seriesList = new ArrayList<>();
        while (resultSet.next()) {
            Series series = new Series(resultSet.getString("title"),
                    resultSet.getInt("number_of_seasons"), resultSet.getString("genre"));
            seriesList.add(series);
        }
        return seriesList;
    }

}
