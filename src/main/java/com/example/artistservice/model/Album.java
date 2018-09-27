package com.example.artistservice.model;

import java.util.Objects;

public class Album {
  private long id;
  private String name;
  private String year;

  public Album(long id, String name, String year) {
    this.id = id;
    this.name = name;
    this.year = year;
  }

  public Album() {}

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Album album = (Album) o;
    return id == album.id &&
        Objects.equals(name, album.name) &&
        Objects.equals(year, album.year);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, year);
  }

  @Override
  public String toString() {
    return "Album{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", year='" + year + '\'' +
        '}';
  }
}
