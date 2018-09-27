package com.example.artistservice.dao;

import com.example.artistservice.model.Album;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "album-service", fallback = AlbumFallback.class)
public interface AlbumClient {

  @RequestMapping("/albums")
  List<Album> getAlbumsByArtistId(@RequestParam("artistId") long id);
}

@Component
class AlbumFallback implements AlbumClient {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public List<Album> getAlbumsByArtistId(long id) {
    logger.info("AlbumFallback used");
    return new ArrayList<>();
  }
}
