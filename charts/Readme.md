# Helm Charts

Here we have Helm Charts following Backbase Guidelines. For more info check the 
internal [guidelines](https://backbase.atlassian.net/wiki/spaces/GUIL/pages/1820691788/Helm+Charts)

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
