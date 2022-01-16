package fr.theogiraudet.minecraft_data.dto.mapper;

import fr.theogiraudet.minecraft_data.dao.MinecraftResourceRepository;
import fr.theogiraudet.minecraft_data.domain.VersionInformation;
import fr.theogiraudet.minecraft_data.dto.VersionInformationDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class VersionInformationMapper {

    @Autowired
    protected MinecraftResourceRepository repository = null;

    @Mapping(source = "url", target = "downloadUrl")
    @Mapping(source = "time", target = "updateTime")
    @Mapping(source = "versionType", target = "type")
    @Mapping(target = "repository", expression = "java(this.repository)")
    public abstract VersionInformationDto versionToDto(VersionInformation information);
}
