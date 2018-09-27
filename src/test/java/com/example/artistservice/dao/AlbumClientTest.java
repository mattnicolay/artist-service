package com.example.artistservice.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import com.example.artistservice.model.Album;
import feign.Feign;
import feign.slf4j.Slf4jLogger;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(
    ids = "com.example:album-service:+:stubs:8100",
    stubsMode = StubsMode.LOCAL)
@TestPropertySource("classpath:test-application.yml")
public class AlbumClientTest {

  @Autowired
  AlbumClient albumClient;

  @Test
  public void getAlbumsByArtistId() {
    List<Album> albums = albumClient.getAlbumsByArtistId(1L);

    assertThat(albums.isEmpty(), is(false));
  }
}