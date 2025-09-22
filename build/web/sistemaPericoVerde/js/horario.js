let horarios = []; 

export async function inicializar() {
    refreshTableHorarios();
}

export async function registrarEntrada() {
    let codigoEmpleado = document.getElementById('txtCodigoEmpleadoHorario').value;

    if (!codigoEmpleado || codigoEmpleado <= 0) {
        Swal.fire('Error', 'Ingresa un código de empleado válido.', 'error');
        return;
    }

    let url = "http://localhost:8080/PericoVerde/api/asistencia/entrada";
    let params = new URLSearchParams();
    params.append('codigoEmpleado', codigoEmpleado);

    try {
        let resp = await fetch(url, {
            method: "POST",
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params
        });

        if (!resp.ok) {
            throw new Error('Error en la solicitud: ' + resp.status);
        }

        let data = await resp.json();

        if (data.error) {
            Swal.fire('Error al registrar entrada', data.error, 'warning');
            return;
        }

        if (data.exception) {
            Swal.fire('Error', data.exception, 'danger');
            return;
        }

        // Actualizar la tabla de asistencia
        refreshTableHorarios();

        Swal.fire('Entrada Registrada', 'Hora de entrada registrada correctamente', 'success');
    } catch (error) {
        console.error('Error al registrar entrada:', error);
        Swal.fire('Error', 'Ocurrió un error al intentar registrar la entrada.', 'error');
    }
}

export async function registrarSalida() {
    let codigoEmpleado = document.getElementById('txtCodigoEmpleadoHorario').value;

    if (!codigoEmpleado || codigoEmpleado <= 0) {
        Swal.fire('Error', 'Ingresa un código de empleado válido.', 'error');
        return;
    }

    let url = "http://localhost:8080/PericoVerde/api/asistencia/salida";
    let params = new URLSearchParams();
    params.append('codigoEmpleado', codigoEmpleado);

    try {
        let resp = await fetch(url, {
            method: "POST",
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params
        });

        if (!resp.ok) {
            throw new Error('Error en la solicitud: ' + resp.status);
        }

        let data = await resp.json();

        if (data.error) {
            Swal.fire('Error al registrar salida', data.error, 'warning');
            return;
        }

        if (data.exception) {
            Swal.fire('Error', data.exception, 'danger');
            return;
        }

        // Actualizar la tabla de asistencia
        refreshTableHorarios();

        Swal.fire('Salida Registrada', 'Hora de salida registrada correctamente', 'success');
    } catch (error) {
        console.error('Error al registrar salida:', error);
        Swal.fire('Error', 'Ocurrió un error al intentar registrar la salida.', 'error');
    }
}

export async function refreshTableHorarios() {
    let url = "http://localhost:8080/PericoVerde/api/asistencia/getAll";
    try {
        let response = await fetch(url);
        let data = await response.json();

        if (data.error) {
            Swal.fire('Error', data.error, 'warning');
            return;
        }

        if (data.exception) {
            Swal.fire('Error', data.exception, 'error');
            return;
        }

        horarios = data;
        fillTableHorarios();
    } catch (error) {
        console.error("Error al obtener los horarios: ", error);
        Swal.fire('Error', 'Ocurrió un error al obtener los horarios. Por favor, inténtelo de nuevo.', 'error');
    }
}

export async function fillTableHorarios() {
    const hoy = new Date().toISOString().split('T')[0];
    const semana = getWeekStart();
    const mes = getMonthStart();

    const filtroNombre = document.getElementById('filtroNombre').value.toLowerCase();
    const filtroCodigo = document.getElementById('filtroCodigo').value;
    const periodo = document.getElementById("selectPeriodo").value;

    let contenido = "";

    for (let i = 0; i < horarios.length; i++) {
        if ((periodo === 'dia' && horarios[i].fecha === hoy ||
             periodo === 'semana' && horarios[i].fecha >= semana ||
             periodo === 'mes' && horarios[i].fecha >= mes) &&
            (horarios[i].nombre.toLowerCase().includes(filtroNombre) &&
             horarios[i].codigoEmpleado.toString().includes(filtroCodigo))) {

            contenido += `<tr>
                <td>${horarios[i].codigoEmpleado}</td>
                <td>${horarios[i].nombre}</td>
                <td>${horarios[i].apellidoPaterno}</td>
                <td>${horarios[i].puesto}</td>
                <td>${horarios[i].horaEntrada}</td>
                <td>${horarios[i].horaSalida}</td>
                <td>${horarios[i].fecha}</td>
            </tr>`;
        }
    }

    document.getElementById("tbodyAsistenciaDia").innerHTML = contenido;
    document.getElementById("tbodyAsistenciaSemana").innerHTML = contenido;
    document.getElementById("tbodyAsistenciaMes").innerHTML = contenido;
}

function getWeekStart() {
    const today = new Date();
    const first = today.getDate() - today.getDay();
    const firstDay = new Date(today.setDate(first));
    return firstDay.toISOString().split('T')[0];
}

function getMonthStart() {
    const today = new Date();
    const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
    return firstDay.toISOString().split('T')[0];
}

function limpiarFormularioHorario() {
    document.getElementById('attendanceForm').reset();
}

function cambiarVista() {
    let periodo = document.getElementById("selectPeriodo").value;
    document.getElementById("vistaDia").style.display = periodo === "dia" ? "block" : "none";
    document.getElementById("vistaSemana").style.display = periodo === "semana" ? "block" : "none";
    document.getElementById("vistaMes").style.display = periodo === "mes" ? "block" : "none";

    fillTableHorarios(); // Actualizar tabla al cambiar de vista
}

window.registrarEntrada = registrarEntrada;
window.registrarSalida = registrarSalida;
window.cambiarVista = cambiarVista;
window.refreshTableHorarios = refreshTableHorarios;

// Añadir eventos para filtros
document.getElementById('filtroNombre').addEventListener('input', fillTableHorarios);
document.getElementById('filtroCodigo').addEventListener('input', fillTableHorarios);
