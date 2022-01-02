package fr.theogiraudet.tp.spring.domain;

import lombok.Data;

import java.util.List;

public @Data class Manifest {

    private Latest latest;
    private List<VersionInformation> versions;

    @Data
    static class Latest {
        private String release;
        private String snapshot;
    }

}
