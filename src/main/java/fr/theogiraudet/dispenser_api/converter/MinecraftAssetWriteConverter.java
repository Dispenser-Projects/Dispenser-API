package fr.theogiraudet.dispenser_api.converter;

import fr.theogiraudet.dispenser_api.domain.MinecraftAsset;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
class MinecraftAssetWriteConverter implements Converter<MinecraftAsset, String> {
    @Override
    public String convert(MinecraftAsset minecraftAsset) {
        return minecraftAsset.name();
    }
}
