package com.example.artistservice.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.example.artistservice.dao.AlbumClient;
import com.example.artistservice.dao.ArtistRepository;
import com.example.artistservice.dao.SongClient;
import com.example.artistservice.model.Album;
import com.example.artistservice.model.Artist;
import com.example.artistservice.model.ArtistDisplay;
import com.example.artistservice.model.Song;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ArtistServiceUnitTests {

  @MockBean
  private ArtistRepository artistRepository;
  @MockBean
  private AlbumClient albumClient;
  @MockBean
  private SongClient songClient;
  private ArtistService artistService;

  @Before
  public void setup() {
    artistService = new ArtistService(artistRepository, albumClient, songClient);
    when(albumClient.getAlbumsByArtistId(anyLong()))
        .thenReturn(Arrays.asList(getAlbum1()));
    when(songClient.getSongsByArtistId(anyLong()))
        .thenReturn(Arrays.asList(getSong1()));
    when(artistRepository.findByName("Radiohead")).thenReturn(getArtist1());
    when(artistRepository.findById(1L)).thenReturn(getArtist1());
  }

  @Test
  public void getAllArtists_DatabaseNotEmpty_ReturnsListOfArtists() {
    when(artistRepository.findAll()).thenReturn(getArtists());
    when(albumClient.getAlbumsByArtistId(2L))
        .thenReturn(Arrays.asList(getAlbum2()));
    when(songClient.getSongsByArtistId(2L))
        .thenReturn(Arrays.asList(getSong2()));
    List<ArtistDisplay> artistDisplays = artistService.getAllArtists();

    assertThat(artistDisplays, is(notNullValue()));
    assertFalse(artistDisplays.isEmpty());

    artistDisplays.forEach(artistDisplay -> {
      assertThat(artistDisplay.getId(), is(notNullValue()));
      assertThat(artistDisplay.getName(), is(notNullValue()));
      assertThat(artistDisplay.getAlbums(), is(notNullValue()));
      assertThat(artistDisplay.getAlbums().isEmpty(), is(false));
      assertThat(artistDisplay.getSongs(), is(notNullValue()));
      assertThat(artistDisplay.getSongs().isEmpty(), is(false));
    });
    List<ArtistDisplay> test = getArtistDisplays();
    test.forEach(artist -> assertTrue(artistDisplays.contains(artist)));
  }

  @Test
  public void getAllArtists_DatabaseEmpty_ReturnsEmptyList() {
    when(artistRepository.findAll()).thenReturn(new ArrayList<>());
    List<ArtistDisplay> artistDisplays = artistService.getAllArtists();

    assertThat(artistDisplays, is(notNullValue()));
    assertThat(artistDisplays.isEmpty(), is(true));
  }

  @Test
  public void getAllArtists_AlbumServiceIsDown_ReturnsEmptyListOfAlbums() {
    when(artistRepository.findAll()).thenReturn(getArtists());
    when(albumClient.getAlbumsByArtistId(anyLong()))
        .thenReturn(new ArrayList<>());
    List<ArtistDisplay> artistDisplays = artistService.getAllArtists();

    assertThat(artistDisplays, is(notNullValue()));
    assertThat(artistDisplays.isEmpty(), is(false));

    artistDisplays.forEach(artistDisplay -> {
      assertThat(artistDisplay.getId(), is(notNullValue()));
      assertThat(artistDisplay.getName(), is(notNullValue()));
      assertThat(artistDisplay.getAlbums(), is(notNullValue()));
      assertThat(artistDisplay.getAlbums().isEmpty(), is(true));
      assertThat(artistDisplay.getSongs(), is(notNullValue()));
      assertThat(artistDisplay.getSongs().isEmpty(), is(false));
    });
  }

  @Test
  public void getAllArtists_SongServiceIsDown_ReturnsEmptyListOfSongs() {
    when(artistRepository.findAll()).thenReturn(getArtists());
    when(songClient.getSongsByArtistId(anyLong()))
        .thenReturn(new ArrayList<>());
    List<ArtistDisplay> artistDisplays = artistService.getAllArtists();

    assertThat(artistDisplays, is(notNullValue()));
    assertThat(artistDisplays.isEmpty(), is(false));

    artistDisplays.forEach(artistDisplay -> {
      assertThat(artistDisplay.getId(), is(notNullValue()));
      assertThat(artistDisplay.getName(), is(notNullValue()));
      assertThat(artistDisplay.getAlbums(), is(notNullValue()));
      assertThat(artistDisplay.getAlbums().isEmpty(), is(false));
      assertThat(artistDisplay.getSongs(), is(notNullValue()));
      assertThat(artistDisplay.getSongs().isEmpty(), is(true));
    });
  }

  @Test
  public void getArtistById_ValidId_ReturnsArtist() {
    ArtistDisplay artist = artistService.getArtistById(1L);

    assertThat(artist, is(notNullValue()));
    assertThat(artist, is(equalTo(getArtistDisplay1())));
  }

  @Test
  public void getArtistById_InvalidId_ReturnsNull() {
    assertThat(artistService.getArtistById(-1), is(nullValue()));
  }

  @Test
  public void getArtistById_AlbumServiceIsDown_AlbumListIsEmpty() {
    when(albumClient.getAlbumsByArtistId(anyLong()))
        .thenReturn(new ArrayList<>());
    ArtistDisplay artist = artistService.getArtistById(1L);

    assertThat(artist, is(notNullValue()));
    assertThat(artist.getAlbums().isEmpty(), is(true));

  }

  @Test
  public void getArtistById_SongServiceIsDown_SongListIsEmpty() {
    when(songClient.getSongsByArtistId(anyLong()))
        .thenReturn(new ArrayList<>());
    ArtistDisplay artist = artistService.getArtistById(1L);

    assertThat(artist, is(notNullValue()));
    assertThat(artist.getSongs().isEmpty(), is(true));

  }

  @Test
  public void getArtistByName_ValidName_ReturnsArtist() {
    ArtistDisplay artist = artistService.getArtistByName("Radiohead");

    assertThat(artist, is(notNullValue()));
    assertThat(artist, is(equalTo(getArtistDisplay1())));
  }

  @Test
  public void getArtistByName_InvalidName_ReturnsNull() {
    assertThat(artistService.getArtistByName("Lena Raine"), is(nullValue()));
  }

  @Test
  public void getArtistByName_AlbumServiceIsDown_AlbumListIsEmpty() {
    when(albumClient.getAlbumsByArtistId(anyLong()))
        .thenReturn(new ArrayList<>());
    ArtistDisplay artist = artistService.getArtistByName("Radiohead");

    assertThat(artist, is(notNullValue()));
    assertThat(artist.getAlbums().isEmpty(), is(true));
  }

  @Test
  public void getArtistByName_SongServiceIsDown_SongListIsEmpty() {
    when(songClient.getSongsByArtistId(anyLong()))
        .thenReturn(new ArrayList<>());
    ArtistDisplay artist = artistService.getArtistByName("Radiohead");

    assertThat(artist, is(notNullValue()));
    assertThat(artist.getSongs().isEmpty(), is(true));
  }

  @Test
  public void createArtist_CreationSuccessful_ReturnsCreatedArtist() {
    Artist newArtist = new Artist("Queen");
    when(artistRepository.save(newArtist)).thenReturn(newArtist);
    Artist artist = artistService.createArtist(newArtist);
    assertThat(artist, is(notNullValue()));
    assertThat(artist, is(equalTo(newArtist)));
  }

  @Test
  public void updateArtist_ValidId_ReturnsUpdatedArtist() {
    Artist updatedArtist = new Artist("Ryo");
    updatedArtist.setId(2L);
    when(artistRepository.findById(anyLong())).thenReturn(updatedArtist);
    when(artistRepository.save(updatedArtist)).thenReturn(updatedArtist);
    Artist artist = artistService.updateArtist(2, updatedArtist);
    assertThat(artist, is(notNullValue()));
    assertThat(artist, is(equalTo(updatedArtist)));
  }

  @Test
  public void updateArtist_InvalidId_ReturnsNull() {
    assertThat(artistService.updateArtist(-1, new Artist("Ryo")), is(nullValue()));
  }

  @Test
  public void deleteArtist_ValidId_ReturnsDeletedArtist() {
    Artist artist = artistService.deleteArtist(1);
    assertThat(artist, is(notNullValue()));
    assertThat(artist, is(equalTo(getArtist1())));
  }

  @Test
  public void deleteArtist_InvalidId_ReturnsNull() {
    assertThat(artistService.deleteArtist(-1), is(nullValue()));
  }

  private List<ArtistDisplay> getArtistDisplays() {
    return Arrays.asList(getArtistDisplay1(), getArtistDisplay2());
  }

  private List<Artist> getArtists() {
    return Arrays.asList(getArtist1(), getArtist2());
  }

  private Artist getArtist1() {
    Artist artist = new Artist("Radiohead");
    artist.setId(1L);
    return artist;
  }

  private Artist getArtist2() {
    Artist artist = new Artist("Ryo Fukui");
    artist.setId(2L);
    return artist;
  }

  private ArtistDisplay getArtistDisplay1() {
    return new ArtistDisplay(
        1L,
        "Radiohead",
        Arrays.asList(getAlbum1()),
        Arrays.asList(getSong1()));
  }

  private ArtistDisplay getArtistDisplay2() {
    return new ArtistDisplay(
        2L,
        "Ryo Fukui",
        Arrays.asList(getAlbum2()),
        Arrays.asList(getSong2()));
  }

  private Album getAlbum1() {
    return new Album(1L, "A Moon Shaped Pool", "2017");
  }

  private Album getAlbum2() {
    return new Album(2L, "Scenery", "1977");
  }

  private Song getSong1() {
    return new Song(1L, "Daydreaming", "2017", "6:47");
  }

  private Song getSong2() {
    return new Song(2L, "It Could Happen to You", "1977", "4:13");
  }

}