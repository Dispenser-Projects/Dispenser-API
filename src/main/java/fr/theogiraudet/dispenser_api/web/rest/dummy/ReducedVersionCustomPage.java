package fr.theogiraudet.dispenser_api.web.rest.dummy;

import fr.theogiraudet.dispenser_api.dto.CustomPage;
import fr.theogiraudet.dispenser_api.dto.reduced_resource.ReducedVersion;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.function.Function;

/**
 * Just a class to be used instead of {@link CustomPage} in the Swagger Documentation
 */
@Schema(name = "Version Page", description = "A Page of version data")
public class ReducedVersionCustomPage extends CustomPage<ReducedVersion> {
    /**
     * Create a new CustomPage
     *
     * @param count      the total number of data
     * @param limit      the maximum number of data to be present in this page
     * @param offset     the offset of the page (page number)
     * @param sort       a sort to apply to the entire data
     * @param elements   the elements wrapped in this page
     * @param controller the REST Controller of the wrapped data type, to define the next and previous URL
     * @param pointTo    the REST endpoint (function of the Controller) to use for build the next and previous URL
     */
    public <U> ReducedVersionCustomPage(long count, int limit, int offset, Sort sort, List<ReducedVersion> elements, Class<U> controller, Function<U, Object> pointTo) {
        super(count, limit, offset, sort, elements, controller, pointTo);
    }
}
