package com.backbase.goldensample.review.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.backbase.goldensample.review.dto.ReviewDTO;
import com.backbase.goldensample.review.persistence.ReviewEntity;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class EntityMapperTest {

    private final EntityMapper mapper = Mappers.getMapper(EntityMapper.class);

    @Test
    void mapperTests() {

        assertNotNull(mapper);

        ReviewDTO reviewDto = new ReviewDTO(1L, 2L, "author", "long content", "subject", 5, Map.of("key1", "val1"));

        ReviewEntity entity = mapper.dtoToEntity(reviewDto);

        assertAll(() -> assertEquals(reviewDto.getProductId(), entity.getProductId(), "Product ID is different"),
            () -> assertEquals(reviewDto.getId(), entity.getId(), "Review ID is different"),
            () -> assertEquals(reviewDto.getAuthor(), entity.getAuthor(), "Author is different"),
            () -> assertEquals(reviewDto.getSubject(), entity.getSubject(), "Subject is different"),
            () -> assertEquals(reviewDto.getContent(), entity.getContent(), "Content is different"),
            () -> assertEquals(reviewDto.getStars(), entity.getStars(), "Stars is different"),
            () -> assertEquals(reviewDto.getAdditions(), entity.getAdditions(), "Additions is different"));

        ReviewDTO reviewDto2 = mapper.entityToDto(entity);

        assertAll(() -> assertEquals(reviewDto.getProductId(), reviewDto2.getProductId(), "Product ID is different"),
            () -> assertEquals(reviewDto.getId(), reviewDto2.getId(), "Review ID is different"),
            () -> assertEquals(reviewDto.getAuthor(), reviewDto2.getAuthor(), "Author is different"),
            () -> assertEquals(reviewDto.getSubject(), reviewDto2.getSubject(), "Subject is different"),
            () -> assertEquals(reviewDto.getContent(), reviewDto2.getContent(), "Content is different"),
            () -> assertEquals(reviewDto.getStars(), reviewDto2.getStars(), "Stars is different"),
            () -> assertEquals(reviewDto.getAdditions(), reviewDto2.getAdditions(), "Additions is different"));
    }


}
