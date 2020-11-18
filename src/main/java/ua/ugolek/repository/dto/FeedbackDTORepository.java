package ua.ugolek.repository.dto;

import org.springframework.stereotype.Repository;
import ua.ugolek.dto.FeedbackDTO;
import ua.ugolek.dto.mappers.FeedbackDTOMapper;
import ua.ugolek.model.Feedback;
import ua.ugolek.payload.filters.FeedbackFilter;
import ua.ugolek.repository.dto.extractors.DTOExtractor;
import ua.ugolek.repository.dto.extractors.FeedbackDTOExtractor;

@Repository
public class FeedbackDTORepository extends FilterSupportDTORepository<Feedback, FeedbackFilter, FeedbackDTO> {

    private FeedbackDTOMapper dtoMapper = new FeedbackDTOMapper();

    @Override
    protected DTOExtractor<Feedback, FeedbackFilter, FeedbackDTO> createDtoExtractor(FeedbackFilter filter) {
        return new FeedbackDTOExtractor(filter, entityManager, dtoMapper);
    }
}
