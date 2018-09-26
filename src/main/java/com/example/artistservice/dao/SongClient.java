package com.example.artistservice.dao;

import com.example.artistservice.model.Song;
import java.util.ArrayList;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "song-service", fallback = SongFallback.class)
public interface SongClient {
  @RequestMapping("/songs")
  List<Song> getSongsByArtistName(@RequestParam("artist") String name);

  @RequestMapping("/actuator/health")
  String healthCheck();
}

@Component
class SongFallback implements SongClient {

  @Override
  public List<Song> getSongsByArtistName(String name) {
    return new ArrayList<>();
  }

  @Override
  public String healthCheck() {
    return "DOWN";
  }
}
