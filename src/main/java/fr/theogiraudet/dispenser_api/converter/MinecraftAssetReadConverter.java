package fr.theogiraudet.dispenser_api.converter;

import fr.theogiraudet.dispenser_api.domain.MinecraftAsset;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
class MinecraftAssetReadConverter implements Converter<String, MinecraftAsset> {
    @Override
    public MinecraftAsset convert(String s) {
        return MinecraftAsset.valueOf(s);
    }
}
