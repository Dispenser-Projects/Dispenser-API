package fr.theogiraudet.minecraft_data.converter;

import fr.theogiraudet.minecraft_data.domain.MinecraftAsset;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
class MinecraftAssetWriteConverter implements Converter<MinecraftAsset, String> {
    @Override
    public String convert(MinecraftAsset minecraftAsset) {
        return minecraftAsset.name();
    }
}
