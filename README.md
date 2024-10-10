## **1. Estructura General de una Aplicación Spring Boot**
### **/controllers**
>Recibe la petición y ejecuta la lógica principal, Maneja las solicitudes HTTP y define los endpoints de la API.
* UsuarioController.java
### **/services**
>Acciones permitidas en el sistema, Contiene la lógica de negocio de la aplicación.
* UsuarioService.java*
### **/repositories**
>Consultas sql o métodos de mapeo de objetos, Interactúa con la base de datos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar).
* UsuarioRepository.java*
### **/models**
>Representación de una tabla en una clase java
* UsuarioModel.java*
---
## **2. Descripción de los Componentes**
>Este es el corazón de tus datos. Representa la entidad `Usuario` que se almacenará en la base de datos.
### Modelo (Model): UsuarioModel*
```java
package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "USUARIO")
public class UsuarioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String name;
    private String job;
    private Double salary;
    private Double commision;
    private Integer prioridad;

    // Getters y Setters
    // ...
}
```
**Conceptos Clave:**

- **@Entity:** Indica que esta clase es una entidad JPA y se mapeará a una tabla en la base de datos.
- **@Table(name = "USUARIO"):** Especifica el nombre de la tabla en la base de datos.
- **@Id:** Define el campo `id` como la clave primaria.
- **@GeneratedValue(strategy = GenerationType.IDENTITY):** Configura cómo se generará el valor del `id` (auto-incremental en este caso).
- **Campos:** `name`, `job`, `salary`, `commision`, `prioridad` representan las columnas de la tabla.

**Getters y Setters:** Métodos para acceder y modificar los atributos privados de la clase.

### Repositorio (Repository): UsuarioRepository*
>Esta interfaz maneja la interacción con la base de datos.
```java
package com.example.demo.repositories;

import com.example.demo.model.UsuarioModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UsuarioRepository extends CrudRepository<UsuarioModel, Long> {
    public abstract ArrayList<UsuarioModel> findByPrioridad(Integer prioridad);
}
```
**Conceptos Clave:**

- **CrudRepository<UsuarioModel, Long>:** Proporciona métodos CRUD básicos (como `save`, `findById`, `findAll`, `deleteById`) para la entidad `UsuarioModel` con clave primaria de tipo `Long`.
- **@Repository:** Indica que esta interfaz es un componente de repositorio de Spring.
- **Método Personalizado:** `findByPrioridad(Integer prioridad)` permite buscar usuarios según su prioridad.

### Servicio (Service) UsuarioService
>Esta clase contiene la lógica de negocio y actúa como intermediaria entre el controlador y el repositorio.
```java
package com.example.demo.services;

import com.example.demo.model.UsuarioModel;
import com.example.demo.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;

    public ArrayList<UsuarioModel> obtenerUsuarios(){
        return (ArrayList<UsuarioModel>) usuarioRepository.findAll();
    }

    public UsuarioModel guardarUsuario(UsuarioModel usuario){
        return usuarioRepository.save(usuario);
    }

    public Optional<UsuarioModel> obtenerUsuarioPorId(Long id){
        return usuarioRepository.findById(id);
    }

    public ArrayList<UsuarioModel> obtenerPorPrioridad(Integer prioridad){
        return usuarioRepository.findByPrioridad(prioridad);
    }

    public boolean eliminarUsuario(Long id){
        try{
            usuarioRepository.deleteById(id);
            return true;
        }catch (Exception err){
            return false;
        }
    }
}
```
**Conceptos Clave:**

- **@Service:** Indica que esta clase es un servicio de Spring, gestionando la lógica de negocio.
- **@Autowired:** Inyecta automáticamente una instancia de `UsuarioRepository` para que puedas usarla sin necesidad de crear una nueva instancia manualmente.
- **Métodos:**
    - `obtenerUsuarios()`: Recupera todos los usuarios.
    - `guardarUsuario(UsuarioModel usuario)`: Guarda un nuevo usuario en la base de datos.
    - `obtenerUsuarioPorId(Long id)`: Busca un usuario por su ID.
    - `obtenerPorPrioridad(Integer prioridad)`: Busca usuarios según su prioridad.
    - `eliminarUsuario(Long id)`: Elimina un usuario por su ID y devuelve `true` si la operación fue exitosa.
### Controlador (Controller): UsuarioController

>Este componente maneja las solicitudes HTTP y define los endpoints de tu API REST.
```java
package com.example.demo.controller;

import com.example.demo.model.UsuarioModel;
import com.example.demo.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

    @GetMapping()
    public ArrayList<UsuarioModel> obtenerUsuarios(){
        return usuarioService.obtenerUsuarios();
    }

    @PostMapping()
    public UsuarioModel guardarUsuario(@RequestBody UsuarioModel usuario){
        return this.usuarioService.guardarUsuario(usuario);
    }

    @GetMapping(path = "/{id}")
    public Optional<UsuarioModel> obtenerUsuarioPorId(@PathVariable("id") Long id){
        return this.usuarioService.obtenerUsuarioPorId(id);
    }

    @GetMapping("/query")
    public ArrayList<UsuarioModel> obtenerPorPrioridad(@RequestParam("prioridad") Integer prioridad){
        return this.usuarioService.obtenerPorPrioridad(prioridad);
    }

    @DeleteMapping(path = "/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id){
        boolean ok = this.usuarioService.eliminarUsuario(id);
        if (ok){
            return "Se elimino el usuario con id: " + id;
        } else{
            return "No se pudo eliminar el usuario con id: " + id;
        }
    }
}
```
**Conceptos Clave:**

- **@RestController:** Indica que esta clase es un controlador donde cada método devuelve un objeto de dominio en lugar de una vista. Es una combinación de `@Controller` y `@ResponseBody`.
- **@RequestMapping("/usuario"):** Define la ruta base para todos los endpoints dentro de este controlador. Por ejemplo, todos los endpoints comenzarán con `http://localhost:8080/usuario`.
- **@Autowired:** Inyecta automáticamente una instancia de `UsuarioService`.
- **Endpoints:**
    - **GET `/usuario`:** Recupera todos los usuarios.
    - **POST `/usuario`:** Crea un nuevo usuario.
    - **GET `/usuario/{id}`:** Recupera un usuario específico por su ID.
    - **GET `/usuario/query?prioridad=valor`:** Recupera usuarios filtrados por prioridad.
    - **DELETE `/usuario/{id}`:** Elimina un usuario por su ID.
---
## **3. Conceptos Fundamentales de Spring Boot Utilizados**

### Inyección de Dependencias con `@Autowired`
>Spring Boot gestiona los componentes de tu aplicación y sus dependencias automáticamente. La anotación `@Autowired` permite que Spring inyecte instancias de clases en otras clases sin necesidad de crearlas manualmente.

Ejemplo: 
```java
@Autowired
UsuarioService usuarioService;
```

### Controladores REST con `@RestController`
>`@RestController` es una especialización de `@Controller` que simplifica la creación de APIs REST. Combina `@Controller` y `@ResponseBody`, lo que significa que cada método del controlador devuelve directamente datos en lugar de una vista.

### Manejo de Datos con JPA y Repositorios
>Spring Data JPA facilita la interacción con la base de datos utilizando repositorios. Extender `CrudRepository` proporciona métodos CRUD básicos sin necesidad de implementarlos manualmente.
* **Personalización de Consultas:** Puedes definir métodos personalizados siguiendo convenciones de nomenclatura, como `findByPrioridad(Integer prioridad)`.*
---
## **Diferencias entre @RestController y @Controller**

### @RestController:
* Descripción: Es una anotación de conveniencia que combina `@Controller` y `@ResponseBody`  Indica que el controlador maneja solicitudes REST y que los métodos devuelvan directamente los cuerpos de las respuestas (generalmente en formato JSON).  
* Uso Típico: APIs RESTful donde se espera que los métodos retornen objetos que se serializan automáticamente a JSON o XML.

### @Controller:
* Descripción: Marca la clase como un controlador de Spring MVC tradicional. Requiere que especifiques como se manejan las respuestas, ya sea retornando vistas (por ejemplo, paginas HTML) o manipulando directamente el `httpServletResponse`.*
* Uso Típico: Aplicaciones web que sirven paginas HTML, donde se necesitan renderizar vistas usando tecnologías como Thymeleaf, JSP, etc. Tambien util cuando se requiere un control mas granular sobre la respuestas HTTP. 
---
### DAO (Data Access Object):

**Definición**: El patrón DAO es un enfoque de diseño que proporciona una interfaz abstracta para la persistencia de datos, permitiendo desacoplar la lógica de acceso a datos de la lógica de negocio.
**Ventajas**:
- **Control Total**: Tienes un control total sobre las consultas SQL, lo que te permite optimizar y personalizar las operaciones de acceso a datos.
- **Flexibilidad**: Puedes usar diferentes fuentes de datos o implementaciones (por ejemplo, bases de datos relacionales, NoSQL) sin afectar a la lógica de negocio.
- **Simplicidad**: Para aplicaciones pequeñas o con operaciones de acceso a datos simples, el patrón DAO puede ser más fácil de entender y mantener.
**Desventajas**:
- **Código Boilerplate**: Puede requerir más código, ya que necesitas implementar manualmente las consultas y la lógica de acceso a datos.
- **Mayor Complejidad en Consultas Complejas**: Las consultas SQL complejas pueden volverse difíciles de gestionar y mantener.
### JPA (Java Persistence API):

**Definición**: JPA es una especificación para la gestión de la persistencia en Java. Permite a los desarrolladores trabajar con objetos Java y realizar operaciones CRUD sin tener que escribir consultas SQL manualmente.
**Ventajas**:
- **Productividad**: Reduce la cantidad de código que necesitas escribir, ya que JPA se encarga de generar consultas SQL automáticamente basadas en las entidades.
- **Manejo de Relaciones**: Facilita el manejo de relaciones entre entidades (uno a uno, uno a muchos, muchos a muchos) sin necesidad de escribir código adicional.
- **Integración con Spring**: Spring Data JPA ofrece características adicionales, como repositorios, que simplifican aún más el acceso a datos.
**Desventajas**:
- **Menor Control**: Aunque puedes personalizar las consultas, JPA puede ocultar detalles de la consulta SQL que se genera, lo que puede dificultar la optimización en algunos casos.
- **Curva de Aprendizaje**: Para los desarrolladores que están acostumbrados a trabajar directamente con SQL, JPA puede tener una curva de aprendizaje más pronunciada.
### Conclusión

- Si estás comenzando y buscas una solución más simple y rápida, **JPA** es probablemente la mejor opción. Te permitirá centrarte más en el desarrollo de la lógica de negocio sin preocuparte tanto por las consultas SQL.
- Si tienes necesidades específicas de optimización o si tu aplicación requiere un control total sobre el acceso a datos, **DAO** podría ser más adecuado.
