## Mock server

The provided [prism](https://stoplight.io/p/docs/gh/stoplightio/prism/README.md) mock server may be executed in two modes:

- `static`: the responses will always be the predefined ones in the specification examples
- `dynamic`: the responses will have random values changing on every request

These are the maven commands to execute it:

- `static`: `mvn package -Pstatic`
- `dynamic`: `mvn package -Pdynamic`

You may also run `prism` yourself pointing it to the specification file [openapi.yaml](src/main/resources/openapi.yaml).
