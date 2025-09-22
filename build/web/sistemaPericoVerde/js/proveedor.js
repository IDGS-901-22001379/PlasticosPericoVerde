let proveedores = [];

export async function inicializar() {
    refreshTableProveedores();
}

export async function saveProveedor() {
    // Obtener valores del formulario
    let idProveedor = parseInt(document.getElementById('txtIdProveedor').value);
    let nombre = document.getElementById('txtNombre').value;
    let tipoProveedor = document.getElementById('txtTipoProveedor').value;
    let rfc = document.getElementById('txtRfc').value;
    let telefono = document.getElementById('txtTelefono').value;
    let direccion = document.getElementById('txtDireccion').value;
    let codigoPostal = document.getElementById('txtCodigoPostal').value;
    let ciudad = document.getElementById('txtCiudad').value;
    let estado = document.getElementById('txtEstado').value;
    let estatusProveedores = document.getElementById('txtEstatusProveedores').value;
    let correoElectronico = document.getElementById('txtCorreoElectronico').value;

    let url = "http://localhost:8080/PericoVerde/api/Proveedor/save";
    let params = null;
    let resp = null;
    let datos = null;

    try {
        let proveedor = {
            idProveedor: idProveedor,
            nombre: nombre,
            tipoProveedor: tipoProveedor,
            rfc: rfc,
            telefono: telefono,
            direccion: direccion,
            codigoPostal: codigoPostal,
            ciudad: ciudad,
            estado: estado,
            estatusProveedores: estatusProveedores,
            correoElectronico: correoElectronico
        };

        params = {
            datosProveedor: JSON.stringify(proveedor)
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

        refreshTableProveedores();
        Swal.fire('Movimiento Realizado', 'Datos del proveedor guardados correctamente', 'success');
    } catch (error) {
        console.error("Error al guardar los datos del proveedor: ", error);
    }
}

function handleCustomErrors(errorMessage) {
    let friendlyMessage = '';

    if (errorMessage.includes("p_nombre")) {
        friendlyMessage = 'Te falta registrar el nombre del proveedor';
    } else if (errorMessage.includes("p_tipo_proveedor")) {
        friendlyMessage = 'Te falta registrar el tipo de proveedor';
    } else if (errorMessage.includes("p_rfc")) {
        friendlyMessage = 'Te falta registrar el RFC del proveedor';
    } else if (errorMessage.includes("p_telefono")) {
        friendlyMessage = 'Te falta registrar el teléfono del proveedor';
    } else if (errorMessage.includes("p_direccion")) {
        friendlyMessage = 'Te falta registrar la dirección del proveedor';
    } else if (errorMessage.includes("p_codigo_postal")) {
        friendlyMessage = 'Te falta registrar el código postal del proveedor';
    } else if (errorMessage.includes("p_ciudad")) {
        friendlyMessage = 'Te falta registrar la ciudad del proveedor';
    } else if (errorMessage.includes("p_estado")) {
        friendlyMessage = 'Te falta registrar el estado del proveedor';
    } else if (errorMessage.includes("p_estatus_proveedores")) {
        friendlyMessage = 'Te falta registrar el estatus del proveedor';
    } else if (errorMessage.includes("p_correo_electronico")) {
        friendlyMessage = 'Te falta registrar el correo electrónico del proveedor';
    } else {
        friendlyMessage = 'Ocurrió un error inesperado, por favor revisa los datos e intenta nuevamente.';
    }

    Swal.fire('Error al guardar los datos del proveedor', friendlyMessage, 'warning');
}

export async function refreshTableProveedores() {
    let url = "http://localhost:8080/PericoVerde/api/Proveedor/getAll";
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

        proveedores = datos;
        fillTableProveedores();
    } catch (error) {
        console.error("Error al obtener los datos de los proveedores: ", error);
    }
}

export async function deleteProveedor() {
    let idProveedor = parseInt(document.getElementById('txtIdProveedor').value);

    if (!idProveedor || idProveedor <= 0) {
        Swal.fire('Error', 'Selecciona un proveedor válido para eliminar.', 'error');
        return;
    }

    let url = `http://localhost:8080/PericoVerde/api/Proveedor/eliminar`;

    try {
        let resp = await fetch(url, {
            method: "POST",
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({ idProveedor })
        });

        let data = await resp.json();

        if (data.result) {
            Swal.fire('Proveedor Eliminado', data.result, 'success');
            refreshTableProveedores();
            clearForm();
        } else if (data.exception) {
            Swal.fire('Error', data.exception, 'error');
        }
    } catch (error) {
        console.error("Error al intentar eliminar el proveedor: ", error);
        Swal.fire('Error', 'Ocurrió un error al intentar eliminar el proveedor.', 'error');
    }
}

function clearForm() {
    document.getElementById('proveedorForm').reset();
}

export function resetProveedorForm() {
    clearForm();
    document.getElementById('txtIdProveedor').value = '0';  // Reiniciar el ID del proveedor
}

export function fillTableProveedores() {
    let contenido = "";
    for (let i = 0; i < proveedores.length; i++) {
        contenido += `<tr onclick="seleccionarProveedor(${i})">
            <td>${proveedores[i].idProveedor}</td>
            <td>${proveedores[i].nombre}</td>
            <td>${proveedores[i].tipoProveedor}</td>
            <td>${proveedores[i].rfc}</td>
            <td>${proveedores[i].ciudad}</td>
            <td>${proveedores[i].estado}</td>
            <td>${proveedores[i].telefono}</td>
            <td>${proveedores[i].correoElectronico}</td>
            <td>${proveedores[i].estatusProveedores}</td>
        </tr>`;
    }
    document.getElementById("tbodyProveedores").innerHTML = contenido;
}

export function seleccionarProveedor(index) {
    let proveedor = proveedores[index];
    document.getElementById('txtIdProveedor').value = proveedor.idProveedor || '';
    document.getElementById('txtNombre').value = proveedor.nombre || '';
    document.getElementById('txtTipoProveedor').value = proveedor.tipoProveedor || '';
    document.getElementById('txtRfc').value = proveedor.rfc || '';
    document.getElementById('txtTelefono').value = proveedor.telefono || '';
    document.getElementById('txtDireccion').value = proveedor.direccion || '';
    document.getElementById('txtCodigoPostal').value = proveedor.codigoPostal || '';
    document.getElementById('txtCiudad').value = proveedor.ciudad || '';
    document.getElementById('txtEstado').value = proveedor.estado || '';
    document.getElementById('txtEstatusProveedores').value = proveedor.estatusProveedores || '';
    document.getElementById('txtCorreoElectronico').value = proveedor.correoElectronico || '';
}

// Asegurarse de que las funciones estén disponibles globalmente después de que el DOM se haya cargado
document.addEventListener("DOMContentLoaded", function () {
    inicializar();
    window.saveProveedor = saveProveedor;
    window.deleteProveedor = deleteProveedor;
    window.seleccionarProveedor = seleccionarProveedor;
    window.resetProveedorForm = resetProveedorForm;  // Agregar la función resetProveedorForm a los métodos globales
});

