package com.momentu.momentuapi.repos;

import com.momentu.momentuapi.entities.Hashtag;
import com.momentu.momentuapi.entities.keys.HashtagKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported=false)
public interface HashtagRepository extends CrudRepository<Hashtag, HashtagKey> {

    @Query("select h from Hashtag h where h.hashtagKey.label=:label and h.hashtagKey.locationId=:locationId")
    Optional<Hashtag> findByLabelAndLocationId(@Param("label") String label, @Param("locationId") Long locationId);
}
