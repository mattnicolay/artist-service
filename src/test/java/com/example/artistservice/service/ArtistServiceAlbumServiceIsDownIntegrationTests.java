package com.example.artistservice.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

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
    ids = {"com.example:song-service:+:stubs:8200"},
    stubsMode = StubsMode.LOCAL)
@TestPropertySource("classpath:test-application.yml")
public class ArtistServiceAlbumServiceIsDownIntegrationTests {

  @Autowired
  private ArtistRepository artistRepository;
  @Autowired
  private AlbumClient albumClient;
  @Autowired
  private SongClient songClient;
  @Autowired
  private ArtistService artistService;

  @Test
  public void getAllArtists_AlbumServiceIsDown_ReturnsEmptyListOfAlbums() {
    List<ArtistDisplay> artistDisplays = artistService.getAllArtists();
    List<Artist> dbArtists = artistRepository.findAll();

    assertThat(artistDisplays, is(notNullValue()));
    assertFalse(artistDisplays.isEmpty());

    List<Artist> artists = new ArrayList<>();
    artistDisplays.forEach(artistDisplay -> {
      assertThat(artistDisplay.getId(), is(notNullValue()));
      assertThat(artistDisplay.getName(), is(notNullValue()));
      assertThat(artistDisplay.getAlbums(), is(notNullValue()));
      assertThat(artistDisplay.getAlbums().isEmpty(), is(true));
      assertThat(artistDisplay.getSongs(), is(notNullValue()));
      assertThat(artistDisplay.getSongs().isEmpty(), is(false));

      artists.add(new Artist(artistDisplay.getName()));
    });
    dbArtists.forEach(artist -> assertThat(artists.contains(artist), is(true)));
  }

  @Test
  public void getArtistById_AlbumServiceIsDown_AlbumListIsEmpty() {
    ArtistDisplay artistDisplay = artistService.getArtistById(1L);
    Artist dbArtist = artistRepository.findById(1L);

    assertThat(artistDisplay, is(notNullValue()));
    assertThat(artistDisplay, is(equalTo(new ArtistDisplay(
        1L,
        "Radiohead",
        new ArrayList<>(),
        Arrays.asList(new Song(1L, "Daydreaming", "2016", "6:47"))))));

    assertThat(artistDisplay.getId(), is(equalTo(dbArtist.getId())));
    assertThat(artistDisplay.getName(), is(equalTo(dbArtist.getName())));

    assertThat(artistDisplay.getAlbums().isEmpty(), is(true));
    assertThat(artistDisplay.getSongs().isEmpty(), is(false));
  }

  @Test
  public void getArtistByName_AlbumServiceIsDown_AlbumListIsEmpty() {
    ArtistDisplay artistDisplay = artistService.getArtistByName("Radiohead");
    Artist dbArtist = artistRepository.findById(1L);

    assertThat(artistDisplay, is(notNullValue()));

    assertThat(artistDisplay.getId(), is(equalTo(dbArtist.getId())));
    assertThat(artistDisplay.getName(), is(equalTo(dbArtist.getName())));

    assertThat(artistDisplay.getAlbums().isEmpty(), is(true));
    assertThat(artistDisplay.getSongs().isEmpty(), is(false));
  }

}
