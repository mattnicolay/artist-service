package com.example.artistservice.dao;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import com.example.artistservice.model.Artist;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@DatabaseSetup("classpath:test-dataset.xml")
@TestPropertySource("classpath:test-application.yml")
public class ArtistRepositoryIntegrationTests {

  @Autowired
  private ArtistRepository artistRepository;

  @Test
  public void findByName_ValidName_ReturnsArtist() {
    Artist artist = artistRepository.findByName("Whitney");

    assertThat(artist, is(notNullValue()));
    assertThat(artist.getId(), is(3L));
    assertThat(artist.getName(), is(equalTo("Whitney")));
  }

  @Test
  public void findByName_InvalidName_ReturnsNull() {
    assertThat(artistRepository.findByName("Queen"), is(nullValue()));
  }

  @Test
  public void findById_ValidId_ReturnsArtist() {
    Artist artist = artistRepository.findById(1L);

    assertThat(artist, is(notNullValue()));
    assertThat(artist.getId(), is(1L));
    assertThat(artist.getName(), is(equalTo("Radiohead")));
  }

  @Test
  public void findById_InvalidId_ReturnsNull() {
    assertThat(artistRepository.findById(-1L), is(nullValue()));
  }

  @Test
  public void findAll_NonEmpty_ReturnsArtistList() {
    List<Artist> artists = artistRepository.findAll();

    assertThat(artists, is(notNullValue()));
    assertFalse(artists.isEmpty());
  }

  @Test
  public void findAll_Empty_ReturnsEmptyList() {
    artistRepository.deleteAll();
    List<Artist> artists = artistRepository.findAll();

    assertThat(artists, is(notNullValue()));
    assertTrue(artists.isEmpty());
  }
}