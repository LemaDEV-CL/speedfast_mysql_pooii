# Speed Fast

Proyecto de Java para **Programación Orientada a Objetos II**, de primer año de la carrera.

El sistema permite gestionar pedidos de SpeedFast. En la semana 7 conectamos las ventanas Swing con MySQL mediante JDBC para guardar los pedidos y las asignaciones.

## Avance por semana

- **Semana 1:** clases y objetos para representar pedidos.
- **Semana 2:** herencia, clase abstracta `Pedido` y cálculo de tiempos según el tipo.
- **Semana 3:** interfaces y `ControladorDeEnvios` para las acciones de los pedidos.
- **Semana 4:** repartidores con `Runnable` e hilos para simular entregas.
- **Semana 5:** cola compartida `ZonaDeCarga` y estados de los pedidos.
- **Semana 6:** ventanas con Swing, registro con validaciones, tabla de pedidos y asignación de repartidores. También se corrigió el ciclo del repartidor para terminar si recibe `null` y conservar la interrupción, según la retroalimentación del profe.
- **Semana 7:** conexión JDBC con MySQL, creación de BD y tablas, DAO, IDs autogenerados y persistencia desde Swing.

## Funcionamiento actual

1. Al iniciar se abre la ventana principal con tres botones.
2. En **Registrar pedido** se ingresan cliente, dirección, distancia y tipo, además de los campos propios de comida, encomienda o express.
3. El formulario valida los datos y el controlador guarda el pedido mediante `PedidoDAO`. MySQL genera el ID y el pedido comienza en `PENDIENTE`.
4. En **Listar pedidos** se consultan ID, dirección, tipo, estado y repartidor desde MySQL. El botón **Actualizar** refresca la tabla.
5. En **Asignar repartidor / Iniciar entrega** se elige un pedido pendiente y un repartidor de la BD. `EntregaDAO` guarda la asignación con fecha y hora, y `PedidoDAO` actualiza el estado a `EN_REPARTO`.

Los repartidores iniciales son Iván, Luis y Pedro. Un pedido sin entrega aparece como **Sin asignar** y, al asignarlo, deja de aparecer entre los pendientes.

Los datos guardados en MySQL se conservan al cerrar la aplicación. Por ahora, del pedido se guardan ID, dirección, tipo y estado; cliente, distancia y campos adicionales del formulario no se guardan en la BD.

## Organización del proyecto

Todos los paquetes están dentro de `src/main/java/cl/lema`:

| Paquete | Contenido |
|---------|-----------|
| `app` | `Main`, que abre la ventana principal. |
| `conexion` | `ConexionBD`, conexión JDBC e inicialización de las tablas. |
| `dao` | `PedidoDAO`, `RepartidorDAO` y `EntregaDAO`, con las operaciones sobre MySQL. |
| `models` | `Pedido`, sus tres tipos y `EstadoPedido`. |
| `servicio` | `ControladorDeEnvios` y `ZonaDeCarga`. |
| `vista` | Ventanas principal, de registro, de listado y de asignación. |
| `hilos` | `Repartidor`, usado en la asignación y en la simulación anterior. |
| `interfaces` | `Asignable`, `Despachable`, `Cancelable` y `Rastreable`. |

El registro pasa por `ControladorDeEnvios` y `PedidoDAO`. Las ventanas de listado y asignación consultan directamente los DAO. La simulación con hilos y el historial en memoria se conservan de semanas anteriores.

## Base de datos de la semana 7

- `ConexionBD` usa JDBC y MySQL Connector/J, incluido en `pom.xml`.
- En la primera conexión se crea `speedfast` si no existe y se crean las tablas `pedido`, `repartidor` y `entrega` con `CREATE TABLE IF NOT EXISTS`.
- Las tres tablas usan IDs `AUTO_INCREMENT`. Ya no se ingresa el ID al registrar un pedido.
- `PedidoDAO` guarda y lista pedidos, consulta pendientes y actualiza estados.
- `RepartidorDAO` consulta los repartidores y `EntregaDAO` guarda la relación entre pedido y repartidor, con fecha y hora.

## Requisitos y ejecución

- Java 23 y Maven.
- MySQL iniciado en `localhost:3306`.
- IntelliJ IDEA u otro IDE para Java.

Revisar `URL`, `USER` y `PASSWORD` en `ConexionBD.java` según la instalación de MySQL. El usuario debe existir y tener permisos para crear la BD y las tablas, consultar, insertar y actualizar datos. No es necesario crear las tablas manualmente.

En IntelliJ, abrir el proyecto Maven, cargar las dependencias y ejecutar `Main` del paquete `cl.lema.app`.

Desde PowerShell, en la carpeta donde está `pom.xml`:

```powershell
mvn compile
mvn exec:java "-Dexec.mainClass=cl.lema.app.Main"
```

## Comprobación manual

- Registrar un pedido de cada tipo y comprobar el ID generado.
- Probar campos vacíos y números incorrectos.
- Actualizar el listado después de registrar o asignar.
- Asignar un repartidor y comprobar su nombre y el estado `EN_REPARTO`.
- Cerrar y abrir la aplicación para comprobar que los datos guardados siguen en el listado.

## Clonar el repositorio

```bash
git clone https://github.com/LemaDEV-CL/speed-fast.git
cd speed-fast
```
