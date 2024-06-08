package ftn.ratingservice.domain.dtos;

import ftn.ratingservice.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LodgeRatingDto {

    private String id;
    private String lodgeId;
    private int rating;
    private String comment;
    private LocalDateTime datetime;
    private User createdBy;

}
