📚 Sistema de Gestión de Biblioteca

Este proyecto es una aplicación de escritorio en Java que permite gestionar una biblioteca utilizando JDBC para la conexión con una base de datos MySQL.

🚀 Características principales

Gestión de usuarios (registro, autenticación y roles).

Gestión de libros (agregar, actualizar, eliminar y buscar libros).

Gestión de préstamos y devoluciones.

Gestión de reservas de libros.

Generación de reportes de uso.

Interfaz gráfica con Swing para una experiencia más intuitiva.

🛠️ Requisitos Previos

Antes de instalar y ejecutar el proyecto, asegúrate de tener los siguientes requisitos:

Java JDK 11 o superior

MySQL Server

Eclipse IDE (o cualquier otro IDE compatible con proyectos Java Maven)

Maven (para la gestión de dependencias)

Git (opcional, para clonar el repositorio)

⚙️ Instalación y Configuración

1️⃣ Clonar el repositorio

Si deseas obtener el código desde GitHub, usa el siguiente comando:

git clone https://github.com/tu-usuario/sistema-biblioteca.git
cd sistema-biblioteca

2️⃣ Configurar la base de datos MySQL

Ejecuta el siguiente script SQL para crear la base de datos y las tablas necesarias:

CREATE DATABASE biblioteca;
USE biblioteca;

CREATE TABLE usuarios (
id_usuario INT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
email VARCHAR(100) UNIQUE NOT NULL,
telefono VARCHAR(20),
rol ENUM('administrador', 'usuario') NOT NULL,
password VARCHAR(255) NOT NULL
);

CREATE TABLE libros (
id_libro INT AUTO_INCREMENT PRIMARY KEY,
titulo VARCHAR(200) NOT NULL,
autor VARCHAR(100) NOT NULL,
genero VARCHAR(50),
estado ENUM('disponible', 'prestado') NOT NULL
);

CREATE TABLE prestamos (
id_prestamo INT AUTO_INCREMENT PRIMARY KEY,
id_usuario INT,
id_libro INT,
fecha_prestamo DATE NOT NULL,
fecha_devolucion DATE,
FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
FOREIGN KEY (id_libro) REFERENCES libros(id_libro)
);

3️⃣ Configurar el archivo de conexión JDBC

Edita el archivo config.properties dentro del proyecto y configura la conexión con MySQL:

db.url=jdbc:mysql://localhost:3306/biblioteca
db.user=root
db.password=tu_contraseña
db.driver=com.mysql.cj.jdbc.Driver

4️⃣ Ejecutar el Proyecto

Desde Eclipse o cualquier otro IDE, ejecuta la clase MainFrame.java para iniciar la aplicación con interfaz gráfica.
Si deseas ejecutarlo desde la terminal, usa:

mvn compile
mvn exec:java -Dexec.mainClass="com.biblioteca.gui.MainFrame"

🖥️ Uso del Sistema

👤 Inicio de sesión

Los usuarios deben autenticarse con su email y contraseña.

Si es un administrador, tendrá acceso a la gestión de libros, préstamos y reportes.

📚 Gestión de Libros

Agregar, actualizar y eliminar libros.

Consultar disponibilidad por título, autor o género.

📖 Gestión de Préstamos

Registrar préstamos y devoluciones.

Limitar a 3 libros por usuario regular.

📊 Generación de Reportes

Libros más prestados.

Usuarios con más préstamos.

Libros no prestados en el último año.

🛠️ Tecnologías Utilizadas

Java 11

Swing (Interfaz gráfica)

JDBC (Conexión a base de datos)

MySQL (Sistema de gestión de bases de datos)

Maven (Gestión de dependencias)

GitHub (Control de versiones)

✨ Contribuciones

Si deseas mejorar el proyecto, puedes hacer un fork, trabajar en una nueva rama y enviar un pull request.

📜 Licencia

Este proyecto está bajo la licencia MIT. Puedes usarlo y modificarlo libremente.

📩 Contacto

Si tienes alguna duda o sugerencia, no dudes en contactarme:
📧 tu.email@correo.com
