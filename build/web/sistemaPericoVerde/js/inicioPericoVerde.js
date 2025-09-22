let modulo = null;

async function cargarModuloEmpleados() {
    let respuesta = await fetch('moduloEmpleados/vistaEmpleado.html');
    let contenido = await respuesta.text();
    document.getElementById("contenedorPrincipal").innerHTML = contenido;
    document.getElementById("contenedorPrincipal").classList.add("contenedor-formulario");

    import('./empleado.js')
        .then(obj => {
            modulo = obj;
            modulo.inicializar();
        });
}

async function cargarModuloClientes() {
    let respuesta = await fetch('moduloClientes/vistaClientes.html');
    let contenido = await respuesta.text();
    document.getElementById("contenedorPrincipal").innerHTML = contenido;
    document.getElementById("contenedorPrincipal").classList.add("contenedor-formulario");

    import('./cliente.js')
        .then(obj => {
            modulo = obj;
            modulo.inicializar();
            window.saveCliente = obj.saveCliente;
            window.deleteCliente = obj.deleteCliente;
            window.seleccionarCliente = obj.seleccionarCliente;
        });
}

async function cargarModuloProveedores() {
    let respuesta = await fetch('moduloProveedores/vistaProveedores.html');
    let contenido = await respuesta.text();
    document.getElementById("contenedorPrincipal").innerHTML = contenido;
    document.getElementById("contenedorPrincipal").classList.add("contenedor-formulario");

    import('./proveedor.js')
        .then(obj => {
            modulo = obj;
            modulo.inicializar();
            window.saveProveedor = obj.saveProveedor;
            window.deleteProveedor = obj.deleteProveedor;
            window.seleccionarProveedor = obj.seleccionarProveedor;
        });
}

async function cargarModuloHorarios() {
    let respuesta = await fetch('moduloHorarios/vistaHorarios.html');
    let contenido = await respuesta.text();
    document.getElementById("contenedorPrincipal").innerHTML = contenido;
    document.getElementById("contenedorPrincipal").classList.add("contenedor-formulario");

    import('./horario.js')
        .then(obj => {
            modulo = obj;
            modulo.inicializar();
        });
}