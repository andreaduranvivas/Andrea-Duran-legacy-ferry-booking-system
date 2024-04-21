# Deuda Técnica en Procesos

Configuramos un proceso de CI utilizando GitHub Actions con los pasos de Build, Unit Test y Code Analysis reportando en SonarCloud para un proyecto Gradle.
Este flujo de trabajo se activará en eventos de push y pull request en la rama principal.

## Archivo build.yml

Primero, crea un archivo `.github/workflows/build.yml` en la raíz del repositorio con el siguiente contenido:

```yaml
name: Java CI with Gradle and SonarCloud

on:
push:
branches: [ "main" ]
pull_request:
branches: [ "main" ]

jobs:
build:
runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'adopt'

    - name: Setup Gradle
      uses: gradle/gradle-build-action@v2
      with:
        gradle-version: 7.4

    - name: Build with Gradle
      run: ./gradlew build

    - name: Run Unit Tests
      run: ./gradlew test

sonarcloud:
needs: build
runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'adopt'

    - name: Setup Gradle
      uses: gradle/gradle-build-action@v2
      with:
        gradle-version: 7.4

    - name: SonarCloud Scan
      uses: jinguji/sonarcloud-gradle-github-action@v2
      with:
        sonar-token: ${{ secrets.SONAR_TOKEN }}
      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

Este flujo de trabajo se divide en dos trabajos principales:

1. **Build y Unit Tests:** Este trabajo se encarga de compilar el proyecto con Gradle y ejecutar las pruebas unitarias.
2. **SonarCloud:** Este trabajo depende del trabajo anterior y se encarga de realizar el análisis de código con SonarCloud. 
Para ello, utiliza la acción jinguji/sonarcloud-gradle-github-action@v2, que se encarga de ejecutar el análisis de código y reportar los resultados a SonarCloud.


Para que este flujo de trabajo funcione correctamente, configuramos las siguientes variables de entorno en los secretos de tu repositorio de GitHub:

- **SONAR_TOKEN:** El token de acceso para SonarCloud.
- **GITHUB_TOKEN:** El token de acceso para GitHub, que es necesario para que la acción de SonarCloud pueda interactuar con GitHub.

Este sería el resultado del flujo de trabajo en GitHub Actions:
![GitHub Actions](multimedia/github-actions.png)


Y este sería el resultado del análisis de código en SonarCloud:
![SonarCloud](multimedia/sonarcloud.png)

---
## Step Adicional