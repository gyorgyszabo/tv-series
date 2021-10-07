package com.example.tvseries.dao;

import com.example.tvseries.entity.Series;
import lombok.AllArgsConstructor;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class SeriesDaoJdbc implements SeriesDao {

    private final DataSource dataSource;

    public void add(Series series) {
        String sql = "INSERT INTO series (title, number_of_seasons, genre) VALUES (?, ?, ?);";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, series.getTitle());
            preparedStatement.setInt(2, series.getNumberOfSeasons());
            preparedStatement.setString(3, series.getGenre());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Series series) {
        String sql = "UPDATE series SET number_of_seasons = ?, genre = ? WHERE title = ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, series.getNumberOfSeasons());
            preparedStatement.setString(2, series.getGenre());
            preparedStatement.setString(3, series.getTitle());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String title) {
        String sql = "DELETE FROM series WHERE title = ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Series> filterByGenre(String genre) {
        List<Series> seriesList = new ArrayList<>();
        String sql = "SELECT * FROM series WHERE genre = ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, genre);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Series series = new Series(resultSet.getString("title"),
                        resultSet.getInt("number_of_seasons"), resultSet.getString("genre"));
                seriesList.add(series);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seriesList;
    }

    public void createTable() {
        String sql = "CREATE TABLE series (title varchar PRIMARY KEY, number_of_seasons integer, genre varchar);";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
           statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropTable() {
        String sql = "DROP TABLE IF EXISTS series;";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}