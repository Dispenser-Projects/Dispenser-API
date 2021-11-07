package fr.theogiraudet.tp.spring.dto;

public class Model {

    private String modelName;
    private String modelUri;

    public Model(String modelName, String modelUri) {
        this.modelName = modelName;
        this.modelUri = modelUri;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelUri() {
        return modelUri;
    }

    public void setModelUri(String modelUri) {
        this.modelUri = modelUri;
    }
}
