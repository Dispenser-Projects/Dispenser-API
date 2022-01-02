package fr.theogiraudet.tp.spring.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data
class VersionInformation {

    private String id;
    private String type;
    private String url;
    @JsonAlias("sha1")
    private String hash;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:m:ssXXX")
    private OffsetDateTime time;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:m:ssXXX")
    private OffsetDateTime  releaseTime;
    private boolean needToBeUpdated = false;
    private boolean lock = false;

}
