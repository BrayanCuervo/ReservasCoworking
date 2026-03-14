# 🏢 Sistema de Reservas COWORKING — parcialUno

Aplicación web Java EE para gestionar reservas de espacios de coworking.  
Permite **crear**, **listar** y **eliminar** reservas, con persistencia en base de datos MySQL.

---

## 📋 Tabla de Contenidos

1. [Stack tecnológico](#stack-tecnológico)
2. [Estructura del proyecto](#estructura-del-proyecto)
3. [Requisitos previos](#requisitos-previos)
4. [Configuración de la base de datos (XAMPP)](#configuración-de-la-base-de-datos-xampp)
5. [Configuración del proyecto en NetBeans 24](#configuración-del-proyecto-en-netbeans-24)
6. [Despliegue en Apache Tomcat 11](#despliegue-en-apache-tomcat-11)
7. [Descripción de las capas del proyecto](#descripción-de-las-capas-del-proyecto)
8. [Flujo de la aplicación](#flujo-de-la-aplicación)
9. [Endpoints / Servlets](#endpoints--servlets)
10. [Diagrama de la arquitectura](#diagrama-de-la-arquitectura)
11. [Solución de problemas frecuentes](#solución-de-problemas-frecuentes)

---

## Stack tecnológico

| Componente        | Versión     |
|-------------------|-------------|
| Java (JDK)        | 21          |
| Jakarta EE API    | 11.0.0      |
| JPA Provider      | EclipseLink 4.0.2 |
| Servidor web      | Apache Tomcat 11 |
| Base de datos     | MySQL 8.x (vía XAMPP) |
| IDE               | NetBeans 24 |
| Build tool        | Maven 3.x   |
| Driver JDBC       | mysql-connector-j 8.3.0 |

---

## Estructura del proyecto

```
parcialUno/
├── pom.xml                                          ← Dependencias Maven
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── com/ucompensar/parcialuno/serverlets/
    │   │   │   ├── SvReserva.java                  ← Servlet: guardar reserva
    │   │   │   ├── SvListaReservas.java             ← Servlet: listar reservas
    │   │   │   └── SvEliminarReserva.java           ← Servlet: eliminar reserva
    │   │   ├── logica/
    │   │   │   └── Reserva.java                    ← Entidad JPA (tabla DB)
    │   │   └── persistencia/
    │   │       ├── ReservaJpaController.java        ← CRUD JPA
    │   │       └── exceptions/
    │   │           ├── PreexistingEntityException.java
    │   │           ├── IllegalOrphanException.java
    │   │           └── NonexistentEntityException.java
    │   ├── resources/
    │   │   └── META-INF/
    │   │       └── persistence.xml                 ← Configuración JPA/BD
    │   └── webapp/
    │       ├── index.jsp                           ← Formulario de reserva
    │       ├── confirmacion_reserva.jsp            ← Confirmación tras guardar
    │       ├── lista_reservas.jsp                  ← Tabla de reservas
    │       └── style.css                           ← Estilos globales
    └── test/
        └── java/
```

---

## Requisitos previos

Antes de comenzar, asegúrate de tener instalado lo siguiente:

### 1. JDK 21
- Descarga desde: https://adoptium.net/ (Temurin 21 LTS) o https://www.oracle.com/java/technologies/downloads/#java21
- Instala y agrega `JAVA_HOME` a las variables de entorno del sistema.
- Verifica con:
  ```bash
  java -version
  # Debe mostrar: openjdk version "21.x.x"
  ```

### 2. Apache Tomcat 11
- Descarga desde: https://tomcat.apache.org/download-11.cgi
- Descomprime en una carpeta, por ejemplo `C:\tomcat11` (Windows) o `/opt/tomcat11` (Linux/Mac).
- **Importante:** Tomcat 11 requiere JDK 11 como mínimo, pero funciona perfectamente con JDK 21.

### 3. XAMPP (MySQL + phpMyAdmin)
- Descarga desde: https://www.apachefriends.org/
- Instala y asegúrate de que el módulo **MySQL** esté activo.
- El puerto por defecto de MySQL en XAMPP es **3306**.

### 4. NetBeans 24
- Descarga desde: https://netbeans.apache.org/front/main/download/
- Selecciona la distribución que incluye JDK o instálala por separado.

### 5. Maven
- NetBeans 24 incluye Maven integrado. Si lo usas desde línea de comandos, descarga desde: https://maven.apache.org/download.cgi

---

## Configuración de la base de datos (XAMPP)

### Paso 1: Iniciar XAMPP

1. Abre el **XAMPP Control Panel**.
2. Haz clic en **Start** junto a **MySQL**.
3. Verifica que el puerto sea `3306` (aparece en la columna "Port").

### Paso 2: Crear la base de datos en phpMyAdmin

1. Abre tu navegador y ve a: `http://localhost/phpmyadmin`
2. En el panel izquierdo, haz clic en **Nueva** (o "New").
3. En el campo **Nombre de la base de datos** escribe exactamente:
   ```
   parcialuno
   ```
   > ⚠️ El nombre debe ser en minúsculas y sin tildes. El `persistence.xml` apunta a `jdbc:mysql://localhost:3306/parcialuno`.
4. Cotejamiento (collation): selecciona `utf8mb4_general_ci`.
5. Haz clic en **Crear**.

### Paso 3: Verificar el usuario de la BD

El `persistence.xml` usa:
```xml
<property name="jakarta.persistence.jdbc.user" value="root"/>
<property name="jakarta.persistence.jdbc.password" value=""/>
```
Por defecto XAMPP tiene usuario `root` sin contraseña. Si tu instalación tiene contraseña, actualiza el `persistence.xml` con el valor correcto.

### Paso 4: Creación automática de tablas

La propiedad de EclipseLink:
```xml
<property name="eclipselink.ddl-generation" value="create-or-extend-tables"/>
```
hace que **las tablas se creen automáticamente** la primera vez que la aplicación se conecte. No necesitas ejecutar ningún script SQL manualmente.

> ✅ Después del primer despliegue, en phpMyAdmin verás la tabla `RESERVA` (o el nombre mapeado en la entidad `Reserva.java`) con sus columnas.

---

## Configuración del proyecto en NetBeans 24

### Paso 1: Clonar / Abrir el proyecto

**Opción A — desde el ZIP:**
1. Extrae el archivo `parcialUno.zip` en una carpeta local.
2. En NetBeans: `File → Open Project`.
3. Navega hasta la carpeta extraída y selecciona el proyecto (verás el ícono de Maven).
4. Haz clic en **Open Project**.

**Opción B — desde cero (crear el proyecto):**
1. `File → New Project → Maven → Web Application`.
2. Nombre: `parcialUno`, Group ID: `com.ucompensar`, Version: `1.0-SNAPSHOT`.
3. Servidor: selecciona **Tomcat 11** (ver siguiente sección para registrarlo).
4. Copia los archivos fuente en sus paquetes correspondientes.

### Paso 2: Registrar Tomcat 11 en NetBeans

1. Ve a `Tools → Servers → Add Server`.
2. Selecciona **Apache Tomcat or TomEE**.
3. En **Server Location**, navega hasta donde descomprimiste Tomcat 11 (p. ej. `C:\tomcat11`).
4. Deja el puerto HTTP en **8080**.
5. Haz clic en **Finish**.

### Paso 3: Asociar el servidor al proyecto

1. Clic derecho en el proyecto → `Properties`.
2. Categoría **Run** → Server: selecciona **Apache Tomcat 11**.
3. Context Path: `/parcialUno` (o deja el que NetBeans asigne).

### Paso 4: Verificar dependencias Maven

NetBeans descargará las dependencias automáticamente al abrir el proyecto. Si no lo hace:
1. Clic derecho en el proyecto → `Build with Dependencies` o `Clean and Build`.
2. Revisa la pestaña **Output** para confirmar que no hay errores de descarga.

Las dependencias principales del `pom.xml` son:
- `jakarta.jakartaee-api:11.0.0` — scope `provided` (la provee Tomcat)
- `org.eclipse.persistence:eclipselink:4.0.2` — JPA provider
- `com.mysql:mysql-connector-j:8.3.0` — driver JDBC

---

## Despliegue en Apache Tomcat 11

### Opción A: Despliegue desde NetBeans (recomendado durante desarrollo)

1. Con XAMPP/MySQL corriendo, haz clic en el botón ▶ **Run** (F6) en NetBeans.
2. NetBeans compilará el proyecto, generará el WAR y lo desplegará automáticamente en Tomcat.
3. El navegador abrirá: `http://localhost:8080/prueba/` (el `finalName` en `pom.xml` es `prueba`).

### Opción B: Despliegue manual del WAR

1. En NetBeans: clic derecho en el proyecto → **Clean and Build**.
2. Localiza el archivo WAR generado en:
   ```
   tu-proyecto/target/prueba.war
   ```
3. Copia `prueba.war` a la carpeta `webapps/` de Tomcat:
   ```
   C:\tomcat11\webapps\prueba.war   (Windows)
   /opt/tomcat11/webapps/prueba.war (Linux/Mac)
   ```
4. Inicia Tomcat (si no está corriendo):
   ```bash
   # Windows
   C:\tomcat11\bin\startup.bat

   # Linux / Mac
   /opt/tomcat11/bin/startup.sh
   ```
5. Tomcat desplegará el WAR automáticamente. Accede en:
   ```
   http://localhost:8080/prueba/
   ```

### Opción C: Despliegue vía Tomcat Manager

1. Abre `http://localhost:8080/manager/html` (requiere usuario en `tomcat-users.xml`).
2. Sección **WAR file to deploy** → selecciona `prueba.war` → Deploy.

---

## Descripción de las capas del proyecto

### Capa de Vista (Web Pages)

| Archivo | Descripción |
|---------|-------------|
| `index.jsp` | Formulario HTML para capturar los datos de una nueva reserva: nombre del cliente, fecha, tipo de espacio (Mesa / Sala de reuniones / Salón de video) y duración en horas. Envía un `POST` al servlet `SvReserva`. |
| `confirmacion_reserva.jsp` | Página de confirmación que se muestra después de guardar exitosamente una reserva. Ofrece botones para registrar otra reserva o ver la lista. |
| `lista_reservas.jsp` | Tabla dinámica que itera sobre la lista de objetos `Reserva` inyectada por `SvListaReservas`. Muestra nombre, fecha, espacio, duración y un botón de eliminar por fila. |
| `style.css` | Hoja de estilos con fondo oscuro degradado, panel semitransparente, inputs y botones con colores naranja/verde/rojo. |

### Capa de Control (Servlets)

| Clase | URL | Método | Responsabilidad |
|-------|-----|--------|-----------------|
| `SvReserva.java` | `/SvReserva` | `POST` | Recibe los parámetros del formulario, construye un objeto `Reserva`, lo persiste via `ReservaJpaController.create()`, redirige a `confirmacion_reserva.jsp`. |
| `SvListaReservas.java` | `/SvListaReservas` | `GET` | Llama a `ReservaJpaController.findReservaEntities()`, pone la lista en el request con `setAttribute`, hace forward a `lista_reservas.jsp`. |
| `SvEliminarReserva.java` | `/SvEliminarReserva` | `GET` | Lee el parámetro `id` de la URL, llama a `ReservaJpaController.destroy(id)`, redirige a `SvListaReservas`. |

### Capa de Persistencia

| Clase | Descripción |
|-------|-------------|
| `logica/Reserva.java` | Entidad JPA anotada con `@Entity`. Atributos: `id` (PK auto), `nombre`, `fecha`, `espacio`, `duracion`. EclipseLink genera la tabla automáticamente. |
| `persistencia/ReservaJpaController.java` | Clase generada por NetBeans que encapsula las operaciones CRUD sobre la entidad `Reserva`: `create()`, `findReservaEntities()`, `destroy()`, `find()`. Usa `EntityManager` y transacciones locales. |
| `persistencia/exceptions/` | Excepciones personalizadas: `PreexistingEntityException`, `NonexistentEntityException`, `IllegalOrphanException`. Utilizadas por el JPA Controller. |
| `META-INF/persistence.xml` | Unidad de persistencia `parcialUnoPU`: define proveedor EclipseLink, la clase `Reserva`, la URL JDBC a MySQL y la generación automática de DDL. |

---

## Flujo de la aplicación

```
Usuario
  │
  ▼
index.jsp  ──POST──▶  SvReserva  ──create()──▶  ReservaJpaController  ──▶  MySQL (parcialuno)
                           │
                           └──redirect──▶  confirmacion_reserva.jsp

index.jsp  ──enlace──▶  SvListaReservas  ──findAll()──▶  ReservaJpaController  ──▶  MySQL
                              │
                              └──forward──▶  lista_reservas.jsp

lista_reservas.jsp  ──enlace──▶  SvEliminarReserva?id=X  ──destroy()──▶  ReservaJpaController  ──▶  MySQL
                                        │
                                        └──redirect──▶  SvListaReservas
```

---

## Endpoints / Servlets

| URL relativa                      | Método HTTP | Acción                              |
|-----------------------------------|-------------|-------------------------------------|
| `/prueba/`                        | GET         | Página principal (index.jsp)        |
| `/prueba/SvReserva`               | POST        | Guardar nueva reserva               |
| `/prueba/SvListaReservas`         | GET         | Ver listado de reservas             |
| `/prueba/SvEliminarReserva?id={n}`| GET         | Eliminar reserva con ID = n         |

---

## Diagrama de la arquitectura

```
┌─────────────────────────────────────────────────┐
│                  NAVEGADOR                       │
│  index.jsp / lista_reservas.jsp / confirmacion  │
└────────────────────┬────────────────────────────┘
                     │ HTTP
┌────────────────────▼────────────────────────────┐
│              APACHE TOMCAT 11                    │
│  ┌─────────────────────────────────────────┐    │
│  │           SERVLETS (Jakarta EE)         │    │
│  │  SvReserva | SvListaReservas |          │    │
│  │  SvEliminarReserva                      │    │
│  └──────────────────┬──────────────────────┘    │
│                     │                           │
│  ┌──────────────────▼──────────────────────┐    │
│  │         ReservaJpaController            │    │
│  │         (EclipseLink JPA 4.0)           │    │
│  └──────────────────┬──────────────────────┘    │
└─────────────────────┼───────────────────────────┘
                      │ JDBC (mysql-connector-j)
┌─────────────────────▼───────────────────────────┐
│         MYSQL 8.x — XAMPP (puerto 3306)          │
│         Base de datos: parcialuno                │
│         Tabla: RESERVA                           │
└─────────────────────────────────────────────────┘
```

---

## Solución de problemas frecuentes

### ❌ Error: "Communications link failure" al conectar a MySQL
**Causa:** XAMPP/MySQL no está corriendo.  
**Solución:** Abre XAMPP Control Panel → Start MySQL.

### ❌ Error: "Unknown database 'parcialuno'"
**Causa:** La base de datos no existe aún.  
**Solución:** Crea la BD en phpMyAdmin con el nombre exacto `parcialuno` (minúsculas).

### ❌ La aplicación se despliega pero las JSP no cargan el CSS
**Causa:** Ruta relativa del CSS. El `<link rel="stylesheet" href="style.css">` funciona si la JSP está en la raíz del webapp.  
**Solución:** Asegúrate de que `style.css` esté en `src/main/webapp/`.

### ❌ Error 404 al acceder a un Servlet
**Causa:** El WAR no contiene el Servlet o el `urlPattern` no coincide.  
**Solución:** Verifica las anotaciones `@WebServlet` y limpia/recompila con `Clean and Build`.

### ❌ "Table 'RESERVA' doesn't exist" en los logs pero la app funciona
**No es un error.** EclipseLink con `create-or-extend-tables` verifica si la tabla existe y la crea si no. El log `FINE` es informativo.

### ❌ Puerto 8080 ocupado
**Solución:** En `conf/server.xml` de Tomcat cambia el puerto:
```xml
<Connector port="8090" protocol="HTTP/1.1" .../>
```
Y accede en `http://localhost:8090/prueba/`.

### ❌ NetBeans no encuentra el servidor Tomcat 11
**Solución:** `Tools → Servers → Add Server → Apache Tomcat or TomEE` y apunta a la carpeta raíz de Tomcat 11.

---

## Checklist rápido de despliegue

```
[ ] JDK 21 instalado y JAVA_HOME configurado
[ ] XAMPP instalado — módulo MySQL en Start
[ ] Base de datos "parcialuno" creada en phpMyAdmin
[ ] Tomcat 11 registrado en NetBeans
[ ] pom.xml sin errores (Clean and Build exitoso)
[ ] persistence.xml apunta a localhost:3306/parcialuno con user=root
[ ] WAR generado en target/prueba.war
[ ] WAR copiado a webapps/ de Tomcat (o desplegado desde NetBeans)
[ ] Aplicación accesible en http://localhost:8080/prueba/
```

---

## Créditos

Proyecto académico — Universidad Compensar  
Parcial 1 — Desarrollo de aplicaciones web con Jakarta EE + JPA + MySQL
