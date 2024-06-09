package ftn.ratingservice.services;

import ftn.ratingservice.AuthMongoIntegrationTest;
import ftn.ratingservice.domain.dtos.*;
import ftn.ratingservice.domain.entities.*;
import ftn.ratingservice.exception.exceptions.NotFoundException;
import ftn.ratingservice.repositories.HostRatingRepository;
import ftn.ratingservice.repositories.LodgeRatingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class RatingServiceTest extends AuthMongoIntegrationTest {

    @Autowired
    private RatingService ratingService;
    @Autowired
    private HostRatingRepository hostRatingRepository;
    @Autowired
    private LodgeRatingRepository lodgeRatingRepository;
    @MockBean
    private RestService restService;
    @MockBean
    private NotificationService notificationService;

    @AfterEach
    private void cleanup() {
        hostRatingRepository.deleteAll();
        lodgeRatingRepository.deleteAll();
    }

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
        doNothing().when(notificationService).sendNotification(anyString(), any(NotificationType.class));

        HostRatingDto createdHostRating = ratingService.createHostRating(createRequest);

        assertNotNull(createdHostRating);
        assertNotNull(createdHostRating.getId());

        HostRating savedRating = hostRatingRepository.findById(createdHostRating.getId()).orElse(null);
        assertNotNull(savedRating);
        assertNotNull(savedRating.getCreatedBy());
        assertEquals(5, savedRating.getRating());
    }

    @Test
    public void testGetLodgeRatings() {
        createLodgeRatings();
        String lodgeId = "e49rcab5-d45b-4526-9591-14ef81g7fea6";

        List<LodgeRatingDto> ratings = ratingService.getLodgeRatings(lodgeId);
        assertEquals(2, ratings.size());
        assertEquals(lodgeId, ratings.get(0).getLodgeId());
        assertEquals(lodgeId, ratings.get(1).getLodgeId());
    }

    @Test
    public void testGetLodgeRating() {
        LodgeRating lodgeRating = createLodgeRating();

        LodgeRatingDto lodgeRatingDto = ratingService.getLodgeRating(lodgeRating.getId());
        assertNotNull(lodgeRatingDto);
        assertEquals(4, lodgeRatingDto.getRating());
        assertEquals("Good", lodgeRatingDto.getComment());
    }

    @Test
    public void getLodgeRatingNotFound() {
        String nonExistingId = "non-existing-id";
        assertThrows(NotFoundException.class, () -> ratingService.getLodgeRating(nonExistingId));
    }

    @Test
    public void testGetAverageLodgeRating() {
        createLodgeRatings();
        String lodgeId = "e49rcab5-d45b-4526-9591-14ef81g7fea6";

        Double average = ratingService.getAverageLodgeRating(lodgeId);
        assertEquals(Double.valueOf(3), average);
    }

    @Test
    public void testCreateLodgeRating() {
        authenticateGuest();
        String hostId = "e49fcab5-d45b-4556-9d91-14e58177fea6";
        String lodgeId = "e49rcab5-d45b-4526-9591-14ef81g7fea6";
        LodgeRatingCreateRequest createRequest = LodgeRatingCreateRequest.builder()
                .rating(5)
                .comment("Excellent host!")
                .hostId(hostId)
                .lodgeId(lodgeId)
                .build();

        doNothing().when(notificationService).sendNotification(anyString(), any(NotificationType.class));

        LodgeRatingDto createdLodgeRating = ratingService.createLodgeRating(createRequest);

        assertNotNull(createdLodgeRating);
        assertNotNull(createdLodgeRating.getId());

        LodgeRating savedRating = lodgeRatingRepository.findById(createdLodgeRating.getId()).orElse(null);
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

    private void createLodgeRatings() {
        lodgeRatingRepository.saveAll(List.of(
                LodgeRating.builder()
                        .hostId("e49fcab5-d45b-4556-9d91-14e58177fea6")
                        .lodgeId("e49rcab5-d45b-4526-9591-14ef81g7fea6")
                        .rating(1)
                        .comment("Bad bad bad")
                        .createdBy(new User("e49fcaa5-d45b-4556-9d91-13e58187fea6", "guest")).build(),
                LodgeRating.builder()
                        .hostId("e49fcab5-d45b-4556-9d91-14e58177fea6")
                        .lodgeId("e49rcab5-d45b-4526-9591-14ef81g7fea6")
                        .rating(5)
                        .comment("Fantastic")
                        .createdBy(new User("e49fcaa5-d45b-4556-9d91-13e58187fea6", "guest")).build()
        ));
    }

    private LodgeRating createLodgeRating() {
        return lodgeRatingRepository.save(
                LodgeRating.builder()
                        .hostId("e49fcab5-d45b-4556-9d91-14e58177fea6")
                        .lodgeId("e49rcab5-d45b-4526-9591-14ef81g7fea6")
                        .rating(4)
                        .comment("Good")
                        .createdBy(new User("e49fcaa5-d45b-4556-9d91-13e58187fea6", "guest")).build()
        );
    }

}
