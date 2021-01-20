It's not a good place to put code-gen with method 'main' in a library. 
The codegen is single class, it is simple and we would like to ship it together with other classes in this library 
so that it can be used in maven plugin 
If the code-gen becomes bigger and more complex, it shouldn't live in this library, and consider to extract it 
to another library, as it's used only in maven build.

Usage: put exec-maven-plugin AFTER swagger-codegen-maven-plugin

Example:
```
<plugin>
		<groupId>io.swagger.codegen.v3</groupId>
		<artifactId>swagger-codegen-maven-plugin</artifactId>
		<version>${swagger-codegen-maven-plugin.version}</version>
		.....
</plugin>

<plugin>
	<groupId>org.codehaus.mojo</groupId>
	<artifactId>exec-maven-plugin</artifactId>
	<version>${exec-maven-plugin.version}</version>
	<executions>
		<execution>
			<id>custom-swagger-codegen-model</id>
			<phase>generate-sources</phase>
			<goals>
				<goal>java</goal>
			</goals>
			<configuration>
				<mainClass>net.ionoff.common.validation.codegen.ConstraintAnnotationCodeGen</mainClass>
				<arguments>
					<argument>--api_file</argument>
					<argument>${project.basedir}/../api.json</argument>
					<argument>--model_dir</argument>
					<argument>${project.basedir}/target/generated-sources/swagger/src/gen/java/net/ionoff/media/api/model</argument>
					<argument>--model_name_suffix</argument>
					<argument>Dto</argument>
					<argument>--validation_packages</argument>
					<argument>net.ionoff.media.validation.constraints</argument>
				</arguments>
			</configuration>
		</execution>
	</executions>
</plugin>
```