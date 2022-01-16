package fr.theogiraudet.dispenser_api.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * A custom {@link Page} to be returned by the REST API, to change the default Spring HATEOAS
 * @param <T> the type of the data wrapped in this page
 */
@Data
@Schema(name = "Page", description = "A Page of generic data")
public class CustomPage<T> {

    /** The total number of data */
    @Schema(description = "The number of data found")
    private long count;

    /** The URL to the next page */
    @Schema(description = "The URL to the next page")
    private String next;

    /** The URL to the previous page */
    @Schema(description = "The URL to the previous page")
    private String previous;

    /** The wrapped data in this page */
    @Schema(description = "The wrapped data in this page")
    private List<T> elements;

    /**
     * Create a new CustomPage
     * @param count the total number of data
     * @param limit the maximum number of data to be present in this page
     * @param offset the offset of the page (page number)
     * @param sort a sort to apply to the entire data
     * @param elements the elements wrapped in this page
     * @param controller the REST Controller of the wrapped data type, to define the next and previous URL
     * @param pointTo the REST endpoint (function of the Controller) to use for build the next and previous URL
     * @param <U> the type of the Controller
     */
    public <U> CustomPage(long count, int limit, int offset, Sort sort, List<T> elements, Class<U> controller, Function<U, Object> pointTo) {
        this.count = count;
        this.elements = new LinkedList<>(elements);
        if(offset + limit <= count) {
            next = linkTo(pointTo.apply(methodOn(controller)))
                    .slash(toParameters(limit, offset, sort)).toUri().toString();
        }
        if(offset > 0) {
            final var notBound = offset > limit;
            previous = linkTo(pointTo.apply(methodOn(controller)))
                    .slash(toParameters(notBound ? limit : offset, notBound ? offset - limit : 0, sort)).toUri().toString();
        }
    }

    /**
     * @param page the spring {@link Page}
     * @param controller the REST Controller of the wrapped data type, to define the next and previous URL
     * @param pointTo the REST endpoint (function of the Controller) to use for build the next and previous URL
     * @param <T> the type of the data wrapped in this page
     * @param <U> the type of the Controller
     * @return a new custom page from a Spring {@link Page}
     */
    public static <T, U> CustomPage<T> from(Page<T> page, Class<U> controller, Function<U, Object> pointTo) {
        return new CustomPage<>(page.getTotalElements(), page.getSize(), page.getNumber(), page.getSort(), page.getContent(), controller, pointTo);
    }

    /**
     * @param limit the size limit of the CustomPage
     * @param offset the offset (page number) of the CustomPage
     * @param sort the sort to apply to the wrapped data
     * @return a string corresponding to HTTP parameters for this function parameters
     */
    private String toParameters(int limit, int offset, Sort sort) {
        final var sortString = sort.stream()
                .map(order -> "sort=" + order.getProperty() + ',' + order.getDirection().name().toLowerCase())
                .collect(Collectors.joining("&"));
        return String.format("?offset=%d&limit=%d&%s", offset, limit, sortString);
    }
}