<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sistema de reservas</title>
<link rel="stylesheet" href="style.css">
</head>

<body>

<div class="panel">

<h1>Formulario de reserva COWORKING</h1>

<form action="SvReserva" method="POST" class="formulario">

<label>Nombre del cliente</label>
<input type="text" name="nombre" required>

<label>Fecha de reserva</label>
<input type="date" name="fecha" required>

<label>Tipo de espacio</label>
<select name="espacio" required>
<option value="">Seleccione</option>
<option value="Mesa">Mesa</option>
<option value="Sala de reuniones">Sala de reuniones</option>
<option value="Salon de video">Salon de video</option>
</select>

<label>Horas de uso</label>
<input type="number" name="duracion" min="1" max="12" required>

<button class="btn-crear">Guardar reserva</button>

</form>

<div class="bloque-lista">
<a href="SvListaReservas" class="btn-ver">Consultar reservas</a>
</div>

</div>

</body>
</html>