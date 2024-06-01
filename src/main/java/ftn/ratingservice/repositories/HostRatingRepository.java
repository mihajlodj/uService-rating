package ftn.ratingservice.repositories;

import ftn.ratingservice.domain.entities.HostRating;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HostRatingRepository extends MongoRepository<HostRating, String> {
}
