# 📷 QReader

#### App para leer códigos QR en formato vCard y almacenar la información en los contacto. De igual manera se puede generar vCards propias para otras personas.
## Introducción
En esta app desarrollada en lenguaje Kotlin funciona como una agenda virtual donde tu puedes: 
- Utilizar la cámara para poder leer códigos QR de vCards.
- Agregar la información de la vCard escaneada a tu aplicación de contactos automáticamente.
- Completar un formulario dentro de la aplicación para generar tu propia vCard QR.
- Desplegar dicha información via QR dentro de la aplicación.

## 💻 Tech Stack
- Para desarrollar el proyecto se utilizó **Android Studio** para programar la aplicación como nativa para android. 
- Se hizo uso de la biblioteca **Zxing  para Java** para poder escanear los codigos QR.
- Uso de **Fragments** para la UI y **Navigation Component** para poder desplazarse a través de la app.
- En el proceso de desarrollo se implemento control de versionamiento utilizando **Git** y manejo de repositorios en **Github**.

## 📲 In-app
- Observamos el icono de la app.
- Al iniciar la aplicación, observamos el siguiente menú donde podremos seleccionar cualquiera de las dos opciones disponibles entre leer un código QR o crear una vCard.
<img src="https://github.com/alexisserapio/AppQR/blob/master/images/1.png" alt="Captura de Pantalla sobre la app en el móvil y su interfaz principal" width="450" height="330">

---
- Al moverse a la sección "Escáner de códigos QR" se nos solicita conceder el permiso para utilizar la cámara.
- Realizamos el escaneo.
<img src="https://github.com/alexisserapio/AppQR/blob/master/images/2.png" alt="Captura de Pantalla sobre el apartado de escaneo QR" width="450" height="330">

---
- Al moverse a la sección "Generador de vCards QR" se nos solicita conceder el permiso para acceder a los contactos.
- Colocamos en el formulario la información que queremos que contenga la vCard.
<img src="https://github.com/alexisserapio/AppQR/blob/master/images/3.png" alt="Captura de Pantalla sobre el apartado de generar QR" width="450" height="330">

---
- Despliegue del código QR generado.
<img src="https://github.com/alexisserapio/AppQR/blob/master/images/4.png" alt="Captura de Pantalla sobre el QR generado" width="150" height="330">

## 📣 To-do
- Modificar las restricciones puestas dentro del lector QR para poder recibir todo tipo de códigos QR.
- Procurar mejorar el diseño de la interfaz para tener un diseño más moderno y actualizado.
- Implementar la generación de códigos QR de todo tipo para que el usuario pueda utilizar estos QR en procesos personales.
