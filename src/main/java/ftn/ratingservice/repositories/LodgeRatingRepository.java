package ftn.ratingservice.repositories;

import ftn.ratingservice.domain.entities.LodgeRating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LodgeRatingRepository extends MongoRepository<LodgeRating, String> {

    List<LodgeRating> getAllByLodgeId(String lodgeId);

}
