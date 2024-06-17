package ftn.ratingservice.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "hostRatings")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HostRating {

    @Id
    private String id;
    private String hostId;
    private int rating;
    private String comment;
    @Builder.Default
    private LocalDateTime datetime = LocalDateTime.now();
    private User createdBy;

}
