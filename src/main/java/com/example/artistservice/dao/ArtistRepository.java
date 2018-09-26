package com.example.artistservice.dao;

import com.example.artistservice.model.Artist;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ArtistRepository extends CrudRepository<Artist, Long> {

  List<Artist> findAll();

  Artist findById(long id);

  Artist findByName(String name);
}
