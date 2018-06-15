# Configurator
[![Build Status][badge-travis]][travis] [![codecov][badge-codecov]][codecov] [![Known Vulnerabilities][badge-snyk]][snyk]

[badge-travis]: https://travis-ci.org/araknoid/Configurator.svg?branch=master
[travis]: https://travis-ci.org/araknoid/Configurator
[badge-codecov]:https://codecov.io/gh/araknoid/Configurator/branch/master/graph/badge.svg
[codecov]:https://codecov.io/gh/araknoid/Configurator
[badge-snyk]:https://snyk.io/test/github/araknoid/Configurator/badge.svg
[snyk]:https://snyk.io/test/github/araknoid/Configurator

HTTP API for managing the configuration values of a system

#### Configuration

The configuration is composed by three attributes:
* *id*: Unique identifier of the configuration
* *name*: Description of the configuration
* *value*: Value of the configuration

Example of configuration:

```
{  
    "id": "server.port",  
    "name": "Configuration for server port",  
    "value": "8080"  
}
```
