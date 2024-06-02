package ftn.ratingservice.services;

import ftn.ratingservice.domain.dtos.HostRatingCreateRequest;
import ftn.ratingservice.domain.dtos.HostRatingDto;
import ftn.ratingservice.domain.dtos.HostRatingUpdateRequest;
import ftn.ratingservice.domain.dtos.UserDto;
import ftn.ratingservice.domain.entities.HostRating;
import ftn.ratingservice.domain.entities.User;
import ftn.ratingservice.domain.entities.UserRole;
import ftn.ratingservice.domain.mappers.HostRatingMapper;
import ftn.ratingservice.exception.exceptions.BadRequestException;
import ftn.ratingservice.exception.exceptions.ForbiddenException;
import ftn.ratingservice.exception.exceptions.NotFoundException;
import ftn.ratingservice.repositories.HostRatingRepository;
import ftn.ratingservice.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final HostRatingRepository hostRatingRepository;
    private final RestService restService;

    public List<HostRatingDto> getHostRatings(String hostId) {
        List<HostRating> ratings = hostRatingRepository.getAllByHostId(hostId);
        return ratings.stream().map(HostRatingMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public HostRatingDto getHostRating(String id) {
        HostRating rating = hostRatingRepository.findById(id).orElseThrow(() -> new NotFoundException("Rating not found"));
        return HostRatingMapper.INSTANCE.toDto(rating);
    }

    public Double getAverageHostRating(String hostId) {
        List<HostRating> ratings = hostRatingRepository.getAllByHostId(hostId);
        if (ratings.isEmpty()) {
            return null;
        }

        double total = 0;
        for (HostRating rating : ratings) {
            total += rating.getRating();
        }
        return total / ratings.size();
    }

    public HostRatingDto createHostRating(HostRatingCreateRequest createRequest) {
        UserDto userDto = restService.getUserById(UUID.fromString(createRequest.getHostId()));
        if (userDto.getRole() != UserRole.HOST) {
            throw new BadRequestException("Can't rate non host");
        }

        HostRating hostRating = HostRatingMapper.INSTANCE.fromCreateRequest(createRequest);
        User createdBy = User.builder()
                .userId(AuthUtils.getLoggedUserId().toString())
                .username(AuthUtils.getLoggedUsername())
                .build();
        hostRating.setCreatedBy(createdBy);

        return HostRatingMapper.INSTANCE.toDto(hostRatingRepository.save(hostRating));
    }

    public HostRatingDto updateHostRating(String id, HostRatingUpdateRequest updateRequest) {
        HostRating hostRating = hostRatingRepository.findById(id).orElseThrow(() -> new NotFoundException("Rating not found"));
        if (!hostRating.getCreatedBy().getUserId().equals(AuthUtils.getLoggedUserId().toString())) {
            throw new ForbiddenException("Can't edit other's rating");
        }

        HostRatingMapper.INSTANCE.update(hostRating, updateRequest);
        return HostRatingMapper.INSTANCE.toDto(hostRatingRepository.save(hostRating));
    }

    public void deleteHostRating(String id) {
        HostRating hostRating = hostRatingRepository.findById(id).orElseThrow(() -> new NotFoundException("Rating not found"));
        if (!hostRating.getCreatedBy().getUserId().equals(AuthUtils.getLoggedUserId().toString())) {
            throw new ForbiddenException("Can't delete other's rating");
        }

        hostRatingRepository.deleteById(id);
    }

}
