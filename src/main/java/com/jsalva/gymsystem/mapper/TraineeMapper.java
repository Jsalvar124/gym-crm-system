package com.jsalva.gymsystem.mapper;

import com.jsalva.gymsystem.dto.response.TraineeResponseDto;
import com.jsalva.gymsystem.dto.response.TraineeSummaryDto;
import com.jsalva.gymsystem.dto.response.TrainerSummaryDto;
import com.jsalva.gymsystem.entity.Trainee;
import com.jsalva.gymsystem.entity.Trainer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = TrainerMapper.class)
public interface TraineeMapper {
    TraineeResponseDto toResponseDto(Trainee trainee);

    TraineeSummaryDto toSummaryDto(Trainee trainee);

    List<TraineeSummaryDto> toSummaryDtoList(List<Trainee> trainees);
}
