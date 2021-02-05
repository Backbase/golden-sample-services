# Intro

https://github.com/kiwigrid/helm-maven-plugin is a Maven plugin for testing, packaging and uploading HELM charts.

# Usage

## Goals

- `helm:init` initializes Helm by downloading a specific version
- `helm:dependency-build` resolves the chart dependencies  
- `helm:package` packages the given charts (chart.tar.gz)
- `helm:lint` tests the given charts
- `helm:dry-run` simulates an install
- `helm:upload` upload charts via HTTP PUT
