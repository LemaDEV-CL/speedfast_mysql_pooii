# SpeedFast

Proyecto desarrollado en Java para la asignatura **Desarrollo Orientado a Objetos II**.

El sistema permite gestionar de forma persistente los **repartidores, pedidos y entregas** de la empresa SpeedFast mediante una interfaz gráfica Swing conectada a MySQL con JDBC.

## Semana 8 - Operaciones CRUD

En esta etapa se completa el ciclo funcional de la aplicación implementando operaciones CRUD para las principales entidades del sistema:

- **Repartidores:** crear, listar, editar y eliminar.
- **Pedidos:** crear, listar, editar y eliminar.
- **Entregas:** crear, listar, editar y eliminar, asociando un pedido con un repartidor.

La información se almacena en MySQL y se mantiene disponible al cerrar y volver a abrir la aplicación.

## Funcionalidades principales

Al ejecutar el programa se abre una ventana principal con acceso a tres módulos:

### Gestión de Repartidores

Permite:

- Registrar un repartidor por nombre.
- Listar los repartidores almacenados en MySQL.
- Seleccionar y editar un repartidor.
- Eliminar un repartidor.
- Validar que el nombre no esté vacío antes de guardar o editar.

### Gestión de Pedidos

Permite:

- Registrar un pedido indicando dirección, tipo y estado.
- Tipos disponibles:
    - `COMIDA`
    - `ENCOMIENDA`
    - `EXPRESS`
- Estados disponibles:
    - `PENDIENTE`
    - `EN_REPARTO`
    - `ENTREGADO`
- Listar pedidos en una `JTable`.
- Editar pedidos existentes.
- Eliminar pedidos cuando no existan relaciones que lo impidan.

### Gestión de Entregas

Permite:

- Seleccionar un pedido desde un `JComboBox` cargado desde la base de datos.
- Seleccionar un repartidor desde un `JComboBox` cargado desde la base de datos.
- Registrar fecha y hora de la entrega.
- Listar las entregas almacenadas.
- Editar una entrega existente.
- Eliminar una entrega.
- Actualizar el estado del pedido a `EN_REPARTO` al registrar una entrega.
- Validar el formato de fecha y hora antes de guardar o modificar datos.

## Arquitectura del proyecto

El proyecto está organizado por responsabilidades dentro de `src/main/java/cl/lema`:

| Paquete | Responsabilidad |
|---|---|
| `app` | Contiene `Main`, punto de inicio de la aplicación. |
| `conexion` | Contiene `ConexionBD`, encargada de la conexión JDBC y creación de las tablas. |
| `dao` | Contiene los DAO de `Pedido`, `Repartidor` y `Entrega`, con operaciones CRUD sobre MySQL. |
| `models` | Contiene las entidades y modelos del sistema. |
| `vista` | Contiene las ventanas Swing utilizadas por el usuario. |
| `servicio` | Conserva lógica desarrollada en semanas anteriores. |
| `hilos` | Conserva la implementación de repartidores mediante `Runnable` desarrollada anteriormente. |
| `interfaces` | Contiene las interfaces utilizadas por los pedidos y funcionalidades de semanas anteriores. |

## Persistencia y DAO

Cada entidad principal cuenta con una clase DAO encargada de acceder a la base de datos mediante JDBC.

### RepartidorDAO

- `create()`
- `readAll()`
- `update()`
- `delete()`

### PedidoDAO

- `create()`
- `readAll()`
- `update()`
- `delete()`

También conserva métodos auxiliares utilizados por funcionalidades anteriores, como la consulta de pedidos pendientes y la actualización del estado.

### EntregaDAO

- `create()`
- `readAll()`
- `update()`
- `delete()`

Los DAO utilizan `PreparedStatement`, `ResultSet` y bloques `try-with-resources` para manejar correctamente conexiones y recursos JDBC.

## Base de datos

La aplicación utiliza la base de datos:

```text
speedfast
```

La conexión JDBC está configurada en `ConexionBD.java` con:

```text
jdbc:mysql://localhost:3306/speedfast?createDatabaseIfNotExist=true
```

Al iniciar la aplicación, si la base de datos o las tablas no existen, se crean automáticamente mediante `CREATE TABLE IF NOT EXISTS`.

### Tabla repartidor

```text
id      INT AUTO_INCREMENT PRIMARY KEY
nombre  VARCHAR(100) NOT NULL UNIQUE
```

### Tabla pedido

```text
id         INT AUTO_INCREMENT PRIMARY KEY
direccion  VARCHAR(150) NOT NULL
tipo       VARCHAR(30) NOT NULL
estado     VARCHAR(20) NOT NULL
```

### Tabla entrega

```text
id              INT AUTO_INCREMENT PRIMARY KEY
id_pedido       INT NOT NULL
id_repartidor   INT NOT NULL
fecha           DATE NOT NULL
hora            TIME NOT NULL
```

`entrega` utiliza claves foráneas hacia `pedido` y `repartidor` para mantener la integridad referencial.

La aplicación no crea repartidores por defecto. Los datos se ingresan desde las ventanas CRUD para permitir que la simulación comience con una base de datos vacía.

## Interfaz gráfica

La aplicación utiliza Java Swing y componentes como:

- `JFrame`
- `JPanel`
- `JTable`
- `JTextField`
- `JComboBox`
- `JButton`
- `JOptionPane`

Las tablas se actualizan después de las operaciones CRUD y los formularios realizan validaciones antes de enviar información a los DAO.

## Requisitos

- Java 23.
- Maven.
- MySQL disponible en `localhost:3306`.
- MySQL Connector/J 9.3.0, administrado mediante Maven.
- IntelliJ IDEA u otro IDE compatible con proyectos Maven.

La configuración actual de conexión se encuentra en `ConexionBD.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/speedfast?createDatabaseIfNotExist=true";
private static final String USER = "speedadmin";
private static final String PASSWORD = "admin1234";
```

El usuario de MySQL debe tener permisos para crear la base de datos y las tablas, además de consultar, insertar, modificar y eliminar registros.

## Ejecución

En IntelliJ IDEA:

1. Abrir el proyecto como proyecto Maven.
2. Esperar que Maven cargue las dependencias.
3. Verificar que MySQL esté iniciado.
4. Ejecutar la clase:

```text
cl.lema.app.Main
```

También se puede comprobar la compilación desde la carpeta donde se encuentra `pom.xml`:

```powershell
mvn clean compile
```

## Prueba funcional

Para comprobar el funcionamiento completo de la aplicación:

1. Iniciar con la base de datos vacía.
2. Crear uno o más repartidores.
3. Crear uno o más pedidos.
4. Registrar una entrega seleccionando pedido y repartidor.
5. Editar registros de las tres entidades.
6. Eliminar registros que no tengan restricciones de claves foráneas.
7. Cerrar la aplicación.
8. Abrirla nuevamente y comprobar que los datos continúan almacenados en MySQL.

## Control de versiones

El proyecto se gestiona mediante Git y GitHub. Para la entrega se recomienda mantener commits separados y descriptivos que evidencien el avance de la Semana 8, por ejemplo:

```text
CRUD repartidores Semana 8
CRUD pedidos Semana 8
CRUD entregas Semana 8
Integración Swing CRUD Semana 8
Validaciones y ajustes finales Semana 8
Documentación Semana 8
```

## Repositorio

```bash
git clone https://github.com/LemaDEV-CL/speed-fast.git
cd speed-fast
```
