<%-- #{extends 'main.html' /} #{set title:'Edição de Ação' /} --%>

<div class="gt-form gt-content-box">
	<form id="tipoAcaoForm">
		<input type="hidden" name="idTipoAcao">

<%-- 		#{ifErrors} --%>
<!-- 		<p class="gt-error">Alguns campos obrigatórios não foram -->
<%-- 			preenchidos ${error}</p> --%>
<%-- 		#{/ifErrors} --%>
		<div class="gt-form-row gt-width-66">
			<label>C&oacute;digo<span>*</span></label>
			<input type="text" name="siglaTipoAcao" maxlength="255" required/>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>T&iacute;tulo <span>*</span></label> <input type="text"
				name="tituloTipoAcao" size="100" maxlength="255" required/>
		</div>
		<div class="gt-form-row gt-width-66">
			<label>Descri&ccedil;&atilde;o</label> <input type="text"
				name="descrTipoAcao" size="100" maxlength="255"/>
		</div>

		<div class="gt-form-row">
			<input type="button" value="Gravar" class="gt-btn-medium gt-btn-left" onclick="tipoAcaoService.gravar()"/>
			<a class="gt-btn-medium gt-btn-left" onclick="tipoAcaoService.cancelarGravacao()">Cancelar</a>
			<input type="button" value="Aplicar" class="gt-btn-medium gt-btn-left" onclick="tipoAcaoService.aplicar()"/>
		</div>
	</form>
</div>