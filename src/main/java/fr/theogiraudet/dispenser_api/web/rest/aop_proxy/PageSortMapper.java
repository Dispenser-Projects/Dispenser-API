package fr.theogiraudet.dispenser_api.web.rest.aop_proxy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 *  Mapper to adapt pagination sort properties with custom and more user-friendly properties tag
 */
@Aspect
@Component
public class PageSortMapper {

    /** The mapper for {@link fr.theogiraudet.dispenser_api.domain.VersionInformation} */
    private final Map<String, String> versionMapper;
    /** The mapper for {@link fr.theogiraudet.dispenser_api.domain.MinecraftAsset} */
    private final Map<String, String> resourceMapper;

    public PageSortMapper() {
        versionMapper = new HashMap<>();
        resourceMapper = new HashMap<>();
        versionMapper.put("type", "versionType");
        resourceMapper.put("id", "versionedAssets");
    }

    /**
     * Proxy to adapt {@link fr.theogiraudet.dispenser_api.web.rest.VersionController} {@link Pageable} input
     */
    @Around("execution(public * fr.theogiraudet.dispenser_api.web.rest.VersionController.*(org.springframework.data.domain.Pageable, ..))")
    private Object proxyingVersion(ProceedingJoinPoint joinPoint) throws Throwable {
        return proxying(joinPoint, versionMapper);
    }

    /**
     * Proxy to adapt {@link fr.theogiraudet.dispenser_api.web.rest.abstract_resources.AssetController} {@link Pageable} input
     */
    @Around("execution(public * fr.theogiraudet.dispenser_api.web.rest.abstract_resources.AssetController+.*(org.springframework.data.domain.Pageable, ..))")
    private Object proxyingResource(ProceedingJoinPoint joinPoint) throws Throwable {
        return proxying(joinPoint, resourceMapper);
    }

    /**
     * @param sort the Sort to adapt
     * @return a new Sort with adapted properties to output URL
     */
    public Sort adapt(Sort sort) {
        final var map = new HashMap<String, String>();
        map.putAll(resourceMapper);
        map.putAll(versionMapper);
        return Sort.by(sort.stream()
                .map(order -> new Sort.Order(order.getDirection(), getKeyIfValueIs(map, order.getProperty())))
                .toList());
    }

    /**
     * @param map a mapper where get the output user-friendly property
     * @param value the value to adapt
     * @return value if value doesn't need to be adapt, the user-friendly property otherwise
     */
    private String getKeyIfValueIs(Map<String, String> map, String value) {
        return map.entrySet()
                .stream()
                .filter(pair -> pair.getValue().equals(value))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(value);
    }

    /**
     * Common proxying function to adapt the Sort according to the mapper
     */
    private Object proxying(ProceedingJoinPoint joinPoint, Map<String, String> mapper) throws Throwable {
        Pageable pageable = null;
        int index = -1;
        final var array = joinPoint.getArgs();
        for(int i = 0; i < array.length; i++)
            if(array[i] instanceof Pageable pageable1) {
                pageable = pageable1;
                index = i;
            }

        if(pageable != null) {
            final var sort = Sort.by(pageable.getSort().stream()
                    .map(order -> new Sort.Order(order.getDirection(), mapper.getOrDefault(order.getProperty(), order.getProperty())))
                    .toList());
            array[index]  = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        return joinPoint.proceed(array);
    }

}
