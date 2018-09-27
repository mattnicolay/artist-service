package com.example.artistservice.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import com.example.artistservice.model.Song;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(
    ids = "com.example:song-service:+:stubs:8100",
    stubsMode = StubsMode.LOCAL)
@TestPropertySource("classpath:test-application.yml")
public class SongClientTest {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  SongClient songClient;

  @Test
  public void getSongsByArtistId() {
    List<Song> songs = songClient.getSongsByArtistId(1L);

    logger.info(songs.toString());
    assertThat(songs.isEmpty(), is(false));
  }
}