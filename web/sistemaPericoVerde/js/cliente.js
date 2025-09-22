let clientes = [];

export async function inicializar() {
    refreshTableClientes();
}

export async function saveCliente() {
    let idCliente = parseInt(document.getElementById('txtIdCliente').value);
    let nombre = document.getElementById('txtNombre').value;
    let apellidoPaterno = document.getElementById('txtApellidoPaterno').value;
    let apellidoMaterno = document.getElementById('txtApellidoMaterno').value;
    let genero = document.getElementById('txtGenero').value;
    let fechaNacimiento = document.getElementById('txtFechaNacimiento').value;
    let rfc = document.getElementById('txtRfc').value;
    let curp = document.getElementById('txtCurp').value;
    let domicilio = document.getElementById('txtDomicilio').value;
    let codigoPostal = parseInt(document.getElementById('txtCodigoPostal').value);
    let ciudad = document.getElementById('txtCiudad').value;
    let estado = document.getElementById('txtEstado').value;
    let telefono = parseInt(document.getElementById('txtTelefono').value);
    let fechaRegistro = document.getElementById('txtFechaRegistro').value;
    let correoElectronico = document.getElementById('txtCorreoElectronico').value;

    let url = "http://localhost:8080/PericoVerde/api/Cliente/save";
    let params = null;
    let resp = null;
    let datos = null;

    try {
        let cliente = {
            idCliente: idCliente,
            nombre: nombre,
            apellidoPaterno: apellidoPaterno,
            apellidoMaterno: apellidoMaterno,
            genero: genero,
            fechaNacimiento: fechaNacimiento,
            rfc: rfc,
            curp: curp,
            domicilio: domicilio,
            codigoPostal: codigoPostal,
            ciudad: ciudad,
            estado: estado,
            telefono: telefono,
            fechaRegistro: fechaRegistro,
            correoElectronico: correoElectronico
        };

        params = {
            datosCliente: JSON.stringify(cliente)
        };

        let ctype = 'application/x-www-form-urlencoded;charset=UTF-8';
        resp = await fetch(url, {
            method: "POST",
            headers: { 'Content-Type': ctype },
            body: new URLSearchParams(params)
        });

        datos = await resp.json();

        if (datos.error != null) {
            handleCustomErrors(datos.error);
            return;
        }

        if (datos.exception != null) {
            handleCustomErrors(datos.exception);
            return;
        }

        refreshTableClientes();
        Swal.fire('Movimiento Realizado', 'Datos del cliente guardados correctamente', 'success');
    } catch (error) {
        console.error("Error al guardar los datos del cliente: ", error);
    }
}

function handleCustomErrors(errorMessage) {
    let friendlyMessage = '';

    if (errorMessage.includes("p_fecha_nacimiento")) {
        friendlyMessage = 'Te falta registrar la fecha de nacimiento';
    } else if (errorMessage.includes("p_fecha_registro")) {
        friendlyMessage = 'Te falta registrar la fecha de registro';
    } else if (errorMessage.includes("p_codigo_postal")) {
        friendlyMessage = 'Te falta registrar el código postal';
    } else if (errorMessage.includes("p_telefono")) {
        friendlyMessage = 'Te falta registrar el teléfono';
    } else if (errorMessage.includes("p_nombre")) {
        friendlyMessage = 'Te falta registrar el nombre';
    } else if (errorMessage.includes("p_apellido_paterno")) {
        friendlyMessage = 'Te falta registrar el apellido paterno';
    } else if (errorMessage.includes("p_apellido_materno")) {
        friendlyMessage = 'Te falta registrar el apellido materno';
    } else if (errorMessage.includes("p_genero")) {
        friendlyMessage = 'Te falta registrar el género';
    } else if (errorMessage.includes("p_rfc")) {
        friendlyMessage = 'Te falta registrar el RFC';
    } else if (errorMessage.includes("p_curp")) {
        friendlyMessage = 'Te falta registrar la CURP';
    } else if (errorMessage.includes("p_domicilio")) {
        friendlyMessage = 'Te falta registrar el domicilio';
    } else if (errorMessage.includes("p_ciudad")) {
        friendlyMessage = 'Te falta registrar la ciudad';
    } else if (errorMessage.includes("p_estado")) {
        friendlyMessage = 'Te falta registrar el estado';
    } else if (errorMessage.includes("p_correo_electronico")) {
        friendlyMessage = 'Te falta registrar el correo electrónico';
    } else {
        friendlyMessage = 'Ocurrió un error inesperado, por favor revisa los datos e intenta nuevamente.';
    }

    Swal.fire('Error al guardar los datos del cliente', friendlyMessage, 'warning');
}

export async function refreshTableClientes() {
    let url = "http://localhost:8080/PericoVerde/api/Cliente/getAll";
    try {
        let resp = await fetch(url);
        let datos = await resp.json();

        if (datos.error != null) {
            Swal.fire('', datos.error, 'warning');
            return;
        }

        if (datos.exception != null) {
            Swal.fire('', datos.exception, 'danger');
            return;
        }

        clientes = datos;
        fillTableClientes();
    } catch (error) {
        console.error("Error al obtener los datos de los clientes: ", error);
    }
}

export async function deleteCliente() {
    let idCliente = parseInt(document.getElementById('txtIdCliente').value);

    if (!idCliente || idCliente <= 0) {
        Swal.fire('Error', 'Selecciona un cliente válido para eliminar.', 'error');
        return;
    }

    let url = `http://localhost:8080/PericoVerde/api/Cliente/eliminar`;

    try {
        let resp = await fetch(url, {
            method: "POST",
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({ idCliente })
        });

        let data = await resp.json();

        if (data.result) {
            Swal.fire('Cliente Eliminado', data.result, 'success');
            refreshTableClientes();
            clearForm();
        } else if (data.exception) {
            Swal.fire('Error', data.exception, 'error');
        }
    } catch (error) {
        console.error("Error al intentar eliminar el cliente: ", error);
        Swal.fire('Error', 'Ocurrió un error al intentar eliminar el cliente.', 'error');
    }
}

function clearForm() {
    document.getElementById('clienteForm').reset();
}

export function resetForm() {
    clearForm();
    document.getElementById('txtIdCliente').value = '';  // Limpiar el ID del cliente si es necesario
}

export function fillTableClientes() {
    let contenido = "";
    for (let i = 0; i < clientes.length; i++) {
        contenido += `<tr onclick="seleccionarCliente(${i})">
            <td>${clientes[i].nombre}</td>
            <td>${clientes[i].apellidoPaterno}</td>
            <td>${clientes[i].apellidoMaterno}</td>
            <td>${clientes[i].fechaRegistro}</td>
            <td>${clientes[i].correoElectronico}</td>
            <td>${clientes[i].telefono}</td>
            <td>${clientes[i].estado}</td>
        </tr>`;
    }
    document.getElementById("tbodyClientes").innerHTML = contenido;
}

export function seleccionarCliente(index) {
    let cliente = clientes[index];
    document.getElementById('txtIdCliente').value = cliente.idCliente || '';
    document.getElementById('txtNombre').value = cliente.nombre || '';
    document.getElementById('txtApellidoPaterno').value = cliente.apellidoPaterno || '';
    document.getElementById('txtApellidoMaterno').value = cliente.apellidoMaterno || '';
    document.getElementById('txtGenero').value = cliente.genero || '';
    document.getElementById('txtFechaNacimiento').value = cliente.fechaNacimiento || '';
    document.getElementById('txtRfc').value = cliente.rfc || '';
    document.getElementById('txtCurp').value = cliente.curp || '';
    document.getElementById('txtDomicilio').value = cliente.domicilio || '';
    document.getElementById('txtCodigoPostal').value = cliente.codigoPostal || '';
    document.getElementById('txtCiudad').value = cliente.ciudad || '';
    document.getElementById('txtEstado').value = cliente.estado || '';
    document.getElementById('txtTelefono').value = cliente.telefono || '';
    document.getElementById('txtFechaRegistro').value = cliente.fechaRegistro || '';
    document.getElementById('txtCorreoElectronico').value = cliente.correoElectronico || '';

    // Asegurarse de que el género esté seleccionado correctamente
    let generoElement = document.getElementById('txtGenero');
    if (cliente.genero) {
        generoElement.value = cliente.genero;
    } else {
        generoElement.value = '';
    }
}

// Asegurarse de que las funciones estén disponibles globalmente después de que el DOM se haya cargado
document.addEventListener("DOMContentLoaded", function () {
    inicializar();
    window.saveCliente = saveCliente;
    window.deleteCliente = deleteCliente;
    window.seleccionarCliente = seleccionarCliente;
    window.resetForm = resetForm;  // Agregar la función resetForm a los métodos globales
});
