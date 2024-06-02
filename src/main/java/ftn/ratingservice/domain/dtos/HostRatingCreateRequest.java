package ftn.ratingservice.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostRatingCreateRequest {

    private int rating;
    private String comment;
    private String hostId;

}
