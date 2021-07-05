# API Module

OpenAPI specifications for Golden Sample services. Here we show to we work with OpenAPI specifications, how we validate them, bundle them, generate documentation and code from them.

## BOAT Plugin

We use [Backbase OpenApi Tools](https://github.com/Backbase/backbase-openapi-tools)
The Backbase Open API Tools is a collection of tools created to work efficiently with OpenAPI.
Please take a look at the pom.xml files in each module to learn how BOAT can help you in your work with OpenAPI specs and following an _API First_ way of working.

## Mock server

We provide here way to generate mocks from the OpenAPI specs using [prism](https://stoplight.io/p/docs/gh/stoplightio/prism/README.md), the mock server may be executed in two modes:

- `static`: the responses will always be the predefined ones in the specification examples
- `dynamic`: the responses will have random values changing on every request

These are the maven commands to execute it:

- `static`: `mvn package -Pstatic`
- `dynamic`: `mvn package -Pdynamic`

