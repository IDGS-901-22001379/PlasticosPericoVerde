// Función login que realiza la autenticación
async function login() {
    let url = "http://localhost:8080/PericoVerde/api/login/validar?";
    let usuario = document.getElementById("txtUsuario").value;
    let contrasenia = document.getElementById("txtContrasenia").value;
    url += "nombreUsuario=" + encodeURIComponent(usuario) + "&contrasenia=" + encodeURIComponent(contrasenia);

    try {
        let resp = await fetch(url);
        if (!resp.ok) {
            throw new Error(`HTTP error! status: ${resp.status}`);
        }
        let data = await resp.json();
        
        if (data.exception != null) {
            Swal.fire('', 'Ocurrió un error interno. Intente más tarde.', 'danger');
            return;
        }
        if (data.error != null) {
            Swal.fire('Error', data.error, 'warning');
            return;
        }
        
        // Redirigir a la página de inicio si la autenticación es exitosa
        window.location.replace('sistemaPericoVerde/inicioPericoVerde.html');
    } catch (error) {
        console.error('Error en el try:', error);
        Swal.fire('Error', 'Ocurrió un error. Por favor, inténtelo de nuevo.', 'error');
    }
}
    
    
    
    

