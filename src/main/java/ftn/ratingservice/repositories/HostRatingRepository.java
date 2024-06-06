package ftn.ratingservice.repositories;

import ftn.ratingservice.domain.entities.HostRating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HostRatingRepository extends MongoRepository<HostRating, String> {

    List<HostRating> getAllByHostId(String hostId);

}
