package ftn.ratingservice.services;

import ftn.ratingservice.AuthMongoIntegrationTest;
import ftn.ratingservice.domain.dtos.HostRatingCreateRequest;
import ftn.ratingservice.domain.dtos.HostRatingDto;
import ftn.ratingservice.domain.dtos.UserDto;
import ftn.ratingservice.domain.entities.HostRating;
import ftn.ratingservice.domain.entities.User;
import ftn.ratingservice.domain.entities.UserRole;
import ftn.ratingservice.exception.exceptions.NotFoundException;
import ftn.ratingservice.repositories.HostRatingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class RatingServiceTest extends AuthMongoIntegrationTest {

    @Autowired
    private RatingService ratingService;
    @Autowired
    private HostRatingRepository hostRatingRepository;
    @MockBean
    private RestService restService;

    @Test
    public void testGetHostRatings() {
        createHostRatings();
        String hostId = "e49fcab5-d45b-4556-9d91-14e58177fea6";

        List<HostRatingDto> ratings = ratingService.getHostRatings(hostId);
        assertEquals(2, ratings.size());
        assertEquals(hostId, ratings.get(0).getHostId());
        assertEquals(hostId, ratings.get(1).getHostId());
    }

    @Test
    public void testGetHostRating() {
        HostRating hostRating = createHostRating();

        HostRatingDto hostRatingDto = ratingService.getHostRating(hostRating.getId());
        assertNotNull(hostRatingDto);
        assertEquals(4, hostRatingDto.getRating());
        assertEquals("Good", hostRatingDto.getComment());
    }

    @Test
    public void getHostRatingNotFound() {
        String nonExistingId = "non-existing-id";
        assertThrows(NotFoundException.class, () -> ratingService.getHostRating(nonExistingId));
    }

    @Test
    public void testGetAverageHostRating() {
        createHostRatings();
        String hostId = "e49fcab5-d45b-4556-9d91-14e58177fea6";

        Double average = ratingService.getAverageHostRating(hostId);
        assertEquals(Double.valueOf(3), average);
    }

    @Test
    public void testCreateHostRating() {
        authenticateGuest();
        String hostId = "e49fcab5-d45b-4556-9d91-14e58177fea6";
        HostRatingCreateRequest createRequest = HostRatingCreateRequest.builder()
                .rating(5)
                .comment("Excellent host!")
                .hostId(hostId)
                .build();

        UserDto hostUser = UserDto.builder().role(UserRole.HOST).build();
        when(restService.getUserById(UUID.fromString(hostId))).thenReturn(hostUser);

        HostRatingDto createdHostRating = ratingService.createHostRating(createRequest);

        assertNotNull(createdHostRating);
        assertNotNull(createdHostRating.getId());

        HostRating savedRating = hostRatingRepository.findById(createdHostRating.getId()).orElse(null);
        assertNotNull(savedRating);
        assertNotNull(savedRating.getCreatedBy());
        assertEquals(5, savedRating.getRating());
    }

    private void createHostRatings() {
        hostRatingRepository.saveAll(List.of(
                HostRating.builder()
                        .hostId("e49fcab5-d45b-4556-9d91-14e58177fea6")
                        .rating(1)
                        .comment("Bad bad bad")
                        .createdBy(new User("e49fcaa5-d45b-4556-9d91-13e58187fea6", "guest"))
                        .build(),
                HostRating.builder()
                        .hostId("e49fcab5-d45b-4556-9d91-14e58177fea6")
                        .rating(5)
                        .comment("GREAT!")
                        .createdBy(new User("e49fcaa5-d45b-4556-9d91-13e58187fea6", "guest"))
                        .build()
        ));
    }

    private HostRating createHostRating() {
        return hostRatingRepository.save(
                HostRating.builder()
                        .hostId("e49fcab5-d45b-4556-9d91-14e58177fea6")
                        .rating(4)
                        .comment("Good")
                        .createdBy(new User("e49fcaa5-d45b-4556-9d91-13e58187fea6", "guest"))
                        .build()
        );
    }

}
