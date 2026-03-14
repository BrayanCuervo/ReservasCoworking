<%@page import="java.util.List"%>
<%@page import="logica.Reserva"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Reservas almacenadas</title>
<link rel="stylesheet" href="style.css">
</head>

<body>

<div class="panel-tabla">

<h1>Listado de reservas</h1>

<table class="tabla-datos">

<thead>
<tr>
<th>Cliente</th>
<th>Día</th>
<th>Área</th>
<th>Tiempo</th>
<th>Opción</th>
</tr>
</thead>

<tbody>

<%
List<Reserva> reservas = (List<Reserva>) request.getAttribute("listaReservas");

for(Reserva item : reservas){
%>

<tr>
<td><%= item.getNombre() %></td>
<td><%= item.getFecha() %></td>
<td><%= item.getEspacio() %></td>
<td><%= item.getDuracion() %> horas</td>

<td>
<a class="btn-borrar"
href="SvEliminarReserva?id=<%= item.getId() %>">
Eliminar
</a>
</td>

</tr>

<%
}
%>

</tbody>

</table>

<a href="index.jsp" class="btn-regresar">
Regresar
</a>

</div>

</body>
</html>