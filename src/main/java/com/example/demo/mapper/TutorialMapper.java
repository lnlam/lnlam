package com.example.demo.mapper;

import com.example.demo.dto.TutorialDTO;
import com.example.demo.entity.Tutorial;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TutorialMapper {
    TutorialMapper MAPPER = Mappers.getMapper(TutorialMapper.class);

    TutorialDTO mapToTutorialDTO(Tutorial tutorial);

    List<TutorialDTO> tutorialsToDTOs(List<TutorialDTO> tutorials);

    Tutorial dtoToEntity(TutorialDTO tutorialDTO);
}
