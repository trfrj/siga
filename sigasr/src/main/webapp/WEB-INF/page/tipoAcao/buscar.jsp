<%-- #{extends 'main.html' /} --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Servi�os">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<div class="gt-content-box gt-for-table">
				<form method="POST" action="${linkTo[TipoAcaoController].buscar}">
					<input type="hidden" name="popup" value="true" />
					<table class="gt-form-table">
						<tr class="header">
							<td align="center" valign="top" colspan="4">Dados do Tipo de a&ccedil;&atilde;o</td>
						</tr>
						<tr>
							<td>C&oacute;digo:</td>
							<td><input type="text" name="filtro.siglaTipoAcao"
								value="${filtro.siglaTipoAcao}" />
							</td>
						</tr>
						<tr>
							<td>T&iacute;tulo</td>
							<td>
								<input type="text" name="filtro.tituloTipoAcao" value="${filtro.tituloTipoAcao}" />
								<input type="hidden" name="nome" value="${nome}" />
							</td>
						</tr>
						<tr>
							<td>
								<input type="hidden" name="pessoa" value="${pessoa}" />
								<input type="hidden" name="item" value="${item}" />
								<input type="submit" class="gt-btn-small gt-btn-left" value="Pesquisar" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<br />
		<div class="gt-content-box gt-for-table">
			<table class="gt-table">
				<thead>
					<tr>
						<th>C&oacute;digo</th>
						<th>Descri&ccedil;&atilde;o</th>
					</tr>
				</thead>
				<c:forEach items="${itens}" var="tipoAcao">
					<tr>
						<td>
							<a href="javascript:opener.retorna_tipoAcao${nome}('${tipoAcao.id}','${tipoAcao.sigla}','${tipoAcao.descricao}');window.close()">${tipoAcao.sigla}</a>
						</td>
						<td style="padding-left: ${tipoAcao.nivel*13}px;">${tipoAcao.descricao}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
</siga:pagina>