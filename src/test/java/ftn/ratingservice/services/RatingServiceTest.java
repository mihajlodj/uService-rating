package ftn.ratingservice.services;

import ftn.ratingservice.domain.dtos.HostRatingDto;
import ftn.ratingservice.domain.dtos.LodgeRatingDto;
import ftn.ratingservice.domain.entities.HostRating;
import ftn.ratingservice.domain.entities.LodgeRating;
import ftn.ratingservice.domain.entities.User;
import ftn.ratingservice.exception.exceptions.NotFoundException;
import ftn.ratingservice.repositories.HostRatingRepository;
import ftn.ratingservice.repositories.LodgeRatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RatingServiceTest {

    private HostRatingRepository hostRatingRepository;
    private LodgeRatingRepository lodgeRatingRepository;
    private RatingService ratingService;

    @BeforeEach
    void setUp() {
        hostRatingRepository = mock(HostRatingRepository.class);
        lodgeRatingRepository = mock(LodgeRatingRepository.class);
        NotificationService notificationService = mock(NotificationService.class);
        RestService restService = mock(RestService.class);
        ratingService = new RatingService(hostRatingRepository, lodgeRatingRepository, notificationService, restService);
    }

    @Test
    public void testGetHostRatings() {
        String hostId = "host123";
        HostRating rating1 = new HostRating("1", hostId, 5, "Great place", LocalDateTime.now(), new User("user1", "User One"));
        HostRating rating2 = new HostRating("2", hostId, 4, "Good service", LocalDateTime.now(), new User("user2", "User Two"));
        List<HostRating> ratings = Arrays.asList(rating1, rating2);

        when(hostRatingRepository.getAllByHostId(hostId)).thenReturn(ratings);

        List<HostRatingDto> result = ratingService.getHostRatings(hostId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(5, result.get(0).getRating());
        assertEquals(4, result.get(1).getRating());

        verify(hostRatingRepository).getAllByHostId(hostId);
        verifyNoMoreInteractions(hostRatingRepository);
    }

    @Test
    public void testGetHostRatingSuccess() {
        String id = "rating1";
        HostRating mockRating = new HostRating("rating1", "host123", 5, "Excellent", LocalDateTime.now(), new User("user1", "User One"));
        when(hostRatingRepository.findById(id)).thenReturn(Optional.of(mockRating));

        HostRatingDto result = ratingService.getHostRating(id);

        assertNotNull(result);
        assertEquals("Excellent", result.getComment());
        assertEquals(5, result.getRating());
        verify(hostRatingRepository).findById(id);
    }

    @Test
    public void testGetHostRatingNotFound() {
        String nonExistentId = "nonexistent-id";
        when(hostRatingRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> ratingService.getHostRating(nonExistentId));

        assertEquals("Rating not found", exception.getMessage());
        verify(hostRatingRepository).findById(nonExistentId);
    }

    @Test
    public void testGetAverageHostRatingWithRatings() {
        String hostId = "host123";
        List<HostRating> ratings = Arrays.asList(
                new HostRating("1", hostId, 5, "Great place", LocalDateTime.now(), new User("user1", "User One")),
                new HostRating("2", hostId, 4, "Good service", LocalDateTime.now(), new User("user2", "User Two"))
        );
        when(hostRatingRepository.getAllByHostId(hostId)).thenReturn(ratings);

        Double average = ratingService.getAverageHostRating(hostId);

        assertNotNull(average);
        assertEquals(4.5, average, 0.01, "The average rating should be correctly calculated.");
        verify(hostRatingRepository).getAllByHostId(hostId);
    }

    @Test
    public void testGetAverageHostRatingNoRatings() {
        String hostId = "host123";
        when(hostRatingRepository.getAllByHostId(hostId)).thenReturn(Collections.emptyList());

        Double average = ratingService.getAverageHostRating(hostId);

        assertNull(average, "Average should be null when there are no ratings.");
        verify(hostRatingRepository).getAllByHostId(hostId);
    }

    @Test
    public void testGetLodgeRatings() {
        String lodgeId = "lodge123";
        List<LodgeRating> mockRatings = Arrays.asList(
                new LodgeRating("1", lodgeId, "host1", 5, "Excellent", LocalDateTime.now(), new User("user1", "User One")),
                new LodgeRating("2", lodgeId, "host2", 4, "Good", LocalDateTime.now(), new User("user2", "User Two"))
        );
        when(lodgeRatingRepository.getAllByLodgeId(lodgeId)).thenReturn(mockRatings);

        List<LodgeRatingDto> results = ratingService.getLodgeRatings(lodgeId);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(5, results.get(0).getRating());
        assertEquals("Excellent", results.get(0).getComment());
        verify(lodgeRatingRepository).getAllByLodgeId(lodgeId);
    }

    @Test
    public void testGetLodgeRatingSuccess() {
        String id = "rating1";
        LodgeRating mockRating = new LodgeRating("rating1", "lodge123", "host123", 5, "Excellent", LocalDateTime.now(), new User("user1", "User One"));
        when(lodgeRatingRepository.findById(id)).thenReturn(Optional.of(mockRating));

        LodgeRatingDto result = ratingService.getLodgeRating(id);

        assertNotNull(result);
        assertEquals("Excellent", result.getComment());
        assertEquals(5, result.getRating());
        verify(lodgeRatingRepository).findById(id);
    }

    @Test
    public void testGetLodgeRatingNotFound() {
        String nonExistentId = "nonexistent-id";
        when(lodgeRatingRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> ratingService.getLodgeRating(nonExistentId));

        assertEquals("Rating not found", exception.getMessage());
        verify(lodgeRatingRepository).findById(nonExistentId);
    }

    @Test
    public void testGetAverageLodgeRating() {
        String lodgeId = "lodge123";
        List<LodgeRating> mockRatings = Arrays.asList(
                new LodgeRating("1", lodgeId, "host123", 5, "Excellent", LocalDateTime.now(), new User("user1", "User One")),
                new LodgeRating("2", lodgeId, "host123", 3, "Good", LocalDateTime.now(), new User("user2", "User Two"))
        );
        when(lodgeRatingRepository.getAllByLodgeId(lodgeId)).thenReturn(mockRatings);

        Double average = ratingService.getAverageLodgeRating(lodgeId);

        assertNotNull(average);
        assertEquals(4.0, average, 0.01, "The average rating should be correctly calculated.");
        verify(lodgeRatingRepository).getAllByLodgeId(lodgeId);
    }



}
