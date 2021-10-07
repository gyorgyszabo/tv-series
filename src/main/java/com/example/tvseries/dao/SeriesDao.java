package com.example.tvseries.dao;

import com.example.tvseries.entity.Series;
import java.util.List;

public interface SeriesDao {

    void add(Series series);

    void update(Series series);

    void delete(String title);

    List<Series> filterByGenre(String genre);

    void createTable();

    void dropTable();

}
