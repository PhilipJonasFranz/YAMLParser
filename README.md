# YAMLParser [![version](https://img.shields.io/badge/version-1.0.0-green.svg)](https://semver.org)

## How to use

Include the standalone .jar in your project folder and you are ready to go. With

`YAMLTree tree = YAMLParser.parse(file)` 

you can parse a given file into the YAML-Tree data structure. Via the accessor methods you can readout the data you are interested in. Note that during parsing, an exception can be thrown if the given input is null or the given YAML-Input is malformed.

## License and Copyright
 © Philip Jonas Franz
 
 Licensed under [Apache License 2.0](LICENSE). 