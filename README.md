## Laboratorio #4 – REST API Blueprints (Java 21 / Spring Boot 3.3.x)
# Escuela Colombiana de Ingeniería – Arquitecturas de Software  

---

## 📋 Requisitos
- Java 21
- Maven 3.9+

## ▶️ Ejecución del proyecto
```bash
mvn clean install
mvn spring-boot:run
```
Probar con `curl`:
```bash
curl -s http://localhost:8080/blueprints | jq
curl -s http://localhost:8080/blueprints/john | jq
curl -s http://localhost:8080/blueprints/john/house | jq
curl -i -X POST http://localhost:8080/blueprints -H 'Content-Type: application/json' -d '{ "author":"john","name":"kitchen","points":[{"x":1,"y":1},{"x":2,"y":2}] }'
curl -i -X PUT  http://localhost:8080/blueprints/john/kitchen/points -H 'Content-Type: application/json' -d '{ "x":3,"y":3 }'
```

> Si deseas activar filtros de puntos (reducción de redundancia, *undersampling*, etc.), implementa nuevas clases que implementen `BlueprintsFilter` y cámbialas por `IdentityFilter` con `@Primary` o usando configuración de Spring.
---

Abrir en navegador:  
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
- OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)  

---

## 🗂️ Estructura de carpetas (arquitectura)

```
src/main/java/edu/eci/arsw/blueprints
  ├── model/         # Entidades de dominio: Blueprint, Point
  ├── persistence/   # Interfaz + repositorios (InMemory, Postgres)
  │    └── impl/     # Implementaciones concretas
  ├── services/      # Lógica de negocio y orquestación
  ├── filters/       # Filtros de procesamiento (Identity, Redundancy, Undersampling)
  ├── controllers/   # REST Controllers (BlueprintsAPIController)
  └── config/        # Configuración (Swagger/OpenAPI, etc.)
```

> Esta separación sigue el patrón **capas lógicas** (modelo, persistencia, servicios, controladores), facilitando la extensión hacia nuevas tecnologías o fuentes de datos.

---

## 📖 Actividades del laboratorio

### 1. Familiarización con el código base
- Revisa el paquete `model` con las clases `Blueprint` y `Point`.  
- Entiende la capa `persistence` con `InMemoryBlueprintPersistence`.  
- Analiza la capa `services` (`BlueprintsServices`) y el controlador `BlueprintsAPIController`.

### 2. Migración a persistencia en PostgreSQL
- Configura una base de datos PostgreSQL (puedes usar Docker).  
- Implementa un nuevo repositorio `PostgresBlueprintPersistence` que reemplace la versión en memoria.  
- Mantén el contrato de la interfaz `BlueprintPersistence`.  

### 3. Buenas prácticas de API REST
- Cambia el path base de los controladores a `/api/v1/blueprints`.  
- Usa **códigos HTTP** correctos:  
  - `200 OK` (consultas exitosas).  
  - `201 Created` (creación).  
  - `202 Accepted` (actualizaciones).  
  - `400 Bad Request` (datos inválidos).  
  - `404 Not Found` (recurso inexistente).  
- Implementa una clase genérica de respuesta uniforme:
  ```java
  public record ApiResponse<T>(int code, String message, T data) {}
  ```
  Ejemplo JSON:
  ```json
  {
    "code": 200,
    "message": "execute ok",
    "data": { "author": "john", "name": "house", "points": [...] }
  }
  ```

### 4. OpenAPI / Swagger
- Configura `springdoc-openapi` en el proyecto.  
- Expón documentación automática en `/swagger-ui.html`.  
- Anota endpoints con `@Operation` y `@ApiResponse`.

### 5. Filtros de *Blueprints*
- Implementa filtros:
  - **RedundancyFilter**: elimina puntos duplicados consecutivos.  
  - **UndersamplingFilter**: conserva 1 de cada 2 puntos.  
- Activa los filtros mediante perfiles de Spring (`redundancy`, `undersampling`).  

---

## ✅ Entregables

1. Repositorio en GitHub con:  
   - Código fuente actualizado.  
   - Configuración PostgreSQL (`application.yml` o script SQL).  
   - Swagger/OpenAPI habilitado.  
   - Clase `ApiResponse<T>` implementada.  

2. Documentación:  
   - Informe de laboratorio con instrucciones claras.  
   - Evidencia de consultas en Swagger UI y evidencia de mensajes en la base de datos.  
   - Breve explicación de buenas prácticas aplicadas.  

---

## 📊 Criterios de evaluación

| Criterio | Peso |
|----------|------|
| Diseño de API (versionamiento, DTOs, ApiResponse) | 25% |
| Migración a PostgreSQL (repositorio y persistencia correcta) | 25% |
| Uso correcto de códigos HTTP y control de errores | 20% |
| Documentación con OpenAPI/Swagger + README | 15% |
| Pruebas básicas (unitarias o de integración) | 15% |

**Bonus**:  

- Imagen de contenedor (`spring-boot:build-image`).  
- Métricas con Actuator.  

---
# REPORTE DE LABORATORIO
---
### INTEGRANTES:  
      - Laura Alejandra Venegas Piraban  
      - Sergio Alejandro Idarraga Torres  

### 1. Familiarización con el código base
- Revisa el paquete `model` con las clases `Blueprint` y `Point`.  
**Respuesta:**  
      Lo primero que nos piden revisar es el paquete model que contiene las clases Blueprint y Point, este paquete es el que contiene las entidades principales del dominio. Primero tenemos la clase Point, esta representa un punto en el plano y tiene atributos *x* y *y*. Por otro lado Blueprint representa el dibujo en el plano compuesto por un conjunto de puntos.  
- Entiende la capa `persistence` con `InMemoryBlueprintPersistence`.  
**Respuesta:**
      La capa de persistencia es la que define como se guardan y se obtiene los datos, maneja excepciones si no encuentra un Blueprint y se maneja la lógica para evitar duplicados.  
      Como principal tenemos una interfaz llamada BlueprintPersistence, esta define los métodos para guardar, consultar y actualizar blueprints. También se definió InMemoryBlueprintPersistence la cual es la implementación concreta de la interfaz, la cual almacena daatos en memoria usando mapas o listas. Logramos observar que no persiste datos a una BD real.  
- Analiza la capa `services` (`BlueprintsServices`) y el controlador `BlueprintsAPIController`.  
**Respuestas:**
      En la capa de services tenemos BlueprintServices acá es donde se aplican reglas de negocio, validaciones, y se invocan filtros si es necesario. Ella maneja operaciones entre la persistencia u los controladores.  
      Por otro lado tenemos el controlador, en el encontramos BlueprintAPIController que es el controlador REST, este expone los métodos HTTP para que los clientes puedan interactuar con blueprints. Recibe peticiones, llama a los serviocios y retorna las respuestas.    


### 2. Migración a persistencia en PostgreSQL  

- Configura una base de datos PostgreSQL (puedes usar Docker).    
Para realizar este paso, levantamos una instancia de PostgreSQL utilizando Docker con el siguiente comando:  

*docker run --name blueprints-postgres -e POSTGRES_PASSWORD=blueprints123 -e POSTGRES_DB=blueprintsdb -p 5432:5432 -v postgres_data:/var/lib/postgresql/data -d postgres:16*  

Este comando crea y ejecuta un contenedor basado en la imagen oficial postgres:16, asignándole el nombre blueprints-postgres. Se configuran variables de entorno para definir la contraseña del usuario administrador (blueprints123) y la base de datos inicial. El mapeo del puerto 5432 permite acceder a la base de datos desde el entorno local, mientras que el volumen postgres_data garantiza la persistencia de la información almacenada. La opción -d ejecuta el contenedor en segundo plano. Con esta configuración se dispone de una instancia de PostgreSQL operativa en entorno local.  

<div align="center">
  <img src="img/DockerInicio" alt="Docker Inicio" style="max-width: 400px; display: block; margin: 0 auto;" />
</div>

<div align="center">
  <img src="img/DockerCorriendo" alt="Docker Corriendo" style="max-width: 400px; display: block; margin: 0 auto;" />
</div>

<div align="center">
  <img src="img/DockerDesktop" alt="Docker Desktop" style="max-width: 400px; display: block; margin: 0 auto;" />
</div>

 - Implementa un nuevo repositorio `PostgresBlueprintPersistence` que reemplace la versión en memoria.  
- Mantén el contrato de la interfaz `BlueprintPersistence`.   

Para estos puntos los pasos que seguimos fueron:  
1)	se creó la clase PostgresBlueprintPersistence, esta clase será la encargada de guardar la persistencia en nuestra base de datos de manera local, implementa la interfaz de BlueprintPersistence, asegurando que cumple el mismo contrato que la versión en memoria.  
2)	Se agregó la anotación @Repository y @Primary, la primera respectivamente es para que Spring la detecte como un componente de persistencia y la segunda para que Spring use esta implementación por defecto cuando inyecta la intefaz, reemplazando así la versión de memoria sin tener que eliminarla del código.  
3)	Se inyectó el repositorio JPA blueprintJpaRepository que extiende de JpaRepository y permite las operaciones CRUD sobre la entidad Blueprint en la base de datos.  
4)	Los métodos como saveBlueprint, getBlueprint, getBlueprintByAuthor, getAllBlueprints y addPoint fuerón implementados para operar sobre la base de datos usando JPA, en vez de estructuras de memoria.  
5)	Se configuró el archivo applicaton.prperties con los datos de conexión a PostgreSQL con URL, usuario, contraseña, driver y dialecto. Acá aseguramos qie la base de datos estuviera corriendo.  
6)	Se revisaron las clases Blueprint y Point para que estén correctamente anotadas con el @Entity y tengan los mapeos necesarios para JPA como los IDs, las relaciones, contructores, vacíos y getters/setters.  

Con esto losgramos que todas las operaciones de perssitencia de blueprints se realizaran en la base de datos configurada, cumpliendo el contrato de la interfaz y reemplazando completamente la versión en memoria.  

A continuación se muestran las pruebas que hicimos para verificar que estuviera funcionando cada una de las implementaciones que realizamos. Las pruebas de los verbos HTTP las realizamos en ThunderClient, el cual consideramos más sencillo, y luego en la terminal verificamos los datos de la base de datos.  

Entonces corremos los siguente comando en la terminal:
  - mvn clean install
  - mvn spring-boot:run
Luego de esto abrimos thunderClient hacemos las pruebas HTTP y en una nueva terminal para cada prueba corriamos:  
  - docker exec -it blueprints-postgres psql -U blueuser -d blueprintsdb  
Ingresamos con la contraseña: blueprints123  
Ejecutamos el comando SQL SELECT * FROM blueprint y as tenemos trazabilidad de lo que esta pasando en la base de datos.  

1)	Crear un nuevo blueprint (POST)  
Método: POST  
URL: http://localhost:8080/blueprints  
Body:  
{
  "author": "john",
  "name": "kitchen",
  "points": [
    { "x": 1, "y": 1 },
    { "x": 2, "y": 2 }
  ]
}  
Resultado:  
<div align="center">
  <img src="img/PruebaPOST" alt="Prueba POST" style="max-width: 400px; display: block; margin: 0 auto;" />
</div>
  
Cambio en la BD:  
<div align="center">
  <img src="img/PruebaBD" alt="Prueba BD" style="max-width: 400px; display: block; margin: 0 auto;" />
</div>

2)	Obtener todos los blueprints   
Método: GET  
URL: http://localhost:8080/blueprints   
Resultado:  
<div align="center">
  <img src="img/PruebaGET" alt="Prueba GET" style="max-width: 400px; display: block; margin: 0 auto;" />
</div>  

3)	Obtener los blueprints de un autor.  
Método: GET  
URL: http://localhost:8080/blueprints/john   
Respuesta:  
<div align="center">
  <img src="img/PruebaGET-john" alt="Prueba GET john" style="max-width: 400px; display: block; margin: 0 auto;" />
</div>  

4)	Obtener un blueprint específico  
Método: GET  
URL: http://localhost:8080/blueprints/john/kitchen   
Respuesta:  
<div align="center">
  <img src="img/PruebaGET-Kitchen" alt="Prueba GET Kitchen" style="max-width: 400px; display: block; margin: 0 auto;" />
</div>  

5)	Agregar un punto a un blueprint  
Método: PUT  
URL: http://localhost:8080/blueprints/john/kitchen/points  
Body:   
{ "x": 3, "y": 3 }  
Respuesta:   
<div align="center">
  <img src="img/Prueba-Points" alt="Prueba Points" style="max-width: 400px; display: block; margin: 0 auto;" />
</div>