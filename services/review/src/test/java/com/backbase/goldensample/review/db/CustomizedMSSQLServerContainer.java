package com.backbase.goldensample.review.db;

import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.utility.DockerImageName;

public class CustomizedMSSQLServerContainer extends MSSQLServerContainer<CustomizedMSSQLServerContainer> {

    public CustomizedMSSQLServerContainer(DockerImageName dockerImageName) {
        super(dockerImageName);
    }

    @Override
    public String getUsername() {
        return "sa";
    }

    @Override
    protected void configure() {
        super.configure();
        addEnv("MSSQL_COLLATION", "Latin1_General_CS_AS_KS");
    }

}
