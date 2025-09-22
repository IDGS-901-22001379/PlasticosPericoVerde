 let empleados = [];

export async function inicializar() {
    refreshTableEmpleados();
}

export async function save() {
    // Obtener valores del formulario
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
    let fechaIngreso = document.getElementById('txtFechaIngreso').value;
    let puesto = document.getElementById('txtPuesto').value;
    let email = document.getElementById('txtEmail').value;
    let codigoEmpleado = document.getElementById('txtCodigoEmpleado').value;


  // Generar código de empleado si no se proporciona uno
    if (!codigoEmpleado) {
        const fecha = new Date(fechaIngreso);
        const dia = String(fecha.getDate()).padStart(2, '0');
        const mes = String(fecha.getMonth() + 1).padStart(2, '0');
        const randomNum = String(Math.floor(Math.random() * 100)).padStart(2, '0');
        codigoEmpleado = `${mes}${dia}${randomNum}`;
    }
    
    console.log(nombre + " " + apellidoPaterno + " " + apellidoMaterno + " " + genero + " " + fechaNacimiento + " " + rfc + " " + curp + " " + domicilio + " " + codigoPostal + " " + ciudad + " " + estado + " " + telefono + " " + fechaIngreso + " " + puesto + " " + email + " " + codigoEmpleado);

    let url = "http://localhost:8080/PericoVerde/api/empleado/save";
    let params = null;
    let resp = null;
    let datos = null;

    try {
        let emp = {
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
            fechaIngreso: fechaIngreso,
            puesto: puesto,
            email: email,
            codigoEmpleado: codigoEmpleado
        };

        params = {
            datosEmpleado: JSON.stringify(emp)
        };

        let ctype = 'application/x-www-form-urlencoded;charset=UTF-8';
        resp = await fetch(url, {
            method: "POST",
            headers: { 'Content-Type': ctype },
            body: new URLSearchParams(params)
        });

        datos = await resp.json();

        if (datos.error != null) {
            Swal.fire('Error al guardar los datos del empleado', datos.error, 'warning');
            return;
        }

        if (datos.exception != null) {
            Swal.fire('Error', datos.exception, 'danger');
            return;
        }

        refreshTableEmpleados();
         clearForm(); 
        Swal.fire('Movimiento Realizado', 'Datos del empleado guardados correctamente', 'success');
    } catch (error) {
        console.error("Error al guardar los datos del empleado: ", error);
    }
}

export async function refreshTableEmpleados() {
    let url = "http://localhost:8080/PericoVerde/api/empleado/getAll";
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

        empleados = datos.filter(emp => emp.estatusEmpleado === 1);
        fillTableEmpleados();
    } catch (error) {
        console.error("Error al obtener los datos de los empleados: ", error);
    }
}

export async function deleteEmpleado() {
    let idEmpleado = parseInt(document.getElementById('txtCodigoEmpleado').value);

    if (!idEmpleado || idEmpleado <= 0) {
        Swal.fire('Error', 'Selecciona un empleado válido para eliminar.', 'error');
        return;
    }

    let url = `http://localhost:8080/PericoVerde/api/empleado/delete?idEmpleado=${idEmpleado}`;

    try {
        let resp = await fetch(url, {
            method: "GET",
            headers: { 'Content-Type': 'application/json' }
        });

        let data = await resp.json();

        if (data.result) {
            Swal.fire('Empleado Eliminado', data.result, 'success');
            refreshTableEmpleados(); 
            clearForm(); 
        } else if (data.exception) {
            Swal.fire('Error', data.exception, 'error');
        }
    } catch (error) {
        console.error("Error al intentar eliminar el empleado: ", error);
        Swal.fire('Error', 'Ocurrió un error al intentar eliminar el empleado.', 'error');
    }
}

export async function clearForm() {
    document.getElementById('employeeForm').reset();
}

export function fillTableEmpleados() {
    let contenido = "";
    for (let i = 0; i < empleados.length; i++) {
        if (empleados[i].estatusEmpleado === 1) {
            contenido += `<tr onclick="seleccionarEmpleado(${i})">
                <td>${empleados[i].nombre}</td>
                <td>${empleados[i].apellidoPaterno}</td>
                <td>${empleados[i].apellidoMaterno}</td>
                <td>${empleados[i].fechaIngreso}</td>
                <td>${empleados[i].puesto}</td>
                <td>${empleados[i].codigoEmpleado}</td>
                
                <td>
                    <!-- Aquí puedes agregar cualquier acción adicional como botones de editar/eliminar -->
                </td>
            </tr>`;
        }
    }
    document.getElementById("tbodyEmpleados").innerHTML = contenido;
}
export function seleccionarEmpleado(index) {
    let empleado = empleados[index];
    document.getElementById('txtNombre').value = empleado.nombre || '';
    document.getElementById('txtApellidoPaterno').value = empleado.apellidoPaterno || '';
    document.getElementById('txtApellidoMaterno').value = empleado.apellidoMaterno || '';
    document.getElementById('txtGenero').value = empleado.genero || '';
    document.getElementById('txtFechaNacimiento').value = empleado.fechaNacimiento || '';
    document.getElementById('txtRfc').value = empleado.rfc || '';
    document.getElementById('txtCurp').value = empleado.curp || '';
    document.getElementById('txtDomicilio').value = empleado.domicilio || '';
    document.getElementById('txtCodigoPostal').value = empleado.codigoPostal || '';
    document.getElementById('txtCiudad').value = empleado.ciudad || '';
    document.getElementById('txtEstado').value = empleado.estado || '';
    document.getElementById('txtTelefono').value = empleado.telefono || '';
    document.getElementById('txtFechaIngreso').value = empleado.fechaIngreso || '';
    document.getElementById('txtPuesto').value = empleado.puesto || '';
    document.getElementById('txtEmail').value = empleado.email || '';
    document.getElementById('txtCodigoEmpleado').value = empleado.codigoEmpleado || '';
}


// Hacer que la función esté disponible globalmente
window.seleccionarEmpleado = seleccionarEmpleado;
window.clearForm = clearForm;