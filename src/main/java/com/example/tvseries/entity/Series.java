package com.example.tvseries.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Series {

    private final String title;
    private final int numberOfSeasons;
    private final String genre;

}
