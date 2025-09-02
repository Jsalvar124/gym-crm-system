package com.jsalva.gymsystem.mapper;

import com.jsalva.gymsystem.dto.response.TraineeResponseDto;
import com.jsalva.gymsystem.entity.Trainee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TrainerMapper.class)
public interface TraineeMapper {
    TraineeResponseDto toResponseDto(Trainee trainee);

}
