package ftn.ratingservice.domain.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LodgeRatingCreateRequest {

    @NotNull
    @Min(1) @Max(5)
    private int rating;
    private String comment;
    @NotEmpty
    private String lodgeId;
    @NotEmpty
    private String hostId;

}
