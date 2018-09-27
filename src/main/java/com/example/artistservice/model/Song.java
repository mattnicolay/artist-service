package com.example.artistservice.model;

import java.util.Objects;

public class Song {
  private long id;
  private String name;
  private String year;
  private String duration;

  public Song(long id, String name, String year, String duration) {
    this.id = id;
    this.name = name;
    this.year = year;
    this.duration = duration;
  }

  public Song() {}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Song song = (Song) o;
    return id == song.id &&
        Objects.equals(name, song.name) &&
        Objects.equals(year, song.year) &&
        Objects.equals(duration, song.duration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, year, duration);
  }

  @Override
  public String toString() {
    return "Song{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", year='" + year + '\'' +
        ", duration='" + duration + '\'' +
        '}';
  }
}
