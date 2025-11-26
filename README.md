# Proyecto AutoLife - Evaluación 4 🚗

### 👥 Integrantes:
* Guiovanni Gómez
* Jhanpieer Rodriguez

### 📄 Descripción:
Aplicación móvil nativa en Kotlin para la gestión de vehículos y mantenimiento, desarrollada con arquitectura MVVM. El sistema consume Microservicios propios (Spring Boot) y una API Externa del Gobierno (Dólar).

### 🛠️ Tecnologías:
* **Frontend:** Android Nativo (Kotlin + Jetpack Compose).
* **Backend:** Spring Boot (Java) - Microservicios.
* **Base de Datos:** MariaDB.
* **API Externa:** Mindicador.cl (Dólar).
* **Testing:** JUnit + Mockito (Backend y Frontend).

### 🚀 Instrucciones de Ejecución:
1. **Base de Datos:** Importar los scripts `autolife_db.sql` y `autolife_maintenance.sql` en MariaDB (o crear las bases vacías).
2. **Backend:** Abrir la carpeta `Backend` y ejecutar los servicios `car-service` (puerto 8080) y `maintenance-service` (puerto 8081).
3. **Móvil:** Abrir la carpeta `App_Movil` en Android Studio, sincronizar Gradle y ejecutar en Emulador o Celular físico.

### 📦 Entregables:
El archivo **APK (Debug)** y la **Llave (.jks)** se encuentran en la carpeta `/Entregables` de este repositorio.
