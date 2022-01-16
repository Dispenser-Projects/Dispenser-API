package fr.theogiraudet.minecraft_data.converter;

import fr.theogiraudet.minecraft_data.domain.MinecraftAsset;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
class MinecraftAssetReadConverter implements Converter<String, MinecraftAsset> {
    @Override
    public MinecraftAsset convert(String s) {
        return MinecraftAsset.valueOf(s);
    }
}
