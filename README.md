# mockito-graphql-matchers
Mockito argument-matchers for GraphQL.

Features: 

 * query
   * name
   * arguments
 * mutation
   * name
   * arguments

The library is primarily intended for mocking proxied GraphQL service implementations, i.e. without needing the whole payload.

## Obtain
The project is built with [Maven] and is available on the central Maven repository. 

<details>
  <summary>Maven coordinates</summary>

Add the property
```xml
<mockito-graphql-matchers.version>1.0.0</mockito-graphql-matchers.version>
```

then add

```xml
<dependency>
    <groupId>com.github.skjolber.mockito.graphql</groupId>
    <artifactId>matchers</artifactId>
    <version>${mockito-graphql-matchers.version}</version>
</dependency>
```
</details>

or

<details>
  <summary>Gradle coordinates</summary>

For

```groovy
ext {
  mockitoGraphqlMatchersVersion = '1.0.0'
}
```

add

```groovy
api ("com.github.skjolber.mockito.graphql:matchers:${mockitoGraphqlMatchersVersion}")
```
</details>

## Usage
Add import

```
import static com.github.skjolber.mockito.graphql.matchers.ArgumentMatchers.*;
```

then start mocking

```
when(http.exchange(queryName("getUser"))).thenReturn("{ ... user response ... }");
when(http.exchange(queryName("getTickets"))).thenReturn("{ ... tickets response ... }");
```

so to emulate more complex mock GraphQL service behaviors.

## License
[Apache 2.0]

## History

  * 1.0.0: Initial release.

[Apache 2.0]:          	http://www.apache.org/licenses/LICENSE-2.0.html
[issue-tracker]:       	https://github.com/skjolber/mockito-graphql-matchers/issues
[Maven]:                http://maven.apache.org/
