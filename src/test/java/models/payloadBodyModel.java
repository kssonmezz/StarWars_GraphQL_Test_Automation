package models;

import tests.starWarsPeopleTests;

import java.util.List;
import java.util.Map;

public class payloadBodyModel {

    String query;
    Map<String,String> variables;
    String operationName;

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }


    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }




}
