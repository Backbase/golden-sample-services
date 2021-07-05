# Helm Charts

Here we have Helm Charts following Backbase Guidelines. For more info check the 
internal [guidelines (Internal Link)](https://backbase.atlassian.net/wiki/spaces/GUIL/pages/1820691788/Helm+Charts)

We have 3 charts here, one per service:
- [Product](charts/product)
- [Review](charts/review) -> using Helm 3 already
- [Store](charts/store)

Our charts have a direct dependency on 
- [backbase-app](https://repo.backbase.com/backbase-charts/backbase-app/): A Common Helm Chart that represents typical Backbase Java Application based on Spring Boot. This chart should be used in other umbrella charts as a dependency with alias. This chart is highly dependent on Helm global values functionality. 
- [backbase-env](https://repo.backbase.com/backbase-charts/backbase-env/): A helm chart designed for development environments which is intended to simplify database schema creation & DRY for shared environment variables. We don't have a direct dependency on it but we will use it to create the database, the schemas and some common configurations for our services.

## How to use the Helm Charts

    > helm install backbase-env bb-charts/backbase-env -f bb-env-values.yaml
    > helm install product product/ -f product-mysql-values.yaml
    > helm install review review/ -f review-mysql-values.yaml
    > helm install store store/ -f store-mysql-values.yaml


## Helm Maven Plugin

https://github.com/kiwigrid/helm-maven-plugin is a Maven plugin for testing, packaging and uploading HELM charts.

### Usage

#### Goals

- `helm:init` initializes Helm by downloading a specific version
- `helm:dependency-build` resolves the chart dependencies  
- `helm:package` packages the given charts (chart.tar.gz)
- `helm:lint` lints the given charts
- `helm:dry-run` simulates an install
- `helm:upload` upload charts via HTTP PUT (Harbor not supported for now)

#### Packaging with the Helm Lifecycle 

To keep your pom files small you can use 'helm' packaging. This binds 
- helm:init to the initialize phase, 
- helm:lint to the test phase, 
- helm:package to the package phase 
- helm:upload to the deploy phase.
