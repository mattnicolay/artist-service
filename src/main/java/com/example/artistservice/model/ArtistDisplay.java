package com.example.artistservice.model;

import java.util.List;
import java.util.Objects;

public class ArtistDisplay {
  private long id;
  private String name;
  private List<Album> albums;
  private List<Song> songs;

  public ArtistDisplay(long id, String name,
      List<Album> albums, List<Song> songs) {
    this.id = id;
    this.name = name;
    this.albums = albums;
    this.songs = songs;
  }

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

  public List<Album> getAlbums() {
    return albums;
  }

  public void setAlbums(List<Album> albums) {
    this.albums = albums;
  }

  public List<Song> getSongs() {
    return songs;
  }

  public void setSongs(List<Song> songs) {
    this.songs = songs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArtistDisplay that = (ArtistDisplay) o;
    return id == that.id &&
        Objects.equals(name, that.name) &&
        (albums.size() == that.albums.size() && albums.containsAll(that.albums)) &&
        (songs.size() == that.songs.size() && songs.containsAll(that.songs));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, albums, songs);
  }

  @Override
  public String toString() {
    return "ArtistDisplay{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", albums=" + albums +
        ", songs=" + songs +
        '}';
  }
}
