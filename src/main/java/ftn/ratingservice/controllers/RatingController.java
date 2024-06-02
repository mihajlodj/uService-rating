package ftn.ratingservice.controllers;

import ftn.ratingservice.domain.dtos.HostRatingCreateRequest;
import ftn.ratingservice.domain.dtos.HostRatingUpdateRequest;
import ftn.ratingservice.services.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/host/all/{hostId}")
    public ResponseEntity<?> getHostRatings(@PathVariable String hostId) {
        return ResponseEntity.ok(ratingService.getHostRatings(hostId));
    }

    @GetMapping("host/average/{hostId}")
    public ResponseEntity<?> getAverageHostRating(@PathVariable String hostId) {
        return ResponseEntity.ok(ratingService.getAverageHostRating(hostId));
    }

    @GetMapping("/host/{id}")
    public ResponseEntity<?> getHostRating(@PathVariable String id) {
        return ResponseEntity.ok(ratingService.getHostRating(id));
    }

    @PostMapping("/host")
    @PreAuthorize("hasAuthority('GUEST')")
    public ResponseEntity<?> createHostRating(@RequestBody @Valid HostRatingCreateRequest createRequest) {
        return ResponseEntity.ok(ratingService.createHostRating(createRequest));
    }

    @PutMapping("/host/{id}")
    @PreAuthorize("hasAuthority('GUEST')")
    public ResponseEntity<?> updateHostRating(@PathVariable String id, @RequestBody @Valid HostRatingUpdateRequest updateRequest) {
        return ResponseEntity.ok(ratingService.updateHostRating(id, updateRequest));
    }

    @DeleteMapping("/host/{id}")
    @PreAuthorize("hasAuthority('GUEST')")
    public ResponseEntity<?> deleteHostRating(@PathVariable String id) {
        ratingService.deleteHostRating(id);
        return ResponseEntity.ok().build();
    }

}
