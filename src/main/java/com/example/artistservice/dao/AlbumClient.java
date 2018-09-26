package com.example.artistservice.dao;

import com.example.artistservice.model.Album;
import java.util.ArrayList;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "album-service", fallback = AlbumFallback.class)
public interface AlbumClient {

  @RequestMapping("/albums")
  List<Album> getAlbumsByArtistName(@RequestParam("artist") String name);

  @RequestMapping("/actuator/health")
  String healthCheck();
}

@Component
class AlbumFallback implements AlbumClient {

  @Override
  public List<Album> getAlbumsByArtistName(String name) {
    return new ArrayList<>();
  }

  @Override
  public String healthCheck() {
    return "DOWN";
  }
}
