package ftn.ratingservice.domain.dtos;

import ftn.ratingservice.domain.entities.NotificationType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest implements Serializable {

    @NotEmpty
    private String userId;
    @NotNull
    private NotificationType notificationType;

}
