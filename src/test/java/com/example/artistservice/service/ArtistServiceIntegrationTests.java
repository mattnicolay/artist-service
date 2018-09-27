package com.example.artistservice.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import com.example.artistservice.dao.AlbumClient;
import com.example.artistservice.dao.ArtistRepository;
import com.example.artistservice.dao.SongClient;
import com.example.artistservice.model.Album;
import com.example.artistservice.model.Artist;
import com.example.artistservice.model.ArtistDisplay;
import com.example.artistservice.model.Song;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@DatabaseSetup("classpath:test-dataset.xml")
@AutoConfigureStubRunner(
    ids = {"com.example:album-service:+:stubs:8100",
    "com.example:song-service:+:stubs:8200"},
    stubsMode = StubsMode.LOCAL)
@TestPropertySource("classpath:test-application.yml")
public class ArtistServiceIntegrationTests {

  @Autowired
  private ArtistRepository artistRepository;
  @Autowired
  private AlbumClient albumClient;
  @Autowired
  private SongClient songClient;
  @Autowired
  private ArtistService artistService;

  @Test
  public void getAllArtists_NonEmptyDatabase_ReturnsListOfArtists() {
    List<ArtistDisplay> artistDisplays = artistService.getAllArtists();
    List<Artist> dbArtists = artistRepository.findAll();

    assertThat(artistDisplays, is(notNullValue()));
    assertFalse(artistDisplays.isEmpty());

    List<Artist> artists = new ArrayList<>();
    artistDisplays.forEach(artistDisplay -> {
      assertThat(artistDisplay.getId(), is(notNullValue()));
      assertThat(artistDisplay.getName(), is(notNullValue()));
      assertThat(artistDisplay.getAlbums(), is(notNullValue()));
      assertThat(artistDisplay.getAlbums().isEmpty(), is(false));
      assertThat(artistDisplay.getSongs(), is(notNullValue()));
      assertThat(artistDisplay.getSongs().isEmpty(), is(false));

      artists.add(new Artist(artistDisplay.getName()));
    });
    dbArtists.forEach(artist -> assertThat(artists.contains(artist), is(true)));
  }


  @Test
  public void getAllArtists_EmptyDatabase_ReturnsEmptyListOfArtists() {
    artistRepository.deleteAll();
    List<ArtistDisplay> artistDisplays = artistService.getAllArtists();

    assertThat(artistDisplays, is(notNullValue()));
    assertThat(artistDisplays.isEmpty(), is(true));
  }

  @Test
  public void getArtistById_ValidId_ReturnsArtist() {
    ArtistDisplay artistDisplay = artistService.getArtistById(1L);
    Artist dbArtist = artistRepository.findById(1L);

    assertThat(artistDisplay, is(notNullValue()));
    assertThat(artistDisplay, is(equalTo(getArtistDisplay1())));

    assertThat(artistDisplay.getId(), is(equalTo(dbArtist.getId())));
    assertThat(artistDisplay.getName(), is(equalTo(dbArtist.getName())));

    assertThat(artistDisplay.getAlbums().isEmpty(), is(false));
    assertThat(artistDisplay.getSongs().isEmpty(), is(false));
  }

  @Test
  public void getArtistById_InvalidId_ReturnsNull() {
    assertThat(artistService.getArtistById(-1), is(nullValue()));
  }

  @Test
  public void getArtistByName_ValidName_ReturnsArtist() {
    getArtistByNameHelper(false, false);
  }

  @Test
  public void getArtistByName_InvalidName_ReturnsNull() {
    assertThat(artistService.getArtistByName("Lang Lang"), is(nullValue()));
  }

  private void getArtistByNameHelper(boolean albumsEmpty, boolean songsEmpty) {
    ArtistDisplay artistDisplay = artistService.getArtistByName("Radiohead");
    Artist dbArtist = artistRepository.findById(1L);

    assertThat(artistDisplay, is(notNullValue()));

    assertThat(artistDisplay.getId(), is(equalTo(dbArtist.getId())));
    assertThat(artistDisplay.getName(), is(equalTo(dbArtist.getName())));

    assertThat(artistDisplay.getAlbums().isEmpty(), is(albumsEmpty));
    assertThat(artistDisplay.getSongs().isEmpty(), is(songsEmpty));

  }

  @Test
  public void createArtist_CreationSuccessful_ReturnsArtist_CreatesArtist() {
    Artist newArtist = new Artist("Lena Raine");
    Artist artist = artistService.createArtist(newArtist);
    newArtist.setId(artist.getId());
    Artist dbArtist = artistRepository.findByName(newArtist.getName());

    assertThat(artist, is(equalTo(newArtist)));
    assertThat(artist, is(equalTo(dbArtist)));
    assertThat(artist.getId(), is(equalTo(dbArtist.getId())));
  }

  @Test
  public void updateArtist_ValidId_ReturnsArtistUpdatesArtist() {
    Artist updatedArtist = new Artist("RYO FUKUI");
    Artist artist = artistService.updateArtist(2, updatedArtist);
    updatedArtist.setId(2);
    Artist dbArtist = artistRepository.findById(2L);

    assertThat(artist, is(equalTo(updatedArtist)));
    assertThat(artist, is(equalTo(dbArtist)));
  }

  @Test
  public void updateArtist_InvalidId_ReturnsNullDatabaseUnchanged() {
    Artist updatedArtist = new Artist("RYO FUKUI");
    Artist artist = artistService.updateArtist(-1, updatedArtist);
    assertThat(artist, is(nullValue()));
    Artist dbArtist = artistRepository.findById(1L);
    assertThat(dbArtist.getName(), is("Radiohead"));
    dbArtist = artistRepository.findById(2L);
    assertThat(dbArtist.getName(), is("Ryo Fukui"));
  }

  @Test
  public void deleteArtist_ValidId_ReturnsDeletedArtist_DeletesArtist() {
    Artist dbArtist = artistRepository.findById(1);
    Artist deletedArtist = artistService.deleteArtist(1);

    assertThat(deletedArtist, is(notNullValue()));
    assertThat(deletedArtist, is(equalTo(dbArtist)));

    assertThat(artistRepository.findById(1), is(nullValue()));
  }

  @Test
  public void deleteArtist_InvalidId_ReturnsNull_DatabaseUnchanged() {
    int dbSize = artistRepository.findAll().size();
    assertThat(artistService.deleteArtist(-1), is(nullValue()));
    assertThat(artistRepository.findAll().size(), is(equalTo(dbSize)));
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
    return new Album(1L, "A Moon Shaped Pool", "2016");
  }

  private Album getAlbum2() {
    return new Album(2L, "Scenery", "1977");
  }

  private Song getSong1() {
    return new Song(1L, "Daydreaming", "2016", "6:47");
  }

  private Song getSong2() {
    return new Song(2L, "It Could Happen to You", "1977", "4:13");
  }

}