package com.example.artistservice.service;

import com.example.artistservice.dao.AlbumClient;
import com.example.artistservice.dao.ArtistRepository;
import com.example.artistservice.dao.SongClient;
import com.example.artistservice.model.Artist;
import com.example.artistservice.model.ArtistDisplay;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ArtistService {

  private ArtistRepository artistRepository;
  private AlbumClient albumClient;
  private SongClient songClient;
  @Autowired
  private RestTemplate restTemplate;

  public ArtistService(ArtistRepository artistRepository,
      AlbumClient albumClient, SongClient songClient) {
    this.artistRepository = artistRepository;
    this.albumClient = albumClient;
    this.songClient = songClient;
  }

  public List<ArtistDisplay> getAllArtists() {
    return artistRepository.findAll()
        .stream()
        .map(artist ->
          new ArtistDisplay(
              artist.getId(),
              artist.getName(),
              albumClient.getAlbumsByArtistId(artist.getId()),
              songClient.getSongsByArtistId(artist.getId())))
        .collect(Collectors.toList());
  }

  public ArtistDisplay getArtistById(long id) {
    Artist artist = artistRepository.findById(id);
    return artist == null ? null
        : new ArtistDisplay(
        artist.getId(),
        artist.getName(),
        albumClient.getAlbumsByArtistId(artist.getId()),
        songClient.getSongsByArtistId(artist.getId()));
  }

  public ArtistDisplay getArtistByName(String name) {
    Artist artist = artistRepository.findByName(name);
    return artist == null ? null
        : new ArtistDisplay(
            artist.getId(),
            artist.getName(),
            albumClient.getAlbumsByArtistId(artist.getId()),
            songClient.getSongsByArtistId(artist.getId()));
  }

  public Artist createArtist(Artist newArtist) {
    return newArtist == null ? null : artistRepository.save(newArtist);
  }

  public Artist updateArtist(long id, Artist updatedArtist) {
    if (updatedArtist != null && artistRepository.findById(id) != null) {
      updatedArtist.setId(id);
      return artistRepository.save(updatedArtist);
    }
    return null;
  }

  public Artist deleteArtist(long id) {
    Artist artist = artistRepository.findById(id);
    if (artist != null) {
      artistRepository.delete(artist);
    }
    return artist;
  }

}
