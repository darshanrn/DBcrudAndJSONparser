# DBcrudAndJSONparser

This is a simple light weighted java project that consumes a opensource webservice that spits 100 json objects. This java projects reads the webservice url from the project properties and makes a http url GET connection and receives the json response.
The json response is then parsed into array of geo name objects and then inserted into a sqlite database